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
 * File: MultiElementQuery.java
 * Created: 2011-09-12
 * Author: faber
 * $Id: MultiElementQuery.java 76 2012-02-27 20:55:05Z faber $
 */

package org.jage.query;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * The base class for all built-in queries that operate on Iterable elements. It provides common operations for them.
 * 
 * <p>
 * Four basic operations are defined here:
 * <ol>
 * <li>selection that arbitrarily chooses some elements from collection (e.g. ten random elements),
 * <li>filtering that filters these chosen elements on the basis of their properties,
 * <li>selection of values of properties (fields) from chosen objects,
 * <li>execution of aggregate function over the results.
 * </ol>
 * They are performed in the sequence shown above.
 * 
 * @param <E>
 *            A type of elements in the target collection.
 * @param <Q>
 *            A type of a queried object.
 * @param <R>
 *            A type of results.
 * 
 * @author AGH AgE Team
 * 
 * @see GenericQuery
 * @see IQueryAware
 */
public class MultiElementQuery<E, Q, R> implements IQuery<Q, R> {

	/**
	 * A list of initial selectors added to this query.
	 */
	protected List<IInitialSelector> initialSelectors = Lists.newArrayList();

	/**
	 * Constructed filtering (WHERE) clause.
	 */
	protected IValueFilter<? super E> valueFilter;

	/**
	 * Selectors for values (SELECT) clause.
	 */
	protected List<IValueSelector<? super E, ?>> valueSelectors = Lists.newArrayList();

	/**
	 * A list of functions to execute over results.
	 */
	protected List<IQueryFunction<? super R>> functions = Lists.newArrayList();

	/**
	 * A class of the queried object.
	 */
	protected Class<?> targetClass;

	/**
	 * A class of elements in a queried object.
	 */
	protected Class<?> elementClass;

	/**
	 * A class of results.
	 */
	protected Class<?> resultClass;

	/**
	 * Constructs a new MultiElementQuery instance. MultiElementQuery requires the target, the result and the element
	 * class being provided. It will use them for creating the result object.
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
	public MultiElementQuery(Class<?> elementClass, Class<?> targetClass, Class<?> resultClass) {
		initialSelectors.add(InitialSelectors.all());
		valueFilter = ValueFilters.any();
		functions.add(QueryFunctions.<R> noOperation());

		this.targetClass = targetClass;
		this.elementClass = elementClass;
		this.resultClass = resultClass;
	}

	@SuppressWarnings("unchecked")
	protected void addValueFilter(IValueFilter<? super E> filter) {
		valueFilter = ValueFilters.<Object> allOf(valueFilter, filter);
	}

	protected void addQueryFunction(IQueryFunction<? super R> queryFunction) {
		functions.add(queryFunction);
	}

	protected void addInitialSelector(IInitialSelector elementSelector) {
		initialSelectors.add(elementSelector);
	}

	protected <T> void addValueSelector(IValueSelector<? super E, T> valueSelector) {
		valueSelectors.add(valueSelector);
	}

	/**
	 * Converts a target to a list object.
	 * <p>
	 * This implementation requires target to be iterable - no conversion is performed.
	 * 
	 * @param target
	 *            A target to convert.
	 * @return An implementation of {@link List}.
	 */
	@SuppressWarnings("unchecked")
	protected List<E> getListFromTarget(Q target) {
		if (!(target instanceof Iterable<?>)) {
			throw new QueryException("The generic version of multi-element query requires an iterable target.");
		}
		if (target instanceof Collection) {
			return Lists.newArrayList((Collection<E>)target);
		}
		List<E> list = Lists.newArrayList();
		for (E item : (Iterable<E>)target) {
			list.add(item);
		}
		return list;
	}

	/**
	 * Converts a provided list of selected elements (raw results of the query) to the object of the type required (and
	 * specified) by the user.
	 * <p>
	 * This implementation requires a result class ({@link #resultClass}) to implement a {@link Collection} interface.
	 * 
	 * @param rawResults
	 *            Raw results to convert.
	 * @return Results packed into the proper type.
	 * 
	 * @throws QueryException
	 *             when it is not possible to create the results object.
	 */
	@SuppressWarnings("unchecked")
	protected R convertToResult(List<?> rawResults) {
		if (!Collection.class.isAssignableFrom(resultClass)) {
			throw new QueryException("The generic version of multi-element query requires a collection as a result.");
		}
		Collection<Object> result;
		try {
			result = (Collection<Object>)resultClass.newInstance();
			result.addAll(rawResults);
			return (R)result;
		} catch (InstantiationException e) {
			throw new QueryException("Cannot instantiate query results.", e);
		} catch (IllegalAccessException e) {
			throw new QueryException("Cannot instantiate query results.", e);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public R execute(Q target) {
		Q realTarget = target;
		if (target instanceof IQueryAware) {
			IQueryAware<Q, R, IQuery<Q, R>> aware = (IQueryAware<Q, R, IQuery<Q, R>>)target;
			Q tempTarget = aware.beforeExecute(this);
			if (tempTarget != null) {
				realTarget = tempTarget;
			}
		}

		List<E> selected = getListFromTarget(realTarget);

		// Initial selection - from()
		for (IInitialSelector collectionFilter : initialSelectors) {
			collectionFilter.initialise(selected.size());
			List<E> newSelected = Lists.newArrayList();
			for (E object : selected) {
				if (collectionFilter.include()) {
					newSelected.add(object);
				}
			}
			selected = newSelected;
		}

		// Value filters - matching()
		List<E> newSelected = Lists.newArrayList();
		for (E object : selected) {
			if (valueFilter.matches(object)) {
				newSelected.add(object);
			}
		}

		// Value selectors - select()
		R result = null;

		if (!valueSelectors.isEmpty()) {
			List<List<Object>> list = Lists.newArrayList();

			for (E object : newSelected) {
				List<Object> elements = Lists.newArrayList();
				list.add(elements);

				for (IValueSelector<? super E, ?> valueSelector : valueSelectors) {
					elements.add(valueSelector.selectValue(object));
				}
			}

			result = convertToResult(list);
		} else {
			result = convertToResult(newSelected);
		}

		// Functions - process()
		for (IQueryFunction<? super R> queryFunction : functions) {
			result = (R)queryFunction.execute(result);
		}

		if (target instanceof IQueryAware) {
			IQueryAware<Q, R, IQuery<Q, R>> aware = (IQueryAware<Q, R, IQuery<Q, R>>)target;
			aware.afterExecute(this);
		}

		return result;
	}

	/**
	 * Adds a selector to the query.
	 * 
	 * @param selector
	 *            A selector to add.
	 * @return This query.
	 */
	public MultiElementQuery<E, Q, R> from(IInitialSelector selector) {
		addInitialSelector(selector);
		return this;
	}

	/**
	 * Adds a selector to the query.
	 * 
	 * @param valueSelector
	 *            A selector to add.
	 * @param <T>
	 *            A type of the selected value.
	 * @return This query.
	 */
	public <T> MultiElementQuery<E, Q, R> select(IValueSelector<? super E, T> valueSelector) {
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
	public MultiElementQuery<E, Q, R> select(String... fields) {
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
	public MultiElementQuery<E, Q, R> matching(IValueFilter<? super E> filter) {
		addValueFilter(filter);
		return this;
	}

	/**
	 * Adds a value filter that operates on a specific field of the target class to the query.
	 * 
	 * @param fieldName
	 *            A field name that will be used in matching.
	 * @param filter
	 *            A value filter to add.
	 * @param <S>
	 *            A type of the field.
	 * @return This query.
	 */
	public <S> MultiElementQuery<E, Q, R> matching(String fieldName, IValueFilter<S> filter) {
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
	public MultiElementQuery<E, Q, R> process(IQueryFunction<? super R> queryFunction) {
		addQueryFunction(queryFunction);
		return this;
	}
}
