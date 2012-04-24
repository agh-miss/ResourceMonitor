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
 * File: CustomCounterEchoStrategy.java
 * Created: 2011-03-15
 * Author: kpietak
 * $Id: CustomCounterEchoStrategy.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.examples.strategy;

import org.jage.platform.component.annotation.Inject;
import org.jage.property.ClassPropertyContainer;
import org.jage.property.PropertyGetter;
import org.jage.property.PropertySetter;

/**
 * An echo strategy that increments a counter of calls by a configured value.
 * 
 * @author AGH AgE Team
 */
public class CustomCounterEchoStrategy extends ClassPropertyContainer implements IEchoStrategy {

	private int count = 0;

	private int increment = 1;

	@PropertyGetter(propertyName = "increment")
	public int getIncrement() {
		return increment;
	}

	@Inject
	@PropertySetter(propertyName = "increment")
	public void setIncrement(int val) {
		increment = val;
	}

	@Override
	public void echo() {
		log.info("Counter {}", count);
		count += increment;
	}

}
