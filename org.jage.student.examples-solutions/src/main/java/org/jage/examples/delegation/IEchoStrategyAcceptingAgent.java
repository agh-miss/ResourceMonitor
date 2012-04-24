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
 * File: IEchoStrategyAcceptingAgent.java
 * Created: 2011-04-07
 * Author: faber
 * $Id: IEchoStrategyAcceptingAgent.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.examples.delegation;

/**
 * Interface for agents that want to accept IEchoStrategy implementations.
 * 
 * @author AGH AgE Team
 */
public interface IEchoStrategyAcceptingAgent {

	/**
	 * Accepts an <em>echo strategy</em> with the provided name.
	 * 
	 * @param echoStrategyName A name of the strategy to use (a key in the configuration).
	 */
	public void acceptEchoStrategy(String echoStrategyName);
}
