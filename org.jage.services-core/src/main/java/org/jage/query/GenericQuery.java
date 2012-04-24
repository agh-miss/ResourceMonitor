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
 * File: GenericQuery.java
 * Created: 2011-09-12
 * Author: faber
 * $Id: GenericQuery.java 76 2012-02-27 20:55:05Z faber $
 */

package org.jage.query;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * The base class for all built-in queries used in the component introspection. It provides common operations for them.
 * 
 * <p>
 * Three basic operations are defined here:
 * <ol>
 * <li>matching a component on the basis of its properties,
 * <li>selection of values of properties from the component,
 * <li>execution of aggregate function over the results.
 * </ol>
 * They are performed in the sequence shown above.
 * 
 * <p>
 * Note: this query does not support operations required to work with collections. For more general mechanism check the
 * {@link MultiElementQuery} class.
 * 
 * <p>
 * Additionally, this query supports targets that implements an {@link IQueryAware} interface.
 * 
 * @param <Q>
 *            A type of a queried object.
 * @param <R>
 *            A type of results.
 * 
 * @author AGH AgE Team
 * 
 * @see MultiElementQuery
 * @see IQueryAware
 */
public class GenericQuery<Q, R> implements IQuery<Q, R> {

	/**
	 * Constructed filtering (WHERE) clause.
	 */
	protected IValueFilter<Q> valueFilter;

	/**
	 * Selectors for values (SELECT) clause.
	 */
	protected List<IValueSelector<Q, ?>> valueSelectors = Lists.newArrayList();

	/**
	 * A list of functions to execute over results.
	 */
	protected List<IQueryFunction<R>> functions = Lists.newArrayList();

	/**
	 * A class of the queried object.
	 */
	protected Class<?> targetClass;

	/**
	 * A class of results.
	 */
	protected Class<?> resultClass;

	/**
	 * Constructs a new GenericQuery instance. GenericQuery requires both the target and result class being provided. It
	 * will use them for creating the result object.
	 * <p>
	 * The execution of an empty query will result in the same object being returned. No operation will be performed.
	 * 
	 * @param targetClass
	 *            A class of the queried object.
	 * @param resultClass
	 *            A class of the result.
	 */
	public GenericQuery(Class<?> targetClass, Class<?> resultClass) {
		valueFilter = ValueFilters.any();
		functions.add(QueryFunctions.<R> noOperation());

		this.targetClass = targetClass;
		this.resultClass = resultClass;
	}

	@SuppressWarnings("unchecked")
	protected void addValueFilter(IValueFilter<Q> filter) {
		valueFilter = ValueFilters.allOf(valueFilter, filter);
	}

	protected void addQueryFunction(IQueryFunction<R> queryFunction) {
		functions.add(queryFunction);
	}

	protected <T> void addValueSelector(IValueSelector<Q, T> valueSelector) {
		valueSelectors.add(valueSelector);
	}

	@SuppressWarnings({ "cast", "unchecked" })
	@Override
	public R execute(final Q target) {
		Q realTarget = target;
		if (target instanceof IQueryAware) {
			IQueryAware<Q, R, IQuery<Q, R>> aware = (IQueryAware<Q, R, IQuery<Q, R>>)target;
			Q tempTarget = aware.beforeExecute(this);
			if (tempTarget != null) {
				realTarget = tempTarget;
			}
		}

		// Value filters - matching()
		if (!valueFilter.matches(realTarget)) {
			return null;
		}

		// Value selectors - select()
		R results = null;

		if (!valueSelectors.isEmpty()) {

			List<Object> list = Lists.newArrayList();

			for (IValueSelector<Q, ?> valueSelector : valueSelectors) {
				list.add((Object)valueSelector.selectValue(realTarget));
			}

			if (list.size() == 1 && !Collection.class.isAssignableFrom(resultClass)) {
				results = (R)list.get(0);
			} else if (resultClass.isAssignableFrom(list.getClass())) {
				results = (R)list;
			}
		} else if (resultClass.isAssignableFrom(targetClass)) {
			results = (R)resultClass.cast(realTarget);
		}

		// Functions - process()
		for (IQueryFunction<R> queryFunction : functions) {
			results = queryFunction.execute(results);
		}

		if (target instanceof IQueryAware) {
			IQueryAware<Q, R, IQuery<Q, R>> aware = (IQueryAware<Q, R, IQuery<Q, R>>)target;
			aware.afterExecute(this);
		}

		return results;
	}

	/**
	 * Adds a value (field) selector to the query.
	 * 
	 * @param valueSelector
	 *            A selector to add.
	 * @param <T>
	 *            A type of the selected value.
	 * @return This query.
	 */
	public <T> GenericQuery<Q, R> select(final IValueSelector<Q, T> valueSelector) {
		addValueSelector(valueSelector);
		return this;
	}

	/**
	 * Adds a value (field) selector to the query.
	 * 
	 * @param fields
	 *            A fields to select. Fields needs to be accessible by getters in the JavaBean naming convention.
	 * @return This query.
	 */
	public GenericQuery<Q, R> select(final String... fields) {
		for (String field : fields) {
			addValueSelector(ValueSelectors.<Q, Object> field(field));
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
	public GenericQuery<Q, R> matching(final IValueFilter<Q> filter) {
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
	public <S> GenericQuery<Q, R> matching(final String fieldName, final IValueFilter<S> filter) {
		addValueFilter(ValueFilters.<Q, S> fieldValue(fieldName, filter));
		return this;
	}

	/**
	 * Adds a function that will process results.
	 * 
	 * @param queryFunction
	 *            A function to execute on results.
	 * @return This query.
	 */
	public GenericQuery<Q, R> process(final IQueryFunction<R> queryFunction) {
		addQueryFunction(queryFunction);
		return this;
	}
}
