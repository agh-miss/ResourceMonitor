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
 * File: AddressMap.java
 * Created: 2012-02-07
 * Author: faber
 * $Id: AddressMap.java 134 2012-03-18 20:17:01Z faber $
 */

package org.jage.communication.common.cache;

import com.google.common.collect.BoundType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ranges;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

/**
 * A simple database that maps nodes' addresses to their physical addresses.
 * <p>
 * 
 * This map also provides basic timeout-control capability. It starts a fixed-delay service that removes old entries
 * from the database and notifies cache listeners.
 * 
 * @author AGH AgE Team
 */
public class AddressMap implements IAddressMap, IStatefulComponent {

	private static final Logger log = LoggerFactory.getLogger(AddressMap.class);

	/**
	 * Private mutex for address mappings and related collections.
	 */
	// We could possibly use RWLock here but we assume that only users of the address cache are the neighbourhood
	// scanner and the communication manager.
	private final Object mappingsMutex = new Object();

	private final Map<INodeAddress, String> mappings = Maps.newHashMap();

	private final Map<INodeAddress, Long> expirations = Maps.newHashMap();

	private List<ICacheModificationListener> listeners = Collections
	        .synchronizedList(new ArrayList<ICacheModificationListener>());

	private static final long DEFAULT_TTL = 6000L; // 6s

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
	public AddressMap() {
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
	public AddressMap(long timeToLive, long cacheEvictionPeriod) {
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
				synchronized (mappingsMutex) {
					Map<INodeAddress, Long> expired = Maps.filterValues(expirations,
					        Ranges.upTo(System.currentTimeMillis(), BoundType.CLOSED));
					for (INodeAddress address : expired.keySet()) {
						log.debug("Node {} is no longer my neighbour.", address);
						mappings.remove(address);
						expirations.remove(address);
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
	public void addAddressMapping(INodeAddress nodeAddress, String physicalAddress) {
		synchronized (mappingsMutex) {
			mappings.put(nodeAddress, physicalAddress);
			expirations.put(nodeAddress, System.currentTimeMillis() + timeToLive);
		}
		informOfCacheChange();
	}

	@Override
	public void removeAddress(INodeAddress nodeAddress) {
		synchronized (mappingsMutex) {
			mappings.remove(nodeAddress);
			expirations.remove(nodeAddress);
		}
		informOfCacheChange();
	}

	@Override
	public void removeAllAddresses() {
		synchronized (mappingsMutex) {
			mappings.clear();
			expirations.clear();
		}
		informOfCacheChange();
	}

	private void informOfCacheChange() {
		synchronized (listeners) {
			for (ICacheModificationListener listener : listeners) {
				listener.onCacheChanged();
			}
		}
	}

	private static final String COMPONENT_ID = "communicationService";

	@Override
	public Collection<IComponentAddress> getAddressesOfRemoteNodes() {
		List<IComponentAddress> result = Lists.newLinkedList();

		synchronized (mappingsMutex) {
			for (INodeAddress address : mappings.keySet()) {
				result.add(new ComponentAddress(COMPONENT_ID, address));
			}
		}

		return result;
	}

	@Override
	public String getPhysicalAddressFor(INodeAddress nodeAddress) {
		synchronized (mappingsMutex) {
			return mappings.get(nodeAddress);
		}
	}

	@Override
	public void addCacheModificationListener(ICacheModificationListener listener) {
		listeners.add(listener);
	}

	@Override
	public int getNodesCount() {
		synchronized (mappingsMutex) {
			return mappings.size();
		}
	}

}
