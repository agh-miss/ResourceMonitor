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
 * File: HazelcastCommonProtocol.java
 * Created: 2012-03-15
 * Author: faber
 * $Id: HazelcastCommonProtocol.java 139 2012-03-19 22:54:18Z faber $
 */

package org.jage.communication.hazelcast.protocol;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.address.node.INodeAddress;
import org.jage.address.node.provider.IAgENodeAddressProvider;
import org.jage.communication.CommunicationException;
import org.jage.communication.common.protocol.ICommunicationProtocol;
import org.jage.communication.common.protocol.IMessageReceivedListener;
import org.jage.platform.component.annotation.Inject;

import com.google.common.collect.Lists;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICountDownLatch;
import com.hazelcast.core.InstanceDestroyedException;
import com.hazelcast.core.MemberLeftException;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Common parts of Hazelcast-based communication protocols.
 *
 * @author AGH AgE Team
 */
public abstract class HazelcastCommonProtocol implements ICommunicationProtocol {

	private static final Logger log = LoggerFactory.getLogger(HazelcastCommonProtocol.class);

	@Inject
	protected IAgENodeAddressProvider addressProvider;

	protected INodeAddress localAddress;

	protected HazelcastInstance hazelcastInstance;

	protected final List<IMessageReceivedListener> listeners = Lists.newLinkedList();

	/**
	 * Creates a new Hazelcast-based protocol.
	 */
	public HazelcastCommonProtocol() {
		this(null);
	}

	/**
	 * Package-visible constructor for mocking.
	 *
	 * @param hazelcastInstance
	 *            a Hazelcast instance to use.
	 */
	HazelcastCommonProtocol(final HazelcastInstance hazelcastInstance) {
		if(hazelcastInstance == null) {
			System.setProperty("hazelcast.logging.type", "slf4j");
			this.hazelcastInstance = Hazelcast.newHazelcastInstance(null);
		} else {
			this.hazelcastInstance = hazelcastInstance;
		}
	}

	/**
	 * Initializes a Hazelcast instance. It also gets the local AgE address from the provider.
	 * <p>
	 *
	 * {@inheritDoc}
	 *
	 * @see org.jage.communication.common.protocol.ICommunicationProtocol#init()
	 */
	@Override
	public void init() {
		log.info("Initializing the Hazelcast-based protocol.");
		localAddress = addressProvider.getNodeAddress();
	}

	/**
	 * Shutdowns the Hazelcast instance.
	 * <p>
	 *
	 * {@inheritDoc}
	 *
	 * @see org.jage.communication.common.protocol.ICommunicationProtocol#finish()
	 */
	@Override
	public boolean finish() {
		log.info("Finalizing the Hazelcast-based protocol.");

		if (hazelcastInstance.getLifecycleService().isRunning()) {
			hazelcastInstance.getLifecycleService().shutdown();
		}

		log.info("Done with finalization of the Hazelcast-based protocol.");

		return true;
	}

	@Override
	public void addMessageReceivedListener(final IMessageReceivedListener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 *
	 * Note: this implementation may cause a deadlock, if not all nodes reach the barrier during their lifetime.
	 * <p>
	 *
	 * This implementation is based on the count provided by the external manager. If a participating node shutdowns
	 * before reaching the barrier all remaining nodes will be deadlocked.
	 * 
	 * @throws InterruptedException when the waiting thread was interrupted. 
	 */
	@Override
	public void barrier(final String key, final int count) throws InterruptedException {
		checkNotNull(key);
		checkArgument(count > 0, "Count must be greater than 0 work a barrier to work.");

		log.debug("Trying to enter the barrier {} with the count {}.", key, count);

		final ICountDownLatch countDownLatch = hazelcastInstance.getCountDownLatch(key);
		if (countDownLatch.setCount(count)) {
			log.debug("I have created the barrier.");
		}
		countDownLatch.countDown();
		try {
			countDownLatch.await();
			log.debug("Leaving the barrier {} succesfully.", key);

		} catch (final MemberLeftException e) {
			log.error("Exception during the latch await.", e);
			throw new CommunicationException(e);
		} catch (final InstanceDestroyedException e) {
			log.error("Exception during the latch await.", e);
			throw new CommunicationException(e);
		} catch (final InterruptedException e) {
			log.info("Interrupted during the latch await. Destroying the barrier.", e);
			countDownLatch.destroy();
			Thread.currentThread().interrupt();
			throw e;
		} catch (final IllegalStateException e) {
			log.error("Exception during the latch await.", e);
			throw new CommunicationException(e);
		}
		// XXX: the deadlocking is at least partially solvable, but we need a consistent behaviour defined for
		// all environment changes.
		// Moreover, we need to think who should care about this - a communication protocol or the polling manager?
	}
}
