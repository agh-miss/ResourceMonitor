/**
 * Copyright (C) 2006 - 2010
 *   Pawel Kedzior
 *   Tomasz Kmiecik
 *   Kamil Pietak
 *   Krzysztof Sikora
 *   Adam Wos
 *   and other students of AGH University of Science and Technology
 *   as indicated in each file separately.
 *
 * This file is part of jAgE.
 *
 * jAgE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jAgE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with jAgE.  If not, see http://www.gnu.org/licenses/
 */
/*
 * File: SimpleAggregate.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: SimpleAggregate.java 213 2012-04-08 20:21:35Z krzywick $
 */

package org.jage.agent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.action.Action;
import org.jage.action.ActionPhase;
import org.jage.action.AgentAction;
import org.jage.action.ComplexAction;
import org.jage.action.IActionContext;
import org.jage.action.IPerformActionStategy;
import org.jage.action.SingleAction;
import org.jage.action.context.AddAgentActionContext;
import org.jage.action.context.AgentActionContext;
import org.jage.action.context.GetAgentActionContext;
import org.jage.action.context.IActionWithAgentReferenceContext;
import org.jage.action.context.KillAgentActionContext;
import org.jage.action.context.MoveAgentActionContext;
import org.jage.action.context.PassToParentActionContext;
import org.jage.action.context.RemoveAgentActionContext;
import org.jage.action.context.SendMessageActionContext;
import org.jage.action.ordering.DefaultActionComparator;
import org.jage.address.IAgentAddress;
import org.jage.address.selector.BroadcastSelector;
import org.jage.address.selector.IAddressSelector;
import org.jage.address.selector.UnicastSelector;
import org.jage.address.selector.agent.ParentAgentAddressSelector;
import org.jage.communication.message.IMessage;
import org.jage.event.AbstractEvent;
import org.jage.event.AgentActionEvent;
import org.jage.event.AggregateEvent;
import org.jage.event.EventQueue;
import org.jage.event.MessageEvent;
import org.jage.monitor.IAgentMonitor;
import org.jage.monitor.IMessageMonitor;
import org.jage.monitor.IQueryMonitor;
import org.jage.monitor.MonitorException;
import org.jage.monitor.MonitorUtil;
import org.jage.platform.component.annotation.Inject;
import org.jage.platform.component.exception.ComponentException;
import org.jage.property.PropertyGetter;
import org.jage.property.PropertySetter;
import org.jage.query.AgentEnvironmentQuery;
import org.jage.util.RebuildablePriorityQueue;

import com.google.common.collect.Maps;

/**
 * The basic implementation of an aggregate.
 *
 * @author AGH AgE Team
 */
public class SimpleAggregate extends AbstractAgent implements ISimpleAggregate, ISimpleAgentEnvironment {

	private static final long serialVersionUID = 4L;

	/** Local environment. */
	private transient IAgentEnvironment localEnvironment;

	/**
	 * The table of child agents <BR>
	 * Keys: AgentAddress <BR>
	 * Values: Agent <BR>
	 * .
	 */
	protected final Map<IAgentAddress, IAgent> agents = Maps.newLinkedHashMap();

	/**
	 * The list of agents set by method setAgents. Agents from this list will be added to the map of agents in init()
	 * method.
	 */
	protected List<IAgent> temporaryAgentsList;

	/** The lock for accessing the table of agents. */
	private final ReentrantReadWriteLock agentsLock = new ReentrantReadWriteLock();

	/**
	 * The list of aggregate monitors. It is created after adding the first monitor and destroyed after removing the
	 * last one.
	 */
	private final ArrayList<IAgentMonitor> aggregateMonitors = new ArrayList<IAgentMonitor>();

	/**
	 * The list of query monitors. It is created after adding the first monitor and destroyed after removing the last
	 * one.
	 */
	private final ArrayList<IQueryMonitor> queryMonitors = new ArrayList<IQueryMonitor>();

	/** The queue of agent's events. */
	private final EventQueue eventQueue = new EventQueue();

	protected final RebuildablePriorityQueue<Action> actionQueue = RebuildablePriorityQueue
			.createWithComparator(new DefaultActionComparator());

	/**
	 * The cache of methods containing actions' performing code (execute in ActionPhase.MAIN) <BR>
	 * Key: class of aggregate (Class) <BR>
	 * Value: map of available actions (action name, method) (Map)
	 */
	private static Map<Class<? extends Object>, Map<String, Map<ActionPhase, Method>>> actionMethods = new HashMap<Class<? extends Object>, Map<String, Map<ActionPhase, Method>>>();

	/**
	 * The list of message monitors. Object must be synchronized by locking it explicitly.
	 */
	private final ArrayList<IMessageMonitor> messageMonitors = new ArrayList<IMessageMonitor>();

	/**
	 * Logger.
	 */
	private static final Logger _log = LoggerFactory.getLogger(SimpleAggregate.class);

	/**
	 * Constructor.
	 */
	@Inject
	public SimpleAggregate() {
		super();
		// empty
	}

	@Override
	public IAgent getAgent(final IAgentAddress addressToGet) {
		IAgent result = null;
		try {
			getAgentsLock().readLock().lockInterruptibly();
			try {
				result = agents.get(addressToGet);
			} finally {
				getAgentsLock().readLock().unlock();
			}
		} catch (InterruptedException e) {
			_log.error("Interrupted in getAgent", e);
		}
		return result;
	}

	@Override
	public boolean containsAgent(final IAgentAddress addressToCheck) {
		boolean result = false;
		try {
			getAgentsLock().readLock().lockInterruptibly();
			try {
				result = agents.containsKey(addressToCheck);
			} finally {
				getAgentsLock().readLock().unlock();
			}
		} catch (InterruptedException e) {
			_log.error("Interrupted in containsAgent", e);
		}
		return result;
	}

	@Override
	public void removeAgent(final IAgentAddress agentAddress) throws AgentException {
		try {
			getAgentsLock().writeLock().lockInterruptibly();
			try {
				removeAgentWithoutSynch(agentAddress);
			} finally {
				getAgentsLock().writeLock().unlock();
			}
		} catch (InterruptedException e) {
			_log.error("Interrupted in removeAgent", e);
		}
	}

	private void removeAgentWithoutSynch(final IAgentAddress agentAddress) {
		try {
			IAgent agent = agents.remove(agentAddress);
			if (agent != null) {
				agent.setAgentEnvironment(null);
				_log.debug("Agent {} removed from the aggregate {}", agent.getAddress(), getAddress());
			}
		} catch (AlreadyExistsException e) {
			_log.error("Can't unset agent's environment.", e);
		}
	}

	/**
	 * Sets the agents list. Agents are stored in a temporary list and will be actually added during initialization.
	 *
	 * @param agents
	 *            the new agents list
	 */
	@Inject
	@PropertySetter(propertyName = "agents")
	public final void setAgents(final List<IAgent> agents) {
		temporaryAgentsList = agents;
		_log.info("Agents added.");
	}

	/**
	 * Gets the agents list.
	 *
	 * @return the agents list
	 */
	@PropertyGetter(propertyName = "agents")
	public final List<IAgent> getAgents() {
		try {
			getAgentsLock().readLock().lockInterruptibly();
			return Collections.unmodifiableList(new ArrayList<IAgent>(agents.values()));
		} catch (InterruptedException e) {
			_log.error("Interrupted while aquiring reader lock", e);
		} finally {
			getAgentsLock().readLock().unlock();
		}
		return Collections.emptyList();
	}

	/**
	 * Provides agents lock. Returns always the same instance of the lock.
	 *
	 * @return agents lock
	 */
	protected ReentrantReadWriteLock getAgentsLock() {
		return agentsLock;
	}

	@Override
	public void addAgentMonitor(final IAgentMonitor monitor) {
		synchronized (aggregateMonitors) {
			aggregateMonitors.add(monitor);
		}
	}

	@Override
	public IAgentMonitor removeAgentMonitor(final IAgentMonitor monitor) {
		synchronized (aggregateMonitors) {
			return aggregateMonitors.remove(monitor) ? monitor : null;
		}
	}

	/**
	 * Processes the event queue and performs existing events.
	 */
	protected void processEvents() {
		AggregateEvent event = null;
		while ((event = eventQueue.removeFirst()) != null) {
			processEvent(event);
		}
	}

	protected void processActions() {
		Action action;
		while ((action = actionQueue.poll()) != null) {
			try {
				processAction(action);
			} catch (AgentException e) {
				log.error("Action could not be executed.", e);
			}
		}
	}

	/**
	 * Processes an event.
	 *
	 * @param event
	 *            the event to process
	 */
	// XXX: After removal of the AgentActionEvent it is useless on its own. Consider its deletion or improvements.
	protected void processEvent(final AggregateEvent event) {
		_log.warn("Unknown event to process: {}", event);
		_log.info("SimpleAggregate does not support any events currently.");
	}

	/**
	 * Processes an action event and executes appropriate action.
	 *
	 * @param event
	 *            the event to process
	 *
	 * @throws AgentException
	 *             occurs when the requested action cannot be found or executed
	 */
	protected void processAction(final Action action) throws AgentException {
		// phase I: initialization and validation
		Collection<IAgentAddress> used = processActionInitialization(action);

		// address generation
		processActionAddressGeneration(action, agents.keySet(), used);

		// phase II: perform main action part
		processActionPerform(action);

		// phase III: finalization
		processActionFinalization(action);
	}

	@SuppressWarnings("unchecked")
	protected Collection<IAgentAddress> processActionInitialization(final Action action) throws AgentException {

		Collection<IAgentAddress> used = new HashSet<IAgentAddress>();

		for (SingleAction singleAction : action) {

			IActionContext context = singleAction.getContext();
			String actionName = processEventGetActionName(singleAction);

			// addresses used by single action
			Collection<IAgentAddress> usedByAction = null;

			/*
			 * First look for a strategic implementation of the action. If this is not found, the action's
			 * implementation is looked up in the aggregate.
			 */
			IPerformActionStategy actionStrategy = processEventActionContextStrategy(context, actionName);
			if (actionStrategy != null) {
				usedByAction = actionStrategy.init(this, singleAction);
			} else {
				Method actionMethod = null;
				try {
					actionMethod = getActionMethod(this.getClass(), actionName, ActionPhase.INIT);
				} catch (AgentException e) {
					// action is not a strategy and has no aggregates methods
					// defined (in all phases).
					handleUnknownAction(actionName, context);
				}

				if (actionMethod != null) {
					usedByAction = (Collection<IAgentAddress>)processActionContextMethod(actionMethod,
					        new Object[] { singleAction }, actionName);
				}

			}

			if (usedByAction == null) { // if no validation done yet, do
				// default validation now
				usedByAction = validateAction(singleAction);
			}

			assert usedByAction != null;
			used.addAll(usedByAction);
		}

		return used;
	}

	/**
	 * Initializes all address selectors in action using all and used collections of agent addresses. For more details
	 * see {@link IAddressSelector#initialize(Collection, Collection)}.
	 *
	 * @param action
	 *            action which contains selectors to initialize
	 * @param all
	 *            collection of all available addresses
	 * @param used
	 *            collection of all used addresses
	 */
	protected void processActionAddressGeneration(final Action action, final Collection<IAgentAddress> all,
	        final Collection<IAgentAddress> used) {

		assert action != null && all != null && used != null;

		for (SingleAction singleAction : action) {
			assert singleAction.getTarget() != null;
			singleAction.getTarget().initialize(all, used);
		}
	}

	private List<IAgent> getTargetAgentsForAction(final SingleAction singleAction) throws AgentException {
		IAddressSelector<IAgentAddress> selector = singleAction.getTarget();

		List<IAgent> targets = new ArrayList<IAgent>();
		if (selector instanceof ParentAgentAddressSelector) {
			if (getListOfAgentsAddresses().contains(((ParentAgentAddressSelector)selector).getChildAddress())) {
				targets.add(this);
			}
		} else {
			for (IAgentAddress target : selector.addresses()) {
				assert target != null;
				final IAgent targetAgent = getAgent(target);

				if (targetAgent == null) {
					throw new AgentException(
					        "Target agent does not exists even if validation proccess was passed. [Agent address: "
					                + target + "]");
				}
				targets.add(targetAgent);
			}
		}
		return targets;
	}

	/**
	 * Performs action. If action is a complex action, it iterates over all single actions and performs them.
	 *
	 * @param action
	 *            action to perform
	 */
	protected void processActionPerform(final Action action) throws AgentException {
		assert action != null;
		for (SingleAction singleAction : action) {

			for (IAgent targetAgent : getTargetAgentsForAction(singleAction)) {
				String actionName = processEventGetActionName(singleAction);
				IActionContext context = singleAction.getContext();

				/*
				 * First look for a strategic implementation of the action. If this is not found, the action's
				 * implementation is looked up in the aggregate.
				 */
				IPerformActionStategy actionStrategy = processEventActionContextStrategy(context, actionName);
				if (actionStrategy != null) {
					try {
						actionStrategy.perfom(targetAgent, context);
					} catch (RuntimeException e) {
						throw new AgentException("An exception occured during performing action " + action, e);
					}
				} else {
					Method actionMethod = null;
					try {
						actionMethod = getActionMethod(this.getClass(), actionName, ActionPhase.MAIN);
					} catch (AgentException e) {
						handleUnknownAction(actionName, context);
					}
					if (actionMethod != null) {
						try {
							processActionContextMethod(actionMethod, new Object[] { targetAgent, context }, actionName);
						} catch (RuntimeException e) {
							throw new AgentException("An exception occured during performing action " + action, e);
						}
					} else {
						performDefaultAction(actionName, targetAgent, context);
					}
				}
			}
		}
	}

	protected void processActionFinalization(final Action action) throws AgentException {

		for (SingleAction singleAction : action) {
			for (IAgent targetAgent : getTargetAgentsForAction(singleAction)) {

				IActionContext context = singleAction.getContext();
				String actionName = processEventGetActionName(singleAction);

				/*
				 * First look for a strategic implementation of the action. If this is not found, the action's
				 * implementation is looked up in the aggregate.
				 */
				IPerformActionStategy actionStrategy = processEventActionContextStrategy(context, actionName);
				if (actionStrategy != null) {
					actionStrategy.finish(this, context);
				} else {
					Method actionMethod = null;
					try {
						actionMethod = getActionMethod(this.getClass(), actionName, ActionPhase.FINISH);
					} catch (AgentException e) {
						handleUnknownAction(actionName, context);
					}
					if (actionMethod != null) {
						processActionContextMethod(actionMethod, new Object[] { targetAgent, context }, actionName);
					} else {
						finishDefaultAction(context, actionName);
					}
				}
			}
		}

	}

	/**
	 * A default implementation of action validation. It checks if used addresses point to agents in current aggregate.
	 *
	 * @param action
	 *            single action to validate
	 * @return collection of agent addresses used in action, if no addresses is used (action has selectors such as
	 *         {@link BroadcastSelector}, etc.) the empty collection is returned; <code>null</code> is returned when
	 *         action didn't validate addresses - then a default validation is performed
	 * @throws AgentException
	 *             when validation fails.
	 */
	@Override
	public Collection<IAgentAddress> validateAction(final SingleAction action) throws AgentException {
		assert action.getTarget() != null : "SingleAction cannot have null address selector";

		Collection<IAgentAddress> used = new HashSet<IAgentAddress>();

		IAddressSelector<IAgentAddress> selector = action.getTarget();

		if (selector instanceof ParentAgentAddressSelector) {
			if (getListOfAgentsAddresses().contains(((ParentAgentAddressSelector)selector).getChildAddress())) {
				used.add(getAddress());
			}
		} else {
			for (IAgentAddress address : selector.addresses()) {
				assert address != null : "Validation error - address is null";

				if (getAgent(address) == null) {
					throw new AgentException("Agent: [" + address + "] doesn't exist in: " + getAddress()
					        + ". Action " + action.toString() + " couldn't be performed");
				} else {
					// add address to used collection (only if agent exists in this aggregate)
					used.add(address);
				}
			}
		}
		return used;
	}

	/**
	 * A default implementation that handles unknown actions finalization in the aggregate. It does nothing.
	 *
	 * @param context
	 *            action context
	 * @param actionName
	 *            action name
	 */
	protected void finishDefaultAction(final IActionContext context, final String actionName) {
		// do nothing
	}

	/**
	 * A default implementation that handles unknown actions in the aggregate. Does nothing. May be overridden by
	 * subclasses.
	 *
	 * @param actionName
	 *            name of unknown action
	 * @param targetAgent
	 *            agent to invoke action on
	 * @param context
	 *            action context
	 * @throws AgentException
	 *             by default
	 */
	protected void performDefaultAction(final String actionName, final IAgent targetAgent, final IActionContext context)
	        throws AgentException {

		// do nothing
	}

	/**
	 * A method which handles unknown (not found as strategy and aggregate's methods) action. Throws exception. May be
	 * overridden by subclasses to modify default behavior.
	 *
	 * @param actionName
	 *            action name
	 * @param context
	 *            action context
	 * @throws AgentException
	 */
	protected void handleUnknownAction(final String actionName, final IActionContext context) throws AgentException {
		throw new AgentException("The action cannot be found [name: " + actionName + "]");
	}

	/**
	 * Determine name of action that should be executed. If context is annotated with one action only, this is obvious.
	 * If it's annotated with more than one action, we need to check if an action to execute is specified and if it is
	 * on the list of supported actions
	 *
	 * @param singleAction
	 *            the single action
	 * @param context
	 *            the context
	 * @param contextAnnotation
	 *            the context annotation
	 *
	 * @return single string with name of action to execute
	 *
	 * @throws AgentException
	 *             the agent exception
	 */
	private String processEventGetActionName(final SingleAction singleAction) throws AgentException {

		IActionContext context = singleAction.getContext();
		AgentActionContext contextAnnotation = context.getClass().getAnnotation(AgentActionContext.class);
		if (contextAnnotation == null) {
			throw new AgentException("ActionContext class [" + context.getClass().toString()
			        + "]isn't annotated correctly\n" + "ActionContext require to be annotated by AgentActionContext");
		}

		String[] actionNames = contextAnnotation.value();
		String actionName = null;
		if (actionNames.length == 1) {
			// context is annotated with 1 action only - simple case
			actionName = actionNames[0];
		} else {
			// context is multiannotated
			String actionToExecute = singleAction.getActionToExecute();
			if (null == actionToExecute) {
				throw new AgentException("ActionContext " + context.getClass().toString()
				        + " is annotated with more than one action name, but no action"
				        + " was specified to be executed in the SingleAction object");
			}
			for (String name : actionNames) {
				if (name.equals(actionToExecute)) {
					actionName = actionToExecute;
					break;
				}
			}
			if (null == actionName) {
				throw new AgentException("ActionContext " + context.getClass().toString()
				        + " annotation does not support execution of action '" + actionToExecute + "'");
			}
		}
		return actionName;
	}

	private Object processActionContextMethod(final Method actionMethod, final Object[] arguments, final String actionName)
	        throws AgentException {

		try {
			/*
			 * Protected and package action methods declared in aggregates in a different package will not be accessible
			 * by default - this is fixed below by setting the method's accessible attribute.
			 */
			if (Modifier.isProtected(actionMethod.getModifiers()) || actionMethod.getModifiers() == 0) {
				actionMethod.setAccessible(true);
			}
			return actionMethod.invoke(this, arguments);
		} catch (IllegalAccessException e) {
			throw new AgentException("Cannot execute the action: " + actionName, e);
		} catch (IllegalArgumentException e) {
			Class<?>[] params = actionMethod.getParameterTypes();

			StringBuffer buffer = new StringBuffer();
			buffer.append("Cannot execute the action: ");
			buffer.append(actionName);
			buffer.append(" method: ");
			buffer.append(actionMethod.getName());
			buffer.append("(");
			for (Class<?> param : params) {
				buffer.append(param.getSimpleName());
				buffer.append(", ");
			}
			buffer.append(")");
			buffer.append(" arguments: ");
			for (Object argument : arguments) {
				buffer.append(argument.getClass().getSimpleName());
				buffer.append(", ");
			}
			_log.error(buffer.toString());
			throw new AgentException(buffer.toString(), e);
		} catch (InvocationTargetException e) {
			throw new AgentException("Cannot execute the action: " + actionName, e.getCause());
		}
	}

	/**
	 * actionContext points on action which will be executed without using Aggregate's method (implementation will be
	 * provided by ResourceManager).
	 *
	 * @param targetAgent
	 *            the target agent
	 * @param context
	 *            the context
	 * @param actionName
	 *            the action name
	 *
	 * @throws AgentException
	 *             the agent exception
	 */
	private IPerformActionStategy processEventActionContextStrategy(final IActionContext context, final String actionName)
	        throws AgentException {

		Object obj = instanceProvider.getInstance(actionName);
		if (obj == null) {
			return null;
		}
		if (!(obj instanceof IPerformActionStategy)) {
			throw new AgentException("Requested object " + actionName + " is not an action implementation");
		}

		return (IPerformActionStategy)obj;
	}

	/**
	 * Executes the action of sending a message to an agent.
	 *
	 * @param target
	 *            the target
	 * @param context
	 *            the context
	 */
	@AgentAction(name = SendMessageActionContext.ACTION_NAME)
	protected void performSendMessageAction(final ISimpleAgent target, final IActionContext context) {
		SendMessageActionContext action = (SendMessageActionContext)context;
		IAgentAddress destAddress = target.getAddress();
		IMessage<IAgentAddress, ?> message = action.getMessage();

		notifyMessageMonitorsMessageSent(new MessageEvent(this, message, destAddress));

		target.deliverMessage(message);
		_log.info("Message delivered to the agent [receiver: " + destAddress + "]");
	}

	/**
	 * Notifies message monitors about given event. Note: a monitor can be notified in a short time after it is
	 * unregistered.
	 *
	 * @param messageEvent
	 *            the event
	 */
	private void notifyMessageMonitorsMessageSent(final MessageEvent messageEvent) {
		Collection<IMessageMonitor> copy;
		synchronized (messageMonitors) {
			copy = new ArrayList<IMessageMonitor>(messageMonitors);
		}
		for (IMessageMonitor messageMonitor : copy) {
			messageMonitor.messageSent(messageEvent);
		}
	}

	private Collection<IAgentAddress> getListOfAgentsAddresses() {
		Collection<IAgentAddress> addresses = new LinkedList<IAgentAddress>();

		for (IAgent agent : getAgents()) {
			addresses.add(agent.getAddress());
		}
		return addresses;
	}

	@Override
	public <E extends IAgent, T> Collection<T> queryParent(final AgentEnvironmentQuery<E, T> query) {
		try {
			return queryEnvironment(query);
		} catch (AgentException ae) {
			_log.error("Can't query parent environment");
			return null;
		}
	}

	@Override
	public void doAction(final Action action, final IAgentAddress invoker) {
		actionQueue.add(action);
	}

	/**
	 * Returns the method which contains the code of the given action in specified phase.
	 *
	 * @param aggrClass
	 *            the class of aggregate on which the action is to be executed
	 * @param actionName
	 *            the name of the action
	 * @param phase
	 *            action phase
	 *
	 * @return the method which contains the code of the given action or <code>null</code> if method does not exists in
	 *         given phase
	 *
	 * @throws AgentException
	 *             occurs when no method is defined for action in all phases
	 */
	private static Method getActionMethod(final Class<? extends Object> aggrClass, final String actionName, final ActionPhase phase)
	        throws AgentException {
		Map<String, Map<ActionPhase, Method>> methodMap = actionMethods.get(aggrClass);
		if (methodMap == null) {
			methodMap = getActionMethods(aggrClass);
			actionMethods.put(aggrClass, methodMap);
		}

		Method result = null;
		Map<ActionPhase, Method> actionMap = methodMap.get(actionName);

		if (actionMap == null) {
			throw new AgentException("The action cannot be found [name: " + actionName + "]");
		} else {
			result = actionMap.get(phase);
		}

		return result;
	}

	/**
	 * Returns the map of action methods of the given class.
	 *
	 * @param aggrClass
	 *            the class of an aggregate
	 *
	 * @return the map (action name, method) of action methods of the given class
	 */
	private static Map<String, Map<ActionPhase, Method>> getActionMethods(final Class<? extends Object> aggrClass) {

		HashMap<String, Map<ActionPhase, Method>> result = new HashMap<String, Map<ActionPhase, Method>>();
		// annotation mechanism for reading actions
		Method[] methods = aggrClass.getDeclaredMethods();
		for (Method method : methods) {
			AgentAction action = method.getAnnotation(AgentAction.class);
			if (action != null) {
				String name = action.name();
				ActionPhase phase = action.phase();
				Map<ActionPhase, Method> actionMap = result.get(name);
				if (actionMap == null) {
					actionMap = new HashMap<ActionPhase, Method>();
				}
				actionMap.put(phase, method);
				result.put(name, actionMap);
				_log.debug("Method: {} added", action.name());
			}
		}
		if (!SimpleAggregate.class.equals(aggrClass)) {
			result.putAll(getActionMethods(aggrClass.getSuperclass()));
		}

		return result;
	}

	/**
	 * Executes the action of adding an agent to this aggregate.
	 *
	 * @param context
	 *            context of the action
	 */
	@AgentAction(name = AddAgentActionContext.ACTION_NAME, phase = ActionPhase.INIT)
	protected void performAddAgentAction(final SingleAction action) {

		AddAgentActionContext addContext = (AddAgentActionContext)action.getContext();
		add(addContext.getAgent());
		AggregateEvent event = new AgentActionEvent(this, new SingleAction(new UnicastSelector<IAgentAddress>(
		        getAddress()), addContext), null);

		for (IAgentMonitor monitor : MonitorUtil.getCopy(aggregateMonitors)) {
			monitor.agentAdded(event);
		}

	}

	/**
	 * Executes the action of removing an agent from this aggregate.
	 *
	 * @param target
	 *            agent to be removed
	 * @param context
	 *            context of the action
	 */
	@AgentAction(name = RemoveAgentActionContext.ACTION_NAME)
	protected void performRemoveAgentAction(final ISimpleAgent target, final IActionContext context) {
		try {
			removeAgent(target.getAddress());
			AgentActionEvent event = new AgentActionEvent(this, new SingleAction(new UnicastSelector<IAgentAddress>(
			        target.getAddress()), context), null);

			for (IAgentMonitor monitor : MonitorUtil.getCopy(aggregateMonitors)) {
				monitor.agentRemoved(event);
			}
		} catch (AgentException e) {
			_log.error("Cannot remove the agent [agent: {}, parent: {}]",
			        new Object[] { target.getAddress(), getAddress() }, e);
		}
	}

	/**
	 * Perform kill agent action.
	 *
	 * @param target
	 *            the target
	 * @param context
	 *            the context
	 */
	@AgentAction(name = KillAgentActionContext.ACTION_NAME, phase = ActionPhase.FINISH)
	protected void performKillAgentAction(final ISimpleAgent target, final IActionContext context) {

		try {
			removeAgent(target.getAddress());

			AgentActionEvent event = new AgentActionEvent(this, new SingleAction(new UnicastSelector<IAgentAddress>(
			        target.getAddress()), context), null);
			// notifying aggregate monitors
			for (IAgentMonitor monitor : MonitorUtil.getCopy(aggregateMonitors)) {
				monitor.agentKilled(event);
			}

			target.finish();
			target.objectDeleted(event);
		} catch (AgentException e) {
			_log.info("Cannot kill the agent [agent: {}, parent: {}]", target.getAddress(), getAddress());
		} catch (ComponentException e) {
			// FIXME Should we use IStatefulComponent.finish() there?
			_log.info("Cannot kill the agent [agent: " + target.getAddress() + ", parent: " + getAddress() + "]");
		}
	}

	/**
	 * Part of moving action. Removing an agent.
	 *
	 * @param action
	 *            the action
	 * @param target
	 *            target
	 *
	 * @throws AgentException
	 *             if fails
	 */
	private void performMoveAgentActionRemove(final MoveAgentActionContext action, final ISimpleAgent target) throws AgentException {
		try {
			IAggregate parent = action.getParent();
			parent.removeAgent(action.getAgent().getAddress());
			// TODO change monitor's interface
			AgentActionEvent event = new AgentActionEvent(this, new SingleAction(new UnicastSelector<IAgentAddress>(
			        target.getAddress()), action), null);
			for (IAgentMonitor monitor : MonitorUtil.getCopy(aggregateMonitors)) {
				monitor.agentMovedOut(event);
			}
		} catch (AgentException e) {
			_log.info("Cannot remove the agent [agent: {}, parent: {}]", action.getAgent().getAddress(), action
			        .getParent().getAddress());
			throw e;
		}
	}

	/**
	 * Adds the agent to target aggregate.
	 *
	 * @param action
	 *            an action
	 * @param target
	 *            target
	 *
	 * @throws AgentException
	 *             when the operation must be rolled back
	 */
	private void performMoveAgentActionAdd(final MoveAgentActionContext action, final IAggregate target) throws AgentException {
		target.add(action.getAgent());

		// TODO change monitor's interface
		AgentActionEvent event = new AgentActionEvent(this, new SingleAction(new UnicastSelector<IAgentAddress>(
		        target.getAddress()), action), null);
		for (IAgentMonitor monitor : MonitorUtil.getCopy(aggregateMonitors)) {
			monitor.agentMovedIn(event);
		}
	}

	/**
	 * Adds an agent back to its aggregate.
	 *
	 * @param action
	 *            an action
	 * @param cause
	 *            the cause
	 */
	private void performMoveAgentActionRollback(final MoveAgentActionContext action, final Throwable cause) {
		action.getParent().add(action.getAgent());
		_log.info("Agent added back to his parent [agent: {}, parent: {}]", action.getAgent().getAddress(), action
		        .getParent().getAddress());
	}

	/**
	 * Perform move agent action.
	 *
	 * @param target
	 *            the target
	 * @param context
	 *            the context
	 */
	@AgentAction(name = MoveAgentActionContext.ACTION_NAME)
	protected void performMoveAgentAction(final ISimpleAgent target, final IActionContext context) {
		MoveAgentActionContext moveContext = (MoveAgentActionContext)context;
		if (moveContext.getAgent() == null) {
			_log.info("Cannot remove the agent");
			return;
		}
		if (!(target instanceof IAggregate)) {
			_log.info("Cannot move the agent. "
			        + "The target parent is not an aggregate [agent: {}, parent: {}, destination: {}]", new Object[] {
			        moveContext.getAgent().getAddress(), moveContext.getParent().getAddress(), target.getAddress() });
			return;
		}

		try {
			performMoveAgentActionRemove(moveContext, target);
		} catch (AgentException ae) {
			return;
		}

		try {
			performMoveAgentActionAdd(moveContext, (IAggregate)target);
		} catch (AgentException ae) {
			performMoveAgentActionRollback(moveContext, ae);
		}
	}

	/**
	 * Gets agent's reference and its parent and save it into context The action can be used in action such as
	 * MoveAgentAction.
	 *
	 * @param target
	 *            the target
	 * @param context
	 *            the context
	 */
	@AgentAction(name = GetAgentActionContext.ACTION_NAME)
	protected void performGetAgentAction(final ISimpleAgent target, final IActionContext context) {
		GetAgentActionContext action = (GetAgentActionContext)context;
		IActionWithAgentReferenceContext awarc = action.getActionWithAgentReferenceContext();
		awarc.setAgent(target);
		awarc.setParent(this);
	}

	/**
	 * Passes action held in a context to parent agent.
	 *
	 * @param target
	 *            this agent
	 * @param context
	 *            action context
	 * @throws AgentException
	 *             when agent environment is <code>null</code> or does not implement {@link ISimpleAgentEnvironment}
	 */
	@AgentAction(name = PassToParentActionContext.ACTION_NAME)
	protected void performPassToParentAction(final ISimpleAgent target, final PassToParentActionContext context)
	        throws AgentException {
		if (getAgentEnvironment() != null && getAgentEnvironment() instanceof ISimpleAgentEnvironment) {
			((ISimpleAgentEnvironment)getAgentEnvironment()).doAction(context.getAction(), context.getInvoker());
		} else {
			throw new AgentException(String.format(
			        "Cannot pass action %1$s to environment - it is not instance of ISimpleAgentEnvironment.",
			        context.getAction()));
		}
	}

	@Override
	public void deliverMessage(final IMessage<IAgentAddress, ?> message) {
		super.deliverMessage(message);
		messageDelivered(message, getAddress());
	}

	/**
	 * Informs message monitors about delivering a message.
	 *
	 * @param message
	 *            the delivered message
	 * @param receiver
	 *            the receiver of the message
	 */
	private void messageDelivered(final IMessage<IAgentAddress, ?> message, final IAgentAddress receiver) {
		MessageEvent messageEvent = new MessageEvent(this, message, receiver);
		for (IMessageMonitor monitor : MonitorUtil.getCopy(messageMonitors)) {
			monitor.messageDelivered(messageEvent);
		}
	}

	@Override
	public void addMessageMonitor(final IMessageMonitor monitor) {
		synchronized (messageMonitors) {
			messageMonitors.add(monitor);
		}
	}

	@Override
	public void objectDeleted(final AbstractEvent event) throws MonitorException {
		super.objectDeleted(event);
		for (IMessageMonitor monitor : MonitorUtil.getCopy(messageMonitors)) {
			monitor.ownerDeleted(event);
		}
	}

	@Override
	public IMessageMonitor removeMessageMonitor(final IMessageMonitor monitor) {
		synchronized (messageMonitors) {
			return messageMonitors.remove(monitor) ? monitor : null;
		}
	}

	@Override
	public void addQueryMonitor(final IQueryMonitor monitor) {
		synchronized (queryMonitors) {
			queryMonitors.add(monitor);
		}
	}

	@Override
	public IQueryMonitor removeQueryMonitor(final IQueryMonitor monitor) {
		synchronized (queryMonitors) {
			return queryMonitors.remove(monitor) ? monitor : null;
		}
	}

	// BEGIN methods for the Collection interface

	@Override
	public boolean add(final IAgent agent) {
		try {
			try {
				getAgentsLock().writeLock().lockInterruptibly();
				return addAgentWithoutSynchronization(agent);
			} finally {
				getAgentsLock().writeLock().unlock();
			}
		} catch (InterruptedException e) {
			_log.error("Interrupted in addAgent", e);
			return false;
		}
	}

	/**
	 * Adds the agent to the map of agents but the adding operation is not synchronized.
	 *
	 * @param agent
	 *            the agent
	 *
	 * @return true, if successful, false if agent already exists in this environment
	 */
	private boolean addAgentWithoutSynchronization(final IAgent agent) {
		try {
			IAgentAddress agentAddress = agent.getAddress();

			// Check whether another *instance* of agent exists - possible error?
			if (agents.containsKey(agentAddress) && agents.get(agentAddress) != agent) {
				_log.error("Another instance of the agent with address '{}' already exists in this environment.",
				        agentAddress);
				return false;
			}
			agents.put(agentAddress, agent);
			agent.setAgentEnvironment(this);

			_log.debug("Agent {} added to the aggregate {}", agentAddress, getAddress());
			return true;

		} catch (AlreadyExistsException e) {
			_log.error("Agent already exist in this environment.", e);
			return false;
		}
	}

	@Override
	public boolean addAll(final Collection<? extends IAgent> agents) {
		try {
			try {
				getAgentsLock().writeLock().lockInterruptibly();
				for (IAgent agent : agents) {
					if (!addAgentWithoutSynchronization(agent)) {
						return false;
					}
				}
				return true;
			} finally {
				getAgentsLock().writeLock().unlock();
			}
		} catch (InterruptedException e) {
			_log.error("Interrupted in addAll", e);
			return false;
		}
	}

	@Override
	public void clear() {
		try {
			getAgentsLock().writeLock().lockInterruptibly();
			try {
				agents.clear();
			} finally {
				getAgentsLock().writeLock().unlock();
			}
		} catch (InterruptedException e) {
			_log.error("Interrupted in clear", e);
		}
	}

	@Override
	public boolean contains(final Object o) {
		boolean result = false;
		try {
			getAgentsLock().readLock().lockInterruptibly();
			try {
				result = agents.containsValue(o);
			} finally {
				getAgentsLock().readLock().unlock();
			}
		} catch (InterruptedException e) {
			_log.error("Interrupted in contains", e);
		}
		return result;
	}

	@Override
	public boolean containsAll(final Collection<?> c) {
		boolean result = false;
		try {
			getAgentsLock().readLock().lockInterruptibly();
			try {
				result = agents.values().containsAll(c);
			} finally {
				getAgentsLock().readLock().unlock();
			}
		} catch (InterruptedException e) {
			_log.error("Interrupted in containsAll", e);
		}
		return result;
	}

	@Override
	public boolean isEmpty() {
		boolean result = false;
		try {
			getAgentsLock().readLock().lockInterruptibly();
			try {
				result = agents.isEmpty();
			} finally {
				getAgentsLock().readLock().unlock();
			}
		} catch (InterruptedException e) {
			_log.error("Interrupted in isEmpty", e);
		}
		return result;
	}

	@Override
	public Iterator<IAgent> iterator() {
		try {
			getAgentsLock().readLock().lockInterruptibly();
			try {
				return agents.values().iterator();
			} finally {
				getAgentsLock().readLock().unlock();
			}
		} catch (InterruptedException e) {
			_log.error("Interrupted in iterator", e);
			return null;
		}
	}

	@Override
	public boolean remove(final Object o) {
		try {
			getAgentsLock().writeLock().lockInterruptibly();
			try {
				removeAgentWithoutSynch(((IAgent)o).getAddress());
				return true;
			} finally {
				getAgentsLock().writeLock().unlock();
			}
		} catch (InterruptedException e) {
			_log.error("Interrupted in remove", e);
		}
		return false;
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		try {
			getAgentsLock().writeLock().lockInterruptibly();
			try {
				return agents.values().removeAll(c);
			} finally {
				getAgentsLock().writeLock().unlock();
			}
		} catch (InterruptedException e) {
			_log.error("Interrupted in removeAll", e);
		}
		return false;
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		try {
			getAgentsLock().writeLock().lockInterruptibly();
			try {
				return agents.values().retainAll(c);
			} finally {
				getAgentsLock().writeLock().unlock();
			}
		} catch (InterruptedException e) {
			_log.error("Interrupted in retainAll", e);
		}
		return false;
	}

	@Override
	public int size() {
		try {
			getAgentsLock().readLock().lockInterruptibly();
			try {
				return agents.size();
			} finally {
				getAgentsLock().readLock().unlock();
			}
		} catch (InterruptedException e) {
			_log.error("Interrupted in size", e);
		}
		return -1;
	}

	@Override
	public Object[] toArray() {
		try {
			getAgentsLock().readLock().lockInterruptibly();
			try {
				return agents.values().toArray();
			} finally {
				getAgentsLock().readLock().unlock();
			}
		} catch (InterruptedException e) {
			_log.error("Interrupted in toArray", e);
		}
		return null;
	}

	@Override
	public <T> T[] toArray(final T[] a) {
		try {
			getAgentsLock().readLock().lockInterruptibly();
			try {
				return agents.values().toArray(a);
			} finally {
				getAgentsLock().readLock().unlock();
			}
		} catch (InterruptedException e) {
			_log.error("Interrupted in toArray", e);
		}
		return null;
	}

	// END methods for the Collection interface

	/**
	 * Sends an action to execute in the local environment.
	 *
	 * @param action
	 *            the action to execute
	 *
	 * @throws AgentException
	 *             occurs when the action is incorrect
	 */
	protected void doAction(final Action action) throws AgentException {
		ISimpleAgentEnvironment localEnvironment = (ISimpleAgentEnvironment)getAgentEnvironment();
		if (localEnvironment != null) {
			localEnvironment.doAction(action, address);
		} else {
			throw new AgentException("Local environment is not available.");
		}
	}

	protected void doActions(final Collection<? extends Action> actions) throws AgentException {
		ISimpleAgentEnvironment agentEnvironment = (ISimpleAgentEnvironment)getAgentEnvironment();
		if (agentEnvironment != null) {
			for (Action action : actions) {
				agentEnvironment.doAction(action, address);
			}
		} else {
			throw new AgentException("Local environment is not available.");
		}
	}

	/**
	 * Kills the agent.
	 *
	 * @throws AgentException
	 *             occurs when the agent cannot be killed
	 */
	protected final void doDie() throws AgentException {
		doAction(new SingleAction(new UnicastSelector<IAgentAddress>(address), new KillAgentActionContext()));
	}

	/**
	 * Sends message to other agents on the same level in the tree of agents (or to the parent). To send message to
	 * children use their {@link #deliverMessage(IMessage)} method.
	 *
	 * @param message
	 *            The message to send.
	 *
	 * @throws AgentException
	 *             occurs when the message cannot be sent.
	 */
	protected void sendMessage(final IMessage<IAgentAddress, ?> message) throws AgentException {
		doAction(new SingleAction(message.getHeader().getReceiverSelector(), new SendMessageActionContext(message)));
	}

	@Override
	public void setAgentEnvironment(final IAgentEnvironment localEnvironment) throws AlreadyExistsException {

		if ((this.localEnvironment == null) || (localEnvironment == null)) {
			this.localEnvironment = localEnvironment;

			processEvents();
			processActions();
			if (temporaryAgentsList != null) {
				addAll(temporaryAgentsList);
				temporaryAgentsList = null;
			}
		} else {
			throw new AlreadyExistsException("Environment in " + getAddress() + " is already set.");
		}
	}

	@Override
	protected IAgentEnvironment getAgentEnvironment() {
		return localEnvironment;
	}

	/**
	 * Moves the agent.
	 *
	 * @param address
	 *            the address of target aggregate
	 *
	 * @throws AgentException
	 *             occurs when the agent cannot be moved
	 */
	protected final void doMove(final IAgentAddress address) throws AgentException {
		MoveAgentActionContext moveContext = new MoveAgentActionContext();
		GetAgentActionContext getAgentContext = new GetAgentActionContext(moveContext);
		ComplexAction action = new ComplexAction();
		action.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(getAddress()), getAgentContext));
		action.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(address), moveContext));
		doAction(action);
	}

	/**
	 * Performs step of all agents and processes events.
	 */
	@Override
	public void step() {
		try {
			getAgentsLock().readLock().lockInterruptibly();
			try {
				// running agents
				for (IAgent agent : agents.values()) {
					ISimpleAgent simpleAgent = (ISimpleAgent)agent;
					simpleAgent.step();
				}
			} finally {
				// step++;
				getAgentsLock().readLock().unlock();
			}
		} catch (InterruptedException e) {
			_log.error("Interrupted in run", e);
		}

		processEvents();
		processActions();

		notifyMonitorsForChangedProperties();
	}

	@Override
	public boolean finish() throws ComponentException {
		try {
			getAgentsLock().readLock().lockInterruptibly();
			try {
				for (IAgent agent : agents.values()) {
					agent.finish();
				}
			} finally {
				getAgentsLock().readLock().unlock();
			}
		} catch (InterruptedException e) {
			_log.warn("Interrupted in finish.", e);
		}
		return super.finish();
	}

}
