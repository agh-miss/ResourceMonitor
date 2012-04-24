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
 * File: ITransferMonitor.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: ITransferMonitor.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.monitor;

import org.jage.address.IAgentAddress;
import org.jage.event.AbstractTransferEvent;

/**
 * The interface for monitors of events which involve transferring objects between objects. It allows to listen for
 * events about sending/receiving an object.
 * 
 * @param <A>
 *            an agent address
 * @param <EV>
 *            abstract transfer
 * @author AGH AgE Team
 */
public interface ITransferMonitor<A extends IAgentAddress, EV extends AbstractTransferEvent<A>> extends IMonitor {
	/**
	 * This method is executed when an object has been sent to a remote object.
	 * 
	 * @param event
	 *            detailed information about the event
	 */
	public void objectSent(EV event);

	/**
	 * This method is executed when an object has been received from a remote object.
	 * 
	 * @param event
	 *            detailed information about the event
	 */
	public void objectReceived(EV event);

	/**
	 * This method is executed when a process of sending an object to a remote object has failed.
	 * 
	 * @param event
	 *            detailed information about the event
	 */
	public void objectSendingFailed(EV event);
}
