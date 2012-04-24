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
 * File: CollectionQuery.java
 * Created: 2011-09-12
 * Author: faber
 * $Id: CollectionQuery.java 76 2012-02-27 20:55:05Z faber $
 */

package org.jage.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Query implementation that can be used with subclasses of the {@link Collection} class. The queried object must be an
 * instance of the Collection class and the provided results also are of this type.
 * 
 * @param <E>
 *            A type of elements in the queried collection.
 * @param <T>
 *            A type of elements in the result.
 * 
 * @author AGH AgE Team
 */
public class CollectionQuery<E, T> extends MultiElementQuery<E, Collection<? extends E>, Collection<T>> {

	/**
	 * Constructs a new CollectionQuery instance. CollectionQuery requires the target, the result and the element class
	 * being provided. It will use them for creating the result object.
	 * <p>
	 * The execution of an empty query will result in the same object being returned. No operation will be performed.
	 * 
	 * @param elementClass
	 *            A class of the components in the queried composition.
	 * @param targetClass
	 *            A class of the queried object.
	 * @param resultClass
	 *            A class of the result, it must be a class that can be instantiated. If it is not, a
	 *            {@link QueryException} will be thrown during query execution.
	 */
	public CollectionQuery(Class<?> elementClass, Class<?> targetClass, Class<?> resultClass) {
		super(elementClass, targetClass, resultClass);
	}

	/**
	 * Constructs a new CollectionQuery instance. This constructor requires only the element class being provided.
	 * Collection is used as the target class and ArrayList as the result class.
	 * 
	 * @param elementClass
	 *            A class of the components in the queried composition.
	 */
	public CollectionQuery(Class<?> elementClass) {
		this(elementClass, Collection.class, ArrayList.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Collection<T> convertToResult(List<?> newSelected) {
		return new ArrayList<T>((List<T>)newSelected);
	}

	@Override
	public Collection<T> execute(Collection<? extends E> target) {
		return super.execute(target);
	}

	/**
	 * Adds a selector to the query.
	 * 
	 * @param selector
	 *            A selector to add.
	 * @return This query.
	 */
	@Override
	public CollectionQuery<E, T> from(IInitialSelector selector) {
		addInitialSelector(selector);
		return this;
	}

	/**
	 * Adds a selector to the query.
	 * 
	 * @param valueSelector
	 *            A selector to add.
	 * @param <S>
	 *            A type of the selected value.
	 * @return This query.
	 */
	@Override
	public <S> CollectionQuery<E, T> select(IValueSelector<? super E, S> valueSelector) {
		addValueSelector(valueSelector);
		return this;
	}

	/**
	 * Adds a selector to the query.
	 * 
	 * @param fields
	 *            A fields to select. Fields needs to be accessible by getters in the JavaBean naming convention.
	 * @return This query.
	 */
	@Override
	public CollectionQuery<E, T> select(String... fields) {
		for (String field : fields) {
			addValueSelector(ValueSelectors.<E, Object> field(field));
		}
		return this;
	}

	/**
	 * Adds a value filter to the query.
	 * 
	 * @param filter
	 *            A value filter to add.
	 * @return This query.
	 */
	@Override
	public CollectionQuery<E, T> matching(IValueFilter<? super E> filter) {
		addValueFilter(filter);
		return this;
	}

	/**
	 * Adds a value filter that operates on a specific field of the element class to the query.
	 * 
	 * @param fieldName
	 *            A field name that will be used in matching.
	 * @param filter
	 *            A value filter to add.
	 * @param <S>
	 *            A type of the field.
	 * @return This query.
	 */
	@Override
	public <S> CollectionQuery<E, T> matching(String fieldName, IValueFilter<S> filter) {
		addValueFilter(ValueFilters.<E, S> fieldValue(fieldName, filter));
		return this;
	}

	/**
	 * Adds a function that will process results.
	 * 
	 * @param queryFunction
	 *            A function to execute on results.
	 * @return This query.
	 */
	@Override
	public CollectionQuery<E, T> process(IQueryFunction<? super Collection<T>> queryFunction) {
		addQueryFunction(queryFunction);
		return this;
	}
}
