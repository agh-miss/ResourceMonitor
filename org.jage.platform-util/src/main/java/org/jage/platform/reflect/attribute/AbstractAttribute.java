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
 * File: AbstractAttribute.java
 * Created: 2012-02-23
 * Author: Krzywicki
 * $Id: AbstractAttribute.java 77 2012-02-29 15:56:51Z krzywick $
 */

package org.jage.platform.reflect.attribute;

import static org.jage.platform.reflect.Classes.isAssignable;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Abstract implementation of Attribute. Subclasses need only implement the actual injection mechanism.
 *
 * @param <T>
 *            the type of this attribute
 * @author AGH AgE Team
 */
abstract class AbstractAttribute<T> implements Attribute<T> {

	private final String name;

	private final Class<T> type;

	/**
	 * Creates an abstract attribute with the given name and type.
	 *
	 * @param name
	 *            the attribute's name
	 * @param type
	 *            the attribute's type
	 */
	protected AbstractAttribute(final String name, final Class<T> type) {
		this.name = checkNotNull(name);
		this.type = checkNotNull(type);
	}

	@Override
	public final String getName() {
		return name;
	}

	@Override
	public final Class<T> getType() {
		return type;
	}

	@Override
	@SuppressWarnings("unchecked" /* checked at runtime */)
	public final <S> Attribute<S> asType(final Class<S> targetType) {
		checkArgument(isAssignable(targetType, getType()), "Attribute %s is not compatible with return type %s",
		        getName(), getType());
		return (Attribute<S>)this;
	}
}
