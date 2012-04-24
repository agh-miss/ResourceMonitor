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
 * File: MethodAttribute.java
 * Created: 2012-02-23
 * Author: Krzywicki
 * $Id: MethodAttribute.java 77 2012-02-29 15:56:51Z krzywick $
 */

package org.jage.platform.reflect.attribute;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static java.lang.reflect.Modifier.isStatic;

import static org.jage.platform.reflect.Methods.getSetterName;
import static org.jage.platform.reflect.Methods.getSetterType;
import static org.jage.platform.reflect.Methods.isSetter;

import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Attribute implementation which injects values into an instance method.
 * <p>
 * Is effectively immutable if the underlying method is not modified.
 *
 * @param <T>
 *            the type of this attribute
 * @author AGH AgE Team
 */
final class MethodAttribute<T> extends AbstractAttribute<T> {

	private final Method method;

	MethodAttribute(final String name, final Class<T> type, final Method method) {
		super(name, type);
		this.method = checkNotNull(method);
	}

	/**
	 * Creates an attribute based on the given setter. The attribute name and type will be inferred from the setter. The
	 * method must be a valid setter and not be static.
	 *
	 * @param setter
	 *            the setter this attribute represents
	 * @throws IllegalArgumentException
	 *             if the method is not a setter or is static
	 */
	@SuppressWarnings("unchecked")
	static <T> Attribute<T> newSetterAttribute(final Method setter) {
		checkNotNull(setter);
		checkArgument(isSetter(setter), "The given method is not a setter");
		checkArgument(!isStatic(setter.getModifiers()), "The given method is static");

		return new MethodAttribute<T>(getSetterName(setter), (Class<T>)getSetterType(setter), setter);
	}

	@Override
	public void setValue(final Object target, final T value) throws IllegalAccessException {
		try {
			method.setAccessible(true);
			// All other exceptions declared in Attribute will be handled by the call
			method.invoke(target, value);
		} catch (final InvocationTargetException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public boolean equals(final Object obj) {
		if(this == obj) {
			return true;
		}
		if(!(obj instanceof MethodAttribute)) {
			return false;
		}

		final MethodAttribute<?> other = (MethodAttribute<?>)obj;

		// name and type are inferred from the setter, so doesn't need to be checked
		return Objects.equal(this.method, other.method);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(method, getName(), getType());
	}
}
