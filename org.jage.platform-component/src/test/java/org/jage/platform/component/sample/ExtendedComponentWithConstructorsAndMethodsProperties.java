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
 * File: ExtendedComponentWithConstructorsAndMethodsProperties.java
 * Created: 2010-03-10
 * Author: kpietak
 * $Id: ExtendedComponentWithConstructorsAndMethodsProperties.java 183 2012-03-31 20:33:32Z krzywick $
 */

package org.jage.platform.component.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.platform.component.annotation.Inject;

public class ExtendedComponentWithConstructorsAndMethodsProperties extends BaseComponent {

	private static final Logger log = LoggerFactory.getLogger(ExtendedComponentWithConstructorsAndMethodsProperties.class);

	private IEchoStrategy echoStrategy;

	@Inject
	public ExtendedComponentWithConstructorsAndMethodsProperties() {
		log.info("Constructing Strategy Simple Agent");
	}

	@Inject
	public ExtendedComponentWithConstructorsAndMethodsProperties(final String arg0) {
		log.info("Constructing Hello World Simple Agent");
	}

	public IEchoStrategy getEchoStr() {
		return echoStrategy;
	}

	@Inject
	public void setEchoStr(final IEchoStrategy val) {
		echoStrategy = val;
	}
}
