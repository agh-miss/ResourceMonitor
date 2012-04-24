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
 * File: QueryFunctions.java
 * Created: 2011-09-12
 * Author: faber
 * $Id: QueryFunctions.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.query;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * Common implementations of {@link IQueryFunction}.
 * 
 * @author AGH AgE Team
 */
public final class QueryFunctions {
	/**
	 * Creates a function that chooses the max element using the natural ordering.
	 * 
	 * @param <T>
	 *            A type of elements in the collection.
	 * @return A new query function.
	 */
	public static <T extends Comparable<? super T>> IQueryFunction<Collection<T>> max() {
		return new IQueryFunction<Collection<T>>() {

			@Override
			public Collection<T> execute(Collection<T> result) {
				return Collections.singleton(Collections.max(result));
			}
		};
	}

	/**
	 * Creates a function that chooses the max element using the ordering defined by a provided comparator.
	 * 
	 * @param comparator
	 *            A comparator to use.
	 * @param <T>
	 *            A type of elements in the collection.
	 * @return A new query function.
	 */
	public static <T> IQueryFunction<Collection<T>> max(final Comparator<T> comparator) {
		return new IQueryFunction<Collection<T>>() {

			@Override
			public Collection<T> execute(Collection<T> result) {
				return Collections.singleton(Collections.max(result, comparator));
			}
		};
	}

	/**
	 * Creates a function that sums values in a collection. This function operates only on doubles.
	 * 
	 * @return A new query function.
	 */
	public static IQueryFunction<Collection<Double>> sum() {
		return new IQueryFunction<Collection<Double>>() {
			@Override
			public Collection<Double> execute(Collection<Double> result) {
				double summed = 0.0;
				for (double element : result) {
					summed += element;
				}
				return Collections.singleton(summed);
			}
		};
	}

	/**
	 * Creates a function that computes an average of values in a collection. This function operates only on doubles.
	 * 
	 * @return A new query function.
	 */
	public static IQueryFunction<Collection<Double>> average() {
		return new IQueryFunction<Collection<Double>>() {
			@Override
			public Collection<Double> execute(Collection<Double> result) {
				double summed = 0.0;
				for (double element : result) {
					summed += element;
				}
				return Collections.singleton(summed / result.size());
			}
		};
	}

	/**
	 * Creates a no-operation functions that simply returns received results.
	 * 
	 * @param <T>
	 *            A type of results.
	 * @return A new query function.
	 */
	public static <T> IQueryFunction<T> noOperation() {
		return new IQueryFunction<T>() {

			@Override
			public T execute(T result) {
				return result;
			}
		};
	}

	private QueryFunctions() {
		// Empty
	}
}
