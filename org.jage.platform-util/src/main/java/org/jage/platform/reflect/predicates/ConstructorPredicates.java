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
 * File: ConstructorPredicates.java
 * Created: 2012-03-15
 * Author: Krzywicki
 * $Id: ConstructorPredicates.java 122 2012-03-18 09:41:07Z krzywick $
 */

package org.jage.platform.reflect.predicates;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.List;

import org.jage.platform.reflect.Constructors;

import com.google.common.base.Predicate;

/**
 * Utility predicates for Constructors.
 *
 * @since 2.6
 * @author AGH AgE Team
 */
public class ConstructorPredicates {

	/**
	 * Returns a predicate checking if constructors match a list of actual parameters types.
	 *
	 * @param actualParameterTypes
	 *            the list of actual parameters types
	 * @return a predicate
	 *
	 * @see Constructors#isMatchingActualParameters(Constructor, List)
	 * @since 2.6
	 */
	public static Predicate<Constructor<?>> matchingActualParameters(final List<Class<?>> actualParameterTypes) {
		return new Predicate<Constructor<?>>() {
			@Override
			public boolean apply(final Constructor<?> input) {
				return Constructors.isMatchingActualParameters(input, actualParameterTypes);
			}
		};
	}

	/**
	 * Returns a predicate checking if constructors are annotated with a given annotation.
	 *
	 * @param annotation
	 *            the annotation
	 * @return a predicate
	 *
	 * @since 2.6
	 */
	public static Predicate<Constructor<?>> withAnnotation(final Class<? extends Annotation> annotation) {
		return new Predicate<Constructor<?>>() {
			@Override
			public boolean apply(final Constructor<?> input) {
				return input.isAnnotationPresent(annotation);
			}
		};
	}
}
