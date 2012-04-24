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
 * File: Constructors.java
 * Created: 2012-03-02
 * Author: Krzywicki
 * $Id: Constructors.java 122 2012-03-18 09:41:07Z krzywick $
 */

package org.jage.platform.reflect;

import java.lang.reflect.Constructor;
import java.util.List;

import static java.util.Arrays.asList;

import static org.jage.platform.reflect.Classes.isAssignable;

/**
 * Constructor related reflection utilities.
 *
 * @since 2.6
 * @author AGH AgE Team
 */
public final class Constructors {

	/**
	 * Checks whether the given actual parameters types match the formal parameters types of the given constructor. That
	 * is, if the iterable of actual parameters types is assignable to the iterable of actual parameters types, as of
	 * {@link Classes#isAssignable(Iterable, Iterable)}.
	 *
	 * @param constructor
	 *            the given constructor
	 * @param actualParameterTypes
	 *            the actual parameters types.
	 * @return true if the actual parameters match the formal ones
	 *
	 * @see Classes#isAssignable(Iterable, Iterable)
	 * @since 2.6
	 */
	public static boolean isMatchingActualParameters(final Constructor<?> constructor,
	        final List<Class<?>> actualParameterTypes) {
		final List<Class<?>> formalParameterTypes = asList(constructor.getParameterTypes());
		return isAssignable(actualParameterTypes, formalParameterTypes);
	}

	private Constructors() {
	}
}
