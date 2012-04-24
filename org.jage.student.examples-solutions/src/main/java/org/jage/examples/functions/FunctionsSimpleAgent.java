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
package org.jage.examples.functions;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.agent.SimpleAgent;
import org.jage.platform.component.annotation.Inject;
import org.jage.property.InvalidPropertyOperationException;
import org.jage.property.InvalidPropertyPathException;
import org.jage.property.Property;
import org.jage.property.PropertyField;

public class FunctionsSimpleAgent extends SimpleAgent {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(FunctionsSimpleAgent.class);

	private static final Random RAND = new Random();

	@Inject
	@PropertyField(propertyName = "value")
	private Double value;

	@Override
	public void step() {
		try {
			Property property = this.getProperty("value");
			property.setValue(RAND.nextDouble());
		} catch (InvalidPropertyPathException e) {
			LOG.error("Can't access value property", e);
		} catch (InvalidPropertyOperationException e) {
			LOG.error("Can't set value property", e);
		}

		LOG.info("{}: value is {}", this, value);
	}
}
