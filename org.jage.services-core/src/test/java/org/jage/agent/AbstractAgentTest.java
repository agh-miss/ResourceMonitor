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
 * File: AbstractAgentTest.java
 * Created: 2011-05-24
 * Author: kpietak
 * $Id: AbstractAgentTest.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.agent;

import java.io.Serializable;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.jage.address.IAgentAddress;
import org.jage.communication.message.IHeader;
import org.jage.communication.message.Message;

/**
 * Tests for the {@link AbstractAgent} class.
 * 
 * @author AGH AgE Team
 */
public class AbstractAgentTest {

	private AbstractAgent abstractAgent;

	// BEGIN Constants and Initial Data

	private static final String MESSAGE1_CONTENT = "A";

	private static final String MESSAGE2_CONTENT = "B";

	private static final String MESSAGE3_CONTENT = "C";

	private static final Integer INTEGER_MESSAGE_CONTENT = 42;

	// END Constants and Initial Data

	@SuppressWarnings("serial")
	@Before
	public void setup() {
		abstractAgent = new AbstractAgent() {
			@Override
			public void setAgentEnvironment(IAgentEnvironment agentEnvironment) throws AlreadyExistsException {
				// Empty
			}

			@Override
			protected IAgentEnvironment getAgentEnvironment() {
				// Empty
				return null;
			}
		};
	}

	// BEGIN Messages Tests

	/**
	 * Tests a simple, untyped delivery. The only requirement is FIFO order for messages.
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void receiveMessageTest() {
		Message<IAgentAddress, String> message1 = new Message<IAgentAddress, String>(mock(IHeader.class),
		        MESSAGE1_CONTENT);
		Message<IAgentAddress, String> message2 = new Message<IAgentAddress, String>(mock(IHeader.class),
		        MESSAGE2_CONTENT);
		Message<IAgentAddress, String> message3 = new Message<IAgentAddress, String>(mock(IHeader.class),
		        MESSAGE3_CONTENT);

		abstractAgent.deliverMessage(message1);
		abstractAgent.deliverMessage(message2);
		abstractAgent.deliverMessage(message3);

		assertEquals(message1, abstractAgent.receiveMessage());
		assertEquals(message2, abstractAgent.receiveMessage());
		assertEquals(message3, abstractAgent.receiveMessage());
		assertNull(abstractAgent.receiveMessage());
	}

	/**
	 * Tests a typed delivery. A message cannot be received if some specific type was requested.
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void receiveTypedMessageTest() {
		Message<IAgentAddress, String> message1 = new Message<IAgentAddress, String>(mock(IHeader.class),
		        MESSAGE1_CONTENT);
		Message<IAgentAddress, Integer> message2 = new Message<IAgentAddress, Integer>(mock(IHeader.class),
		        INTEGER_MESSAGE_CONTENT);

		abstractAgent.deliverMessage(message1);
		abstractAgent.deliverMessage(message2);

		assertNull(abstractAgent.receiveMessage(Integer.class));
		assertEquals(message1, abstractAgent.receiveMessage(String.class));
		assertEquals(message2, abstractAgent.receiveMessage(Serializable.class));
		assertNull(abstractAgent.receiveMessage());
	}

	// END Messages Tests

}
