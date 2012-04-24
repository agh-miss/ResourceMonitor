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
 * File: IMessageMonitor.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: IMessageMonitor.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.monitor;

import org.jage.event.MessageEvent;

/**
 * The interface for monitors of events of sending/delivering messages. It enables an agent container (for instance an
 * aggregate) to inform an external object about sending/delivering a message by/to an agent.
 * 
 * @author AGH AgE Team
 */
public interface IMessageMonitor extends IMonitor {

	/**
	 * This method is executed when a message has been sent by an agent. Note: a monitor can be notified in a short time
	 * after it is unregistered.
	 * 
	 * @param event
	 *            the event of sending the message by the agent
	 */
	public void messageSent(MessageEvent event);

	/**
	 * This method is executed when a message has been delivered to an agent.
	 * 
	 * @param event
	 *            the event of delivering the message
	 */
	public void messageDelivered(MessageEvent event);
}
