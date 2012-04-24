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
 * File: PicoWorkplaceManager.java
 * Created: 2008-10-07
 * Author: kpietak, krzs
 * $Id: PicoWorkplaceManager.java 139 2012-03-19 22:54:18Z faber $
 */

package org.jage.pico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.address.IAgentAddress;
import org.jage.address.selector.IAddressSelector;
import org.jage.agent.AlreadyExistsException;
import org.jage.agent.IAgent;
import org.jage.communication.ICommunicationService;
import org.jage.communication.message.IMessage;
import org.jage.monitor.IQueryMonitor;
import org.jage.platform.component.annotation.Inject;
import org.jage.platform.component.definition.IComponentDefinition;
import org.jage.platform.component.exception.ComponentException;
import org.jage.platform.component.pico.IPicoComponentInstanceProvider;
import org.jage.platform.component.pico.PicoComponentInstanceProvider;
import org.jage.property.PropertyGetter;
import org.jage.property.PropertySetter;
import org.jage.query.AgentEnvironmentQuery;
import org.jage.services.core.ICoreComponentListener;
import org.jage.workplace.IStopCondition;
import org.jage.workplace.IWorkplace;
import org.jage.workplace.WorkplaceException;
import org.jage.workplace.WorkplaceManager;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;

/**
 * Basic implementation of <code>IWorkplaceManager</code>.
 * <p>
 * 
 * This implementation synchronises in two points when run in a distributed environment:
 * <ol>
 * <li>just before starting the computation,
 * <li>just before stopping the computation.
 * </ol>
 * 
 * @author AGH AgE Team
 */
public class PicoWorkplaceManager extends WorkplaceManager {

	private static Logger log = LoggerFactory.getLogger(PicoWorkplaceManager.class);

	/**
	 * <p>
	 * Attached workplaces.
	 * <p>
	 * Note: Operations on the HashMap should be synchronized by locking this object (not the field).
	 */
	private final HashMap<IAgentAddress, IWorkplace> workplaces = new HashMap<IAgentAddress, IWorkplace>();

	/**
	 * Contains running workplaces.
	 */
	private final List<IWorkplace> activeWorkplaces = new LinkedList<IWorkplace>();

	private final ReadWriteLock workplacesLock = new ReentrantReadWriteLock(true);

	private final Lock workplacesReadLock = workplacesLock.readLock();

	private final Lock workplacesWriteLock = workplacesLock.writeLock();

	private List<IWorkplace> injectedWorkplaces;

	private ICommunicationService communicationService;

	/**
	 * The list of query monitors. It is created after adding the first monitor and destroyed after removing the last
	 * one.
	 */
	private final ArrayList<IQueryMonitor> queryMonitors = new ArrayList<IQueryMonitor>();

	// BEGIN Core component methods

	private void initializeWorkplaces() throws ComponentException {
		if (injectedWorkplaces == null || injectedWorkplaces.isEmpty()) {
			throw new ComponentException("There is no workplace defined. Cannot run the computation.");
		}

		log.info("Created {} workplace(s).", injectedWorkplaces.size());

		try {
			workplacesWriteLock.lock();
			for (final IWorkplace workplace : injectedWorkplaces) {
				final IAgentAddress agentAddress = workplace.getAddress();

				// We call setWorkplaceEnvironment() relying on the fact, that all agents and workplaces were
				// initialized before the workplace manager by the container.
				try {
					workplaces.put(agentAddress, workplace);
					workplace.setWorkplaceEnvironment(this);
					log.info("Workplace {} initialized.", agentAddress.toString());
				} catch (final AlreadyExistsException e) {
					workplaces.remove(agentAddress); // Do not leave not configured workplace in the manager
					throw new ComponentException(String.format("Cannot set workplace environment for: %s.",
					        agentAddress), e);
				}
			}

		} finally {
			workplacesWriteLock.unlock();
		}

	}

	@Override
	public void start() throws ComponentException {
		// Workaround for the case when IStopCondition was configured during the start.
		instanceProvider.getInstance(IStopCondition.class);

		if (communicationService != null) {
			try {
	            communicationService.barrier(PicoWorkplaceManager.class.getName());
            } catch (InterruptedException e) {
	            log.error("Interrupted when on barrier. Aborting.", e);
	            throw new ComponentException(e);
            }
		}

		// notify all listeners
		for (ICoreComponentListener listener : listeners) {
			listener.notifyStarting(this);
		}

		// start all workplaces
		try {
			workplacesReadLock.lock();

			if (workplaces.isEmpty()) {
				throw new ComponentException("There is no workplace to run.");
			}

			for (IWorkplace workplace : workplaces.values()) {
				workplace.start();
				activeWorkplaces.add(workplace);
				log.info("Workplace {} started", workplace.getAddress());
			}
		} finally {
			workplacesReadLock.unlock();
		}
	}

	@Override
	public void stop() {
		if (communicationService != null) {
			try {
	            communicationService.barrier(PicoWorkplaceManager.class.getName());
            } catch (InterruptedException e) {
	            log.error("Interrupted when on barrier. Aborting.", e);
            }
		}

		try {
			workplacesReadLock.lock();
			for (IWorkplace workplace : this.workplaces.values()) {
				synchronized (workplace) {
					if (workplace.isRunning()) {
						workplace.stop();
					} else {
						log.warn("Trying to stop already not running workplace: {}.", workplace.getAddress());
					}
				}
			}
		} finally {
			workplacesReadLock.unlock();
		}

	}

	@Override
	public boolean finish() throws ComponentException {
		try {
			workplacesReadLock.lock();
			for (IWorkplace workplace : workplaces.values()) {
				synchronized (workplace) {
					if (!workplace.isRunning()) {
						workplace.finish();
						log.info("Workplace {} finished", workplace.getAddress());
					} else {
						log.error("Cannot finish still running workplace: {}", workplace.getAddress());
					}
				}
			}
		} finally {
			workplacesReadLock.unlock();
		}
		// manager is finished
		return true;
	}

	// END Core component methods

	// BEGIN IWorkplaceEnvironment Methods

	@Override
	public void notifyStopped(IWorkplace workplace) {

		log.info("Get stopped notification from {}", workplace.getAddress());
		if (activeWorkplaces.contains(workplace)) {
			activeWorkplaces.remove(workplace);
		} else {
			log.error("Received event notify stopped from workplace {} which is already stopped",
			        workplace.getAddress());
		}

		if (activeWorkplaces.isEmpty()) {
			// notify all listeners that the component has just stopped
			synchronized (this) {
				// we need to do list copy because during notifications,
				// listeners can be unregistered
				List<ICoreComponentListener> listenersCopy = new LinkedList<ICoreComponentListener>(listeners);
				for (ICoreComponentListener listener : listenersCopy) {
					listener.notifyStopped(this);
				}
			}
		}
	}

	// END IWorkplaceEnvironment Methods

	/**
	 * Attaches a given workplace to this manager.
	 * 
	 * @param workplace
	 *            workplace to attache
	 * @throws WorkplaceException
	 *             when a given workplace is already attached to this manager or workplace's address exists already in
	 *             the manager
	 */
	public synchronized void addWorkplace(IWorkplace workplace) throws WorkplaceException {

		try {
			workplacesWriteLock.lock();
			IAgentAddress address = workplace.getAddress();
			if (!workplaces.containsKey(address)) {
				workplaces.put(address, workplace);
				if (workplace.isRunning() && !activeWorkplaces.contains(workplace)) {
					try {
						workplace.setWorkplaceEnvironment(this);
						// workplace.setAddressRegister(this);
						activeWorkplaces.add(workplace);
					} catch (AlreadyExistsException e) {
						throw new WorkplaceException(e.getMessage(), e);
					}
				}
				log.info("Workplace added: {}", workplace.getAddress());
			} else {
				throw new WorkplaceException(String.format("IWorkplace: %s already exists in the manager.", address));
			}
		} finally {
			workplacesWriteLock.unlock();
		}
	}

	@Override
	public synchronized IWorkplace getWorkplace(IAgentAddress address) {
		try {
			workplacesReadLock.lock();
			return workplaces.get(address);
		} finally {
			workplacesReadLock.unlock();
		}
	}

	@Override
	@PropertyGetter(propertyName = "workplaces")
	public synchronized List<IWorkplace> getWorkplaces() {
		try {
			workplacesReadLock.lock();
			return new LinkedList<IWorkplace>(workplaces.values());
		} finally {
			workplacesReadLock.unlock();
		}
	}

	@PropertySetter(propertyName = "workplaces")
	public void setWorkplaces(List<IWorkplace> workplaces) {
		this.injectedWorkplaces = workplaces;
	}

	/**
	 * Indicates if the workplace manager is active, i.e. it contains at least on active workplace.
	 * 
	 * @return true if the manager is active
	 */
	public boolean isActive() {
		return !activeWorkplaces.isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <E extends IAgent, T> Collection<T> queryWorkplaces(AgentEnvironmentQuery<E, T> query) {
		return query.execute((Collection<E>)getWorkplaces());
	}

	protected List<IAgentAddress> getListOfWorkplaceAddresses() {

		List<IAgentAddress> addresses = new ArrayList<IAgentAddress>(getWorkplaces().size());

		for (IAgent workplace : getWorkplaces()) {
			addresses.add(workplace.getAddress());
		}
		return addresses;
	}

	@Override
	public void sendMessage(IMessage<IAgentAddress, ?> message) {
		log.debug("Sending message {}.", message);
		try {
			workplacesReadLock.lock();
			IAddressSelector<IAgentAddress> receiverSelector = message.getHeader().getReceiverSelector();
			receiverSelector.initialize(getListOfWorkplaceAddresses(), getListOfWorkplaceAddresses());
			for (IAgentAddress agentAddress : receiverSelector.addresses()) {
				log.debug("Delivering to {}.", agentAddress);
				getWorkplace(agentAddress).deliverMessage(message);
			}

		} finally {
			workplacesReadLock.unlock();
		}
	}

	@Override
	public void init() throws ComponentException {
		communicationService = instanceProvider.getInstance(ICommunicationService.class);
		if (communicationService == null) {
			log.debug("The communication service is not available. I'm running local, non-synchronised computation.");
		}

		// Ignore lack of injected workplaces - maybe they will be configured later.
		if (injectedWorkplaces != null) {
			initializeWorkplaces();
		}
	}

	@Override
	public void computationConfigurationUpdated(Collection<IComponentDefinition> componentDefinitions)
	        throws ComponentException {
		checkNotNull(componentDefinitions);

		if (injectedWorkplaces != null) {
			throw new ComponentException("The core component is already configured.");
		}

		final IPicoComponentInstanceProvider childInstanceProvider = ((PicoComponentInstanceProvider)instanceProvider)
		        .makeChildContainer();
		for (final IComponentDefinition def : componentDefinitions) {
			childInstanceProvider.addComponent(def);
		}
		childInstanceProvider.verify();

		injectedWorkplaces = newArrayList(childInstanceProvider.getInstances(IWorkplace.class));

		log.info("Loaded workplaces: {}.", injectedWorkplaces);

		// Initialise a stop condition
		childInstanceProvider.getInstance(IStopCondition.class);

		initializeWorkplaces();
	}
}
