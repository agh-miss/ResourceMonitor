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
 * File: AddressSet.java
 * Created: 2012-02-07
 * Author: faber
 * $Id: AddressSet.java 134 2012-03-18 20:17:01Z faber $
 */

package org.jage.communication.common.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.address.node.ComponentAddress;
import org.jage.address.node.IComponentAddress;
import org.jage.address.node.INodeAddress;
import org.jage.platform.component.IStatefulComponent;

import com.google.common.collect.BoundType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ranges;

/**
 * A simple, list based, database of node addresses.
 * <p>
 * 
 * This set also provides basic timeout-control capability. It starts a fixed-delay service that removes old entries
 * from the database and notifies cache listeners.
 * 
 * @author AGH AgE Team
 */
public class AddressSet implements IAddressSet, IStatefulComponent {

	private static final Logger log = LoggerFactory.getLogger(AddressSet.class);

	private final Map<INodeAddress, Long> addresses = Collections.synchronizedMap(new HashMap<INodeAddress, Long>());

	private List<ICacheModificationListener> listeners = Collections
	        .synchronizedList(new ArrayList<ICacheModificationListener>());

	private static final String COMPONENT_ID = "communicationService";

	private static final Long DEFAULT_TTL = 6000L; // 6s

	private static final long DEFAULT_CACHE_PERIOD = 4L;

	/**
	 * A delay (in seconds) between cache eviction phases.
	 */
	private long cacheEvictionPeriod;

	private long timeToLive;

	private ScheduledExecutorService service;

	/**
	 * Default constructor - uses default configuration values.
	 */
	public AddressSet() {
		this(DEFAULT_TTL, DEFAULT_CACHE_PERIOD);
	}

	/**
	 * Constructs a new address map instance with given configuration values.
	 * 
	 * @param timeToLive
	 *            a time to live of cache entries (in s).
	 * @param cacheEvictionPeriod
	 *            a delay between cache eviction phases (in s).
	 */
	public AddressSet(long timeToLive, long cacheEvictionPeriod) {
		super();
		this.timeToLive = timeToLive;
		this.cacheEvictionPeriod = cacheEvictionPeriod;
	}

	@Override
	public void init() {
		// Evictor
		service = Executors.newScheduledThreadPool(1);
		service.scheduleWithFixedDelay(new Runnable() {

			@Override
			public void run() {
				synchronized (addresses) {
					Map<INodeAddress, Long> expired = Maps.filterValues(addresses,
					        Ranges.upTo(System.currentTimeMillis(), BoundType.CLOSED));
					for (INodeAddress address : expired.keySet()) {
						log.debug("Node {} is no longer my neighbour.", address);
						addresses.remove(address);
					}
					if (!expired.isEmpty()) {
						informOfCacheChange();
					}
				}
			}
		}, cacheEvictionPeriod, cacheEvictionPeriod, TimeUnit.SECONDS);
	}

	@Override
	public boolean finish() {
		service.shutdown();
		try {
			service.awaitTermination(cacheEvictionPeriod, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			log.error("Interrupted during stoping the executor service.", e);
		}
		return true;
	}

	@Override
	public void addAddress(INodeAddress nodeAddress) {
		addresses.put(nodeAddress, System.currentTimeMillis() + timeToLive);
		informOfCacheChange();
	}

	@Override
	public void removeAddress(INodeAddress nodeAddress) {
		addresses.remove(nodeAddress);
		informOfCacheChange();
	}

	@Override
	public void removeAllAddresses() {
		addresses.clear();
		informOfCacheChange();
	}

	private void informOfCacheChange() {
		synchronized (listeners) {
			for (ICacheModificationListener listener : listeners) {
				listener.onCacheChanged();
			}
		}
	}

	@Override
	public Collection<IComponentAddress> getAddressesOfRemoteNodes() {
		List<IComponentAddress> result = Lists.newLinkedList();

		synchronized (addresses) {
			for (INodeAddress address : addresses.keySet()) {
				result.add(new ComponentAddress(COMPONENT_ID, address));
			}
		}

		return result;
	}

	@Override
	public void addCacheModificationListener(ICacheModificationListener listener) {
		listeners.add(listener);
	}

	@Override
	public int getNodesCount() {
		synchronized (addresses) {
			return addresses.size();
		}
	}

}
