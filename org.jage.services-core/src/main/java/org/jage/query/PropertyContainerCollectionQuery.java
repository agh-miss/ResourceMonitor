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
 * File: PropertyContainerCollectionQuery.java
 * Created: 2011-09-12
 * Author: faber
 * $Id: PropertyContainerCollectionQuery.java 76 2012-02-27 20:55:05Z faber $
 */

package org.jage.query;

import java.util.ArrayList;
import java.util.Collection;

import org.jage.property.IPropertyContainer;
import org.jage.property.InvalidPropertyPathException;
import org.jage.property.Property;

/**
 * Query implementation that can be used with subclasses of the {@link Collection} class.
 * 
 * @param <E>
 *            A type of elements in the collection. T must be a realisation of {@link IPropertyContainer}.
 * @param <T>
 *            A type of elements in the result.
 * 
 * @author AGH AgE Team
 */
public class PropertyContainerCollectionQuery<E extends IPropertyContainer, T> extends CollectionQuery<E, T> {

	/**
	 * Constructs a new PropertyContainerCollectionQuery instance. PropertyContainerCollectionQuery requires the target,
	 * the result and the element class being provided. It will use them for creating the result object.
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
	public PropertyContainerCollectionQuery(Class<?> elementClass, Class<?> targetClass, Class<?> resultClass) {
		super(elementClass, targetClass, resultClass);
	}

	/**
	 * Constructs a new PropertyContainerCollectionQuery instance. This constructor requires only the element class
	 * being provided. Collection is used as the target class and ArrayList as the result class.
	 * 
	 * @param elementClass
	 *            A class of the components in the queried composition.
	 */
	public PropertyContainerCollectionQuery(Class<?> elementClass) {
		this(elementClass, Collection.class, ArrayList.class);
	}

	/**
	 * Constructs a new PropertyContainerCollectionQuery instance. This constructor uses following default values:
	 * IPropertyContainer is used as the element class, Collection as the target class and ArrayList as the result
	 * class.
	 */
	public PropertyContainerCollectionQuery() {
		this(IPropertyContainer.class, Collection.class, ArrayList.class);
	}

	/**
	 * Adds a value filter to the query. This value filter operates on the property with the given name.
	 * 
	 * @param propertyName
	 *            A name of the property that will be used in filtering.
	 * @param filter
	 *            A value filter to add.
	 * @param <S>
	 *            A type of a value of the tested property.
	 * @return This query.
	 */
	@Override
	public <S> PropertyContainerCollectionQuery<E, T> matching(final String propertyName, final IValueFilter<S> filter) {
		addValueFilter(new IValueFilter<E>() {
			@SuppressWarnings("unchecked")
			@Override
			public boolean matches(E obj) {
				try {
					return filter.matches((S)obj.getProperty(propertyName).getValue());
				} catch (InvalidPropertyPathException e) {
					throw new QueryException("Cannot dereference a property.", e);
				}
			}
		});
		return this;
	}

	// Forwarding methods to keep the correct type in the fluent interface

	@Override
	public PropertyContainerCollectionQuery<E, T> from(IInitialSelector selector) {
		return (PropertyContainerCollectionQuery<E, T>)super.from(selector);
	}

	@Override
	public <S> PropertyContainerCollectionQuery<E, T> select(IValueSelector<? super E, S> valueSelector) {
		return (PropertyContainerCollectionQuery<E, T>)super.select(valueSelector);
	}

	@Override
	public PropertyContainerCollectionQuery<E, T> select(String... fields) {
		return (PropertyContainerCollectionQuery<E, T>)super.select(fields);
	}

	@Override
	public PropertyContainerCollectionQuery<E, T> matching(IValueFilter<? super E> filter) {
		return (PropertyContainerCollectionQuery<E, T>)super.matching(filter);
	}

	@Override
	public PropertyContainerCollectionQuery<E, T> process(IQueryFunction<? super Collection<T>> queryFunction) {
		return (PropertyContainerCollectionQuery<E, T>)super.process(queryFunction);
	}

	// End of forwarding methods
}
