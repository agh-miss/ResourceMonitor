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
 * File: ExtendedComponentWithConstructors.java
 * Created: 2010-03-10
 * Author: kpietak
 * $Id: ExtendedComponentWithConstructors.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.platform.component.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jage.platform.component.annotation.Inject;

/**
 *
 * @author AGH AgE Team
 */
public class ExtendedComponentWithConstructors extends BaseComponent {

	/**
	 * @see BaseComponent#SimpleAgent()
	 */
	@Inject
	public ExtendedComponentWithConstructors() {
		log.info("Constructing");
	}

	/**
	 * @see HelloWorldSimpleAgent(IAgentAddress address, Status status)
	 */
	@Inject
	public ExtendedComponentWithConstructors(String arg0) {
		log.info("Constructing");
	}

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(ExtendedComponentWithConstructors.class);
}
