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
 * File: VirtualPropertyTest.java
 * Created: 2009-05-20
 * Author: kpietak
 * $Id: VirtualPropertyTest.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.agent.property;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.jage.agent.SimpleAgent;
import org.jage.agent.SimpleAggregate;
import org.jage.agent.testHelpers.HelperSimpleAgent;
import org.jage.agent.testHelpers.HelperSimpleWorkplace;
import org.jage.property.InvalidPropertyPathException;
import org.jage.property.Property;
import org.jage.workplace.SimpleWorkplace;

/**
 * Tests for virtual properties.
 *
 * @author AGH AgE Team
 */
public class VirtualPropertyTest {

	private static final String VIRTUAL_PROPERTY_NAME = "step";

	private SimpleWorkplace workplace;

	private SimpleAggregate aggregate;

	private SimpleAgent agent;

	@Before
	public void setUp() throws Exception {
		workplace = new HelperSimpleWorkplace();

		aggregate = new SimpleAggregate();
		aggregate.setAgentEnvironment(workplace);

		agent = new HelperSimpleAgent();
		agent.setAgentEnvironment(aggregate);
	}

	@Test
	public void testGettingVirtualProperty() throws Exception {
		Property property = agent.getProperty(VIRTUAL_PROPERTY_NAME);
		assertNotNull(property);
		assertEquals(VIRTUAL_PROPERTY_NAME, property.getMetaProperty().getName());
		assertEquals(0L, property.getValue());
	}

	@Test
	public void testInvalidProperty() {
		try {
			agent.getProperty("invalidName");
			fail();
		} catch (InvalidPropertyPathException e) {
			// passed
		}
	}

}
