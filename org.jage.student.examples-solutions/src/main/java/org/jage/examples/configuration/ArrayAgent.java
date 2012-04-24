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
 * $Id: ArrayAgent.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.examples.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.agent.SimpleAgent;
import org.jage.platform.component.annotation.Inject;

/**
 * This class provides a simple agent that presents how to obtain and handle arrays from the instance provider.
 * 
 * @author AGH AgE Team
 */
public class ArrayAgent extends SimpleAgent {

	private static final long serialVersionUID = 1L;

	private final Logger log = LoggerFactory.getLogger(ArrayAgent.class);

	/**
	 * Creates a sample ArrayAgent instance.
	 */
	@Inject
	public ArrayAgent() {
		log.info("Constructing Hello World Simple Agent");
	}

	@Override
	public void init() {
		log.info("Initializing Hello World Simple Agent: {}", getAddress());

		ExampleClass[] array = (ExampleClass[])instanceProvider.getInstance("object-example");

		log.info("Obtained an instance of object-example: {}", array);
		log.info("Values:");
		for (ExampleClass element : array) {
			log.info("Instance: {}. Value: {}", element, element.getArgument());
		}

		Long[] longArray = (Long[])instanceProvider.getInstance("long-example");

		log.info("Obtained an instance of long-example: {}", longArray);
		log.info("Values:");
		for (long element : longArray) {
			log.info("Instance: {}", element);
		}
	}

	@Override
	public void step() {
		// Do nothing
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation does nothing.
	 * 
	 * @see org.jage.agent.AbstractAgent#finish()
	 */
	@Override
	public boolean finish() {
		log.info("Finishing Array Agent: {}", getAddress());
		return true;
	}

}
