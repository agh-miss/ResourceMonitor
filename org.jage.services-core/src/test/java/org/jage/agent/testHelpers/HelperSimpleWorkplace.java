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
 * File: HelperSimpleWorkplace.java
 * Created: 2009-05-20
 * Author: kpietak
 * $Id: HelperSimpleWorkplace.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.agent.testHelpers;

import java.util.UUID;

import static org.mockito.Mockito.mock;

import org.jage.address.AgentAddress;
import org.jage.address.node.INodeAddress;
import org.jage.workplace.SimpleWorkplace;

/**
 * A sample implementation of a workplace that does nothing.
 *
 * @author AGH AgE Team
 */
public class HelperSimpleWorkplace extends SimpleWorkplace {

	private static final long serialVersionUID = 1L;

	public HelperSimpleWorkplace() {
		setAddress(new AgentAddress(UUID.randomUUID(), mock(INodeAddress.class), null));
	}
}
