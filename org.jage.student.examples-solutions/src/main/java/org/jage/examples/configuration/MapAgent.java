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
 * File: ArrayAgent.java
 * Created: 2011-07-11
 * Author: faber
 * $Id: MapAgent.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.examples.configuration;

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.agent.SimpleAgent;
import org.jage.platform.component.annotation.Inject;
import org.jage.platform.component.exception.ComponentException;

/**
 * This class provides a simple agent that presents how to obtain and handle maps from the instance provider.
 *
 * @author AGH AgE Team
 */
public class MapAgent extends SimpleAgent {

	private static final long serialVersionUID = 1L;

	private final Logger log = LoggerFactory.getLogger(MapAgent.class);

	@Inject
	private Map<String, Long> injectedMap1;

	@Inject
	private Map<String, ExampleClass> injectedMap2;

	/**
	 * Creates a sample ListAgent instance.
	 */
	@Inject
	public MapAgent() {
		log.info("Constructing Map Simple Agent");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init() throws ComponentException {
		super.init();
		log.info("Initializing Map Simple Agent: {}", getAddress());

		// Print the first injected map
		log.info("The first instance was injected: {}", injectedMap1);
		log.info("Values:");
		for (Entry<String, Long> element : injectedMap1.entrySet()) {
			log.info("Key: {}. Value: {}.", element.getKey(), element.getValue());
		}

		// Print the second injected map
		log.info("The second instance was injected: {}", injectedMap2);
		log.info("Values:");
		for (Entry<String, ExampleClass> element : injectedMap2.entrySet()) {
			log.info("Key: {}. Value: {}.", element.getKey(), element.getValue());
		}

		// Retrieve the "object-example" map
		Map<String, ExampleClass> objectMap = (Map<String, ExampleClass>)instanceProvider.getInstance("object-example");

		// Print it
		log.info("Obtained an instance of object-example: {}", objectMap);
		log.info("Values:");
		for (Entry<String, ExampleClass> element : objectMap.entrySet()) {
			log.info("Key: {}. Value: {}.", element.getKey(), element.getValue());
		}

		// Retrieve the "long-example" map
		Map<String, Long> longMap = (Map<String, Long>)instanceProvider.getInstance("long-example");

		// Print it
		log.info("Obtained an instance of long-example: {}", longMap);
		log.info("Values:");
		for (Entry<String, Long> element : longMap.entrySet()) {
			log.info("Key: {}. Value: {}.", element.getKey(), element.getValue());
		}
	}

	@Override
	public void step() {
		// Empty
	}

	@Override
	public boolean finish() {
		log.info("Finishing Map Agent: {}", getAddress());
		return true;
	}

}
