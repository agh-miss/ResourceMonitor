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
 * $Id: ListAgent.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.examples.configuration;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.agent.SimpleAgent;
import org.jage.platform.component.annotation.Inject;
import org.jage.platform.component.exception.ComponentException;

/**
 * This class provides a simple agent that presents how to obtain and handle lists from the instance provider.
 *
 * @author AGH AgE Team
 */
public class ListAgent extends SimpleAgent {

	private static final long serialVersionUID = 1L;

	private final Logger log = LoggerFactory.getLogger(ListAgent.class);

	@Inject
	private List<Long> injectedList1;

	@Inject
	private List<ExampleClass> injectedList2;

	/**
	 * Creates a sample ListAgent instance.
	 */
	@Inject
	public ListAgent() {
		log.info("Constructing List Simple Agent");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init() throws ComponentException {
		super.init();
		log.info("Initializing List Simple Agent: {}", getAddress());

		// Print the first injected list
		log.info("The first instance was injected: {}", injectedList1);
		log.info("Values:");
		for (long element : injectedList1) {
			log.info("Instance: {}", element);
		}

		// Print the second injected list
		log.info("The second instance was injected: {}", injectedList2);
		log.info("Values:");
		for (ExampleClass element : injectedList2) {
			log.info("Instance: {}", element);
		}

		// Retrieve the "object-example" list
		List<ExampleClass> list = (List<ExampleClass>)instanceProvider.getInstance("object-example");

		// Print it
		log.info("Obtained an instance of object-example: {}", list);
		log.info("Values:");
		for (ExampleClass element : list) {
			log.info("Instance: {}", element);
		}

		// Retrieve the "long-example" list
		List<Long> longList = (List<Long>)instanceProvider.getInstance("long-example");

		// Print it
		log.info("Obtained an instance of long-example: {}", longList);
		log.info("Values:");
		for (long element : longList) {
			log.info("Instance: {}", element);
		}
	}

	@Override
	public void step() {
		// Empty
	}

	@Override
	public boolean finish() {
		log.info("Finishing List Agent: {}", getAddress());
		return true;
	}

}
