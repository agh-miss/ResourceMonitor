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
 * File: ValueDefinition.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: ValueDefinition.java 172 2012-03-30 16:08:11Z krzywick $
 */

package org.jage.platform.component.definition;

import com.google.common.base.Objects;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This argument definition creates a value-type object instance, parsing its string representation.
 *
 * @author AGH AgE Team
 */
public class ValueDefinition implements IArgumentDefinition {

	private static final long serialVersionUID = -83586069588622618L;

	private final Class<?> desiredClass;

	private final String stringValue;

	/**
	 * Creates a value definition providing <code>stringValue</code> parsed to the <code>desiredClass</code> class.
	 *
	 * @param desiredClass
	 *            A class to which the provided value will be parsed.
	 * @param stringValue
	 *            A desired value as a string.
	 */
	public ValueDefinition(final Class<?> desiredClass, final String stringValue) {
		this.desiredClass = checkNotNull(desiredClass);
		this.stringValue = checkNotNull(stringValue);
	}

	public Class<?> getDesiredClass() {
		return desiredClass;
	}

	public String getStringValue() {
		return stringValue;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof ValueDefinition) {
			final ValueDefinition other = (ValueDefinition)obj;
			return Objects.equal(desiredClass, other.desiredClass)
					&& Objects.equal(stringValue, other.stringValue);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(desiredClass, stringValue);
	}

	@Override
	public String toString() {
		return toStringHelper(this)
				.add("desiredClass", desiredClass)
				.add("stringValue", stringValue)
				.toString();
	}
}
