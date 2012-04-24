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
 * File: XMLContractHelloWorldSimpleAgent.java
 * Created: 2011-03-15
 * Author: kpietak
 * $Id: XMLContractHelloWorldSimpleAgent.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.examples.properties.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.agent.SimpleAgent;
import org.jage.platform.component.annotation.Inject;
import org.jage.platform.component.exception.ComponentException;
import org.jage.property.PropertyField;

/**
 * This agent presents a usage of components described with XML.
 * 
 * @author AGH AgE Team
 */
public class XMLContractHelloWorldSimpleAgent extends SimpleAgent {

	private static final long serialVersionUID = -7280785631107779790L;

	private final Logger log = LoggerFactory.getLogger(XMLContractHelloWorldSimpleAgent.class);

	/**
	 * This is the reference to not-ClassPropertyContainer object
	 */
	@Inject
	@PropertyField(propertyName = "exampleComponent")
	private ExampleComponent exampleComponent;

	/**
	 * Constructs a new XMLContractHelloWorldSimpleAgent agent without an address.
	 */
	@Inject
	public XMLContractHelloWorldSimpleAgent() {
		// Empty
	}

	@Override
	public void step() {
		log.info("Hello world! An example not-ClassPropertyContainer object will introduce himself: ");
		exampleComponent.printComponentInfo();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
			log.error("Interrupted", ex);
		}
	}

	@Override
	public boolean finish() {
		log.info("Finishing Hello World Simple Agent with XMLContracts.");
		return true;
	}

	@Override
	public void init() throws ComponentException {
		super.init();
		log.info("Initializing Hello World Simple Agent with XMLContracts.");
	}

}
