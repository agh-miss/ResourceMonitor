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
 * File: Attributes.java
 * Created: 2012-02-23
 * Author: Krzywicki
 * $Id: Attributes.java 77 2012-02-29 15:56:51Z krzywick $
 */

package org.jage.platform.reflect.attribute;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Static factory methods for creating attributes from fields and methods.
 *
 * @since 2.6
 * @author AGH AgE Team
 */
public final class Attributes {

	/**
	 * Creates an attribute based on the given field. The attribute name and type will be inferred from this field. The
	 * field must not be final or static.
	 *
	 * @param <T>
	 *            the attribute type
	 * @param field
	 *            the field this attribute represents
	 * @return an attribute for the given field
	 * @throws IllegalArgumentException
	 *             if the field is final or static
	 * @since 2.6
	 */
	public static <T> Attribute<T> forField(final Field field) {
		return FieldAttribute.newFieldAttribute(field);
	}

	/**
	 * Creates an attribute based on the given setter. The attribute name and type will be inferred from the setter. The
	 * method must be a valid setter and not be static.
	 *
	 * @param <T>
	 *            the attribute type
	 * @param setter
	 *            the setter this attribute represents
	 * @return an attribute for the given setter
	 * @throws IllegalArgumentException
	 *             if the method is not a setter or is static
	 * @since 2.6
	 */
	public static <T> Attribute<T> forSetter(final Method setter) {
		return MethodAttribute.newSetterAttribute(setter);
	}

	private Attributes() {
	}
}
