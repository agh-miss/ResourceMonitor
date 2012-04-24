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
 * File: Attribute.java
 * Created: 2012-02-23
 * Author: Krzywicki
 * $Id: Attribute.java 77 2012-02-29 15:56:51Z krzywick $
 */

package org.jage.platform.reflect.attribute;

/**
 * Common abstraction for reflection based injection. May represent a field or a method.
 *
 * @param <T>
 *            the type of this attribute
 *
 * @since 2.6
 * @author AGH AgE Team
 */
public interface Attribute<T> {

	/**
	 * Returns the name of this attribute.
	 *
	 * @return this attribute's name
	 * @since 2.6
	 */
	public String getName();

	/**
	 * Returns the type of this attribute.
	 *
	 * @return this attribute's type
	 * @since 2.6
	 */
	public Class<T> getType();

	/**
	 * Sets this attribute on the given target to the given value.
	 *
	 * @param target
	 *            the target to be set on
	 * @param value
	 *            the value to be set to
	 * @throws NullPointerException
	 *             if the target is null
	 * @throws IllegalArgumentException
	 *             if the value is not an instance of the attribute type or some exception happens.
	 * @throws IllegalAccessException
	 *             if the underlying attribute is inaccessible
	 * @since 2.6
	 */
	public void setValue(Object target, T value) throws IllegalAccessException;

	/**
	 * Provides a type safe generic view of this attribute. Usage:
	 *
	 * <pre>
	 * Attribute<?> someAttribute = // initialize this somehow
	 * if(String.class.equals(someAttribute.getType())){
	 *     someAttribute.asType(String.class).setValue(bean, "Hello World");
	 * }
	 * </pre>
	 *
	 * @param <S>
	 *            the target type
	 * @param targetType
	 *            the target type class
	 * @return this attribute, casted to the supplied generic type
	 * @throws IllegalArgumentException
	 *             if the supplied type is not compatible with {@link #getType()}
	 * @since 2.6
	 */
	public <S> Attribute<S> asType(final Class<S> targetType);
}
