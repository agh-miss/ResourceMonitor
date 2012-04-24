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
 * File: MessagesTest.java
 * Created: 2012-03-04
 * Author: faber
 * $Id: MessagesTest.java 134 2012-03-18 20:17:01Z faber $
 */

package org.jage.communication.message;

import java.io.Serializable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.jage.address.IAddress;
import org.jage.address.node.IComponentAddress;
import org.jage.address.selector.UnicastSelector;

/**
 * Tests for the {@link Messages} class.
 * 
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class MessagesTest {

	@Mock
	private IComponentAddress senderAddress;
	
	@Test
	public void testGetOnlyReceiverAddressWithOneReceiver() {
		// given
		IAddress receiverAddress = mock(IComponentAddress.class);
		UnicastSelector<IAddress> selector = new UnicastSelector<IAddress>(receiverAddress);
		Header<IAddress> header = new Header<IAddress>(senderAddress, selector);
		Message<IAddress, Serializable> message = new Message<IAddress, Serializable>(header, mock(Serializable.class));
		
		// when
		IAddress onlyReceiverAddress = Messages.getOnlyReceiverAddress(message);
		
		// then
		assertThat(onlyReceiverAddress, is(equalTo(receiverAddress)));
	}

	@SuppressWarnings("unchecked")
    @Test
	public void testGetPayloadOfTypeOrThrowWithCorrectType() {
		// given
		String samplePayload = "payload"; 
		Message<IAddress, String> message = new Message<IAddress, String>(mock(IHeader.class), samplePayload);
		
		// when
		String returnedPayload = Messages.getPayloadOfTypeOrThrow(message, String.class);
		
		// then
		assertThat(returnedPayload, is(equalTo(samplePayload)));
	}
	
	@SuppressWarnings("unchecked")
    @Test(expected=IllegalArgumentException.class)
	public void testGetPayloadOfTypeOrThrowWithWrongType() {
		// given
		String samplePayload = "payload"; 
		Message<IAddress, Serializable> message = new Message<IAddress, Serializable>(mock(IHeader.class), samplePayload);
		
		// when
		Messages.getPayloadOfTypeOrThrow(message, Integer.class);
		
		// then should throw
	}

}
