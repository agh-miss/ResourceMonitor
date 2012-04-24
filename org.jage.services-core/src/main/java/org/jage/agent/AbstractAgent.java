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
 * File: AbstractAgent.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: AbstractAgent.java 213 2012-04-08 20:21:35Z krzywick $
 */

package org.jage.agent;

import java.io.Serializable;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.jage.address.IAgentAddress;
import org.jage.address.provider.IAgentAddressProvider;
import org.jage.communication.message.IMessage;
import org.jage.platform.component.exception.ComponentException;
import org.jage.platform.component.provider.IComponentInstanceProvider;
import org.jage.platform.component.provider.IComponentInstanceProviderAware;
import org.jage.property.ClassPropertyContainer;
import org.jage.property.IPropertyContainer;
import org.jage.property.InvalidPropertyPathException;
import org.jage.property.Property;
import org.jage.property.PropertyField;
import org.jage.property.PropertyGetter;
import org.jage.query.AgentEnvironmentQuery;
import org.jage.workplace.IllegalOperationException;

/**
 * Abstract agent implementation.
 *
 * @author AGH AgE Team
 */
public abstract class AbstractAgent extends ClassPropertyContainer implements IAgent, IComponentInstanceProviderAware {



	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2L;

	protected IComponentInstanceProvider instanceProvider;

	/**
	 * AbstractAgent properties.
	 *
	 * @author AGH AgE Team
	 */
	public static class Properties {
		/**
		 * Agent address initialization value property.
		 */
		public static final String NAME_INITIALIZER = "nameInitializer";

		/**
		 * Agent address property.
		 */
		public static final String ADDRESS = "address";
	}

	/**
	 * Address of the agent.
	 */
	protected IAgentAddress address;

	/**
	 * Initialization value for the agent address.
	 */
	@PropertyField(propertyName = Properties.NAME_INITIALIZER)
	protected String nameInitializer;

    public void setNameInitializer(final String nameInitializer) {
	    this.nameInitializer = nameInitializer;
    }

	/**
	 * The set of messages to this agent. These messages are waiting for being received.
	 */
	private final Queue<IMessage<IAgentAddress, ?>> messages = new LinkedBlockingQueue<IMessage<IAgentAddress, ?>>();

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation adds a message to the agent's private message queue.
	 *
	 * @see IAgent#deliverMessage(IMessage)
	 */
	@Override
	public void deliverMessage(final IMessage<IAgentAddress, ?> message) {
		messages.add(message);
	}

	/**
	 * Receives a first message and removes it from the queue.
	 *
	 * @return A previously delivered message or <code>null</code> if there is no message to receive.
	 */
	@SuppressWarnings("unchecked")
	protected final <T extends Serializable> IMessage<IAgentAddress, T> receiveMessage() {
		IMessage<IAgentAddress, T> msg = null;
		if (messages.size() > 0) {
			msg = (IMessage<IAgentAddress, T>)messages.remove();
		}
		return msg;
	}

	/**
	 * Receives a first message and removes it from the queue.
	 * <p>
	 * This method performs type-checking of payload. If the first message does not have a payload of the required type,
	 * <code>null</code> is returned.
	 *
	 * @param klass
	 *            A required type of the payload.
	 * @param <T>
	 *            A type of the payload.
	 *
	 * @return A previously delivered message or <code>null</code> if there is no message to receive or the payload has
	 *         a wrong type.
	 */
	@SuppressWarnings("unchecked")
	protected final <T extends Serializable> IMessage<IAgentAddress, T> receiveMessage(final Class<T> klass) {
		IMessage<IAgentAddress, T> msg = null;
		if (messages.size() > 0) {
			if (klass.isAssignableFrom(messages.peek().getPayload().getClass())) {
				msg = (IMessage<IAgentAddress, T>)messages.remove();
			}
		}
		return msg;
	}

	/**
	 * Constructs an agent without any address and with the status: Status.STOPPED.
	 */
	public AbstractAgent() {
		// Empty
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation obtains a {@link IAgentAddressProvider} from the container and tries to initialise its own
	 * address.
	 */
	@Override
	public void init() throws ComponentException {
		final IAgentAddressProvider addressProvider = instanceProvider.getInstance(IAgentAddressProvider.class);
		if (addressProvider == null) {
			throw new ComponentException("No implementation of IAgentAddressProvider available.");
		}
		address = addressProvider.obtainAddress(nameInitializer);
	}

	@Override
	public boolean finish() throws ComponentException {
		return true;
	}

	@Override
	@PropertyGetter(propertyName = Properties.ADDRESS)
	public IAgentAddress getAddress() {
		return address;
	}

	/**
	 * Sets an agent address.
	 * <p>
	 * An address is a read-only property of the agent and should not change during the platform life. This is a reason
	 * behind this method being <em>protected</em>.
	 *
	 * @param address
	 *            An agent address.
	 */
	protected void setAddress(final IAgentAddress address) {
		this.address = address;
	}

	@Override
	public void setInstanceProvider(final IComponentInstanceProvider provider) {
		instanceProvider = provider;
	}

	/**
	 * Provides local environment. An agent don't have to be in an environment, because it is root agent or the
	 * environment is not yet set.
	 *
	 * @return instance of environment or null
	 */
	protected abstract IAgentEnvironment getAgentEnvironment();

	/**
	 * Queries the local environment of this agent.
	 * <p>
	 * This is just an utility method that basically calls: <code>
	 * query.execute(agentEnvironment)
	 * </code> after verifying parameters.
	 *
	 * @param query
	 *            The query to perform on the agent's local environment.
	 *
	 * @return A result of the query.
	 *
	 * @throws AgentException
	 *             When environment is not available.
	 */
	protected <E extends IAgent, T> Collection<T> queryEnvironment(final AgentEnvironmentQuery<E, T> query)
	        throws AgentException {
		final IAgentEnvironment agentEnvironment = getAgentEnvironment();
		if (agentEnvironment == null) {
			throw new AgentException("Agent environment is not available");
		}
		return query.execute(agentEnvironment);
	}

	/**
	 * Queries the local environment of a parent of this agent.
	 *
	 * @param query
	 *            The query to perform on the agent's parent's local environment.
	 *
	 * @return A result of the query.
	 *
	 * @throws AgentException
	 *             When environment is not available.
	 *
	 * @see IAgentEnvironment#queryParent(AgentEnvironmentQuery)
	 */
	protected <E extends IAgent, T> Collection<T> queryParentEnvironment(final AgentEnvironmentQuery<E, T> query)
	        throws AgentException {
		final IAgentEnvironment agentEnvironment = getAgentEnvironment();
		if (agentEnvironment == null) {
			throw new AgentException("Agent environment is not available");
		}
		return agentEnvironment.queryParent(query);
	}

	/**
	 * Checks if this agent has a local environment.
	 *
	 * @return <TT>true</TT> if this agent has a local environment; otherwise - <TT>false</TT>
	 */
	protected boolean hasAgentEnvironment() {
		return getAgentEnvironment() != null;
	}

	/**
	 * Returns the address of the parent of this agent.
	 *
	 * @return the address of the parent of this agent or null if parent is not available.
	 */
	protected final IAgentAddress getParentAddress() {
		final IAgentEnvironment agentEnvironment = getAgentEnvironment();
		if (agentEnvironment != null) {
			return agentEnvironment.getAddress();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This method is overridden to implement virtual properties. If agent can't find property with given path it ask
	 * about the property it's parent (through agent environment).
	 */
	@Override
	public Property getProperty(final String name) throws InvalidPropertyPathException {
		Property result = null;
		try {
			result = super.getProperty(name);
		} catch (final InvalidPropertyPathException e) {
			try {
				if (getAgentEnvironment() instanceof IPropertyContainer) {
					result = ((IPropertyContainer)getAgentEnvironment()).getProperty(name);
				}
			} catch (final InvalidPropertyPathException e2) {
				throw e;
			} catch (final IllegalOperationException e2) {
				throw e;
			}
		}
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final AbstractAgent other = (AbstractAgent)obj;
		if (address == null) {
			if (other.address != null) {
				return false;
			}
		} else if (!address.equals(other.address)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return address != null ? address.toString() : "unknown";
	}
}
