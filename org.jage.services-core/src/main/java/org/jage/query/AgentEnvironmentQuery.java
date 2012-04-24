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
 * File: AgentEnvironmentQuery.java
 * Created: 2011-09-19
 * Author: faber
 * $Id: AgentEnvironmentQuery.java 213 2012-04-08 20:21:35Z krzywick $
 */

package org.jage.query;

import java.util.ArrayList;
import java.util.Collection;

import org.jage.agent.IAgent;
import org.jage.agent.IAgentEnvironment;
import org.jage.agent.IAggregate;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Query implementation that can be used with implementations of the {@link IAgentEnvironment} interface.
 *
 * @param <E>
 *            A type of elements in the collection. It must be a realisation of {@link IAgent}.
 * @param <T>
 *            A type of elements in the result.
 *
 * @author AGH AgE Team
 */
public class AgentEnvironmentQuery<E extends IAgent, T> extends PropertyContainerCollectionQuery<E, T> {

	/**
	 * Constructs a new AgentEnvironmentQuery instance. AgentEnvironmentQuery requires the target, the result and the
	 * element class being provided. It will use them for creating the result object.
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
	public AgentEnvironmentQuery(Class<?> elementClass, Class<?> targetClass, Class<?> resultClass) {
		super(elementClass, targetClass, resultClass);
	}

	/**
	 * Constructs a new AgentEnvironmentQuery instance. This constructor requires only the element class being provided.
	 * Collection is used as the target class and ArrayList as the result class.
	 *
	 * @param elementClass
	 *            A class of the components in the queried composition.
	 */
	public AgentEnvironmentQuery(Class<?> elementClass) {
		this(elementClass, Collection.class, ArrayList.class);
	}

	/**
	 * Constructs a new AgentEnvironmentQuery instance. This constructor uses following default values: IAgent is used
	 * as the element class, Collection as the target class and ArrayList as the result class.
	 */
	public AgentEnvironmentQuery() {
		this(IAgent.class, Collection.class, ArrayList.class);
	}

	// Forwarding methods to keep the correct type in the fluent interface

	@Override
	public AgentEnvironmentQuery<E, T> from(IInitialSelector selector) {
		return (AgentEnvironmentQuery<E, T>)super.from(selector);
	}

	@Override
	public <S> AgentEnvironmentQuery<E, T> select(IValueSelector<? super E, S> valueSelector) {
		return (AgentEnvironmentQuery<E, T>)super.select(valueSelector);
	}

	@Override
	public AgentEnvironmentQuery<E, T> select(String... fields) {
		return (AgentEnvironmentQuery<E, T>)super.select(fields);
	}

	@Override
	public AgentEnvironmentQuery<E, T> matching(IValueFilter<? super E> filter) {
		return (AgentEnvironmentQuery<E, T>)super.matching(filter);
	}

	@Override
	public <S> AgentEnvironmentQuery<E, T> matching(String fieldName, IValueFilter<S> filter) {
		return (AgentEnvironmentQuery<E, T>)super.matching(fieldName, filter);
	}

	@Override
	public AgentEnvironmentQuery<E, T> process(IQueryFunction<? super Collection<T>> queryFunction) {
		return (AgentEnvironmentQuery<E, T>)super.process(queryFunction);
	}

	// End of forwarding methods

	/**
	 * Executes the query on a provided agent environment and generates results.
	 *
	 * @param target
	 *            An object to perform query on.
	 * @return Results of the execution.
	 */
	@SuppressWarnings("unchecked")
	public Collection<T> execute(IAgentEnvironment target) {
		checkArgument(target instanceof IAggregate, "This query can operate only on aggregates.");

		return super.execute((Collection<E>)target);
	}

}
