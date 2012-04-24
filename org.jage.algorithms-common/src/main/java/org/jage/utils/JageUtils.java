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
 * File: GeneticUtils.java
 * Created: 2011-05-01
 * Author: Krzywicki
 * $Id: JageUtils.java 19 2012-01-30 13:47:18Z krzywick $
 */

package org.jage.utils;

import org.jage.agent.AgentException;
import org.jage.agent.IAgent;
import org.jage.population.IPopulation;
import org.jage.population.IPopulation.Tuple;
import org.jage.property.InvalidPropertyOperationException;
import org.jage.property.InvalidPropertyPathException;
import org.jage.property.Property;
import org.jage.solution.ISolution;

/**
 * This class provides some common utility methods, used in the genetic module.
 *
 * @author AGH AgE Team
 */
public class JageUtils {

	/**
	 * Produces a log String describing a given iterable of solutions.
	 *
	 * @param <S>
	 *            the type of solutions.
	 * @param <E>
	 *            the type of evaluation
	 * @param solutions
	 *            a collection of solutions.
	 * @param msg
	 *            a header message
	 * @return a formatted log string.
	 */
	public static <S extends ISolution, E> String getPopulationLog(Iterable<S> solutions, String msg) {
		int i = 0;
		StringBuilder builder = logBuilder(msg);
		for (S solution : solutions) {
			builder.append(String.format("\n\t[%1$d] %2$s", i++, solution));
		}
		return builder.toString();
	}

	/**
	 * Produces a log String describing a given solution-evaluation mapping.
	 *
	 * @param <S>
	 *            the type of solutions.
	 * @param <E>
	 *            the type of evaluation
	 * @param population
	 *            a population.
	 * @param msg
	 *            a header message
	 * @return a formatted log string.
	 */
	public static <S extends ISolution, E> String getPopulationLog(IPopulation<S, E> population, String msg) {
		int i = 0;
		StringBuilder builder = logBuilder(msg);
		for (Tuple<S, E> e : population.asTupleList()) {
			builder.append(String.format("\n\t[%1$d] %2$f %3$s", i++, e.getEvaluation(), e.getSolution()));
		}
		return builder.toString();
	}

	private static StringBuilder logBuilder(String msg) {
		return new StringBuilder("\n\t---=== " + msg + " ===---");
	}

	/**
	 * Wraps an agent property access into a convenient method. Returns the property or throws an exception, if unable
	 * to.
	 *
	 * @param agent
	 *            The agent whose property is to be accessed.
	 * @param propertyName
	 *            The name of the property.
	 * @return The property
	 * @throws AgentException
	 *             If it was not possible to access the property.
	 */
	public static Property getPropertyOrThrowException(IAgent agent, String propertyName) throws AgentException {
		try {
			return agent.getProperty(propertyName);
		} catch (InvalidPropertyPathException e) {
			throw new AgentException(String.format("Unable to access agent's %1$s property", propertyName), e);
		}
	}

	/**
	 * Wraps an agent property value access into a convenient method. Returns the property value or throws an exception,
	 * if unable to.
	 *
	 * @param <T>
	 *            the expected object type
	 *
	 * @param agent
	 *            The agent whose property is to be accessed.
	 * @param propertyName
	 *            The name of the property.
	 * @return The property value
	 * @throws AgentException
	 *             If it was not possible to access the property.
	 */
	// We can ignore the warnings, a classcast is appropriate there
	@SuppressWarnings("unchecked")
	public static <T> T getPropertyValueOrThrowException(IAgent agent, String propertyName) throws AgentException {
		return (T)getPropertyOrThrowException(agent, propertyName).getValue();
	}

	/**
	 * Wraps an agent property update into a convenient method. Updates the property or throws an exception, if unable
	 * to.
	 *
	 * @param agent
	 *            The agent whose property is to be accessed.
	 * @param propertyName
	 *            The name of the property.
	 * @param value
	 *            The new property value.
	 * @throws AgentException
	 *             If it was not possible to update the property.
	 */
	public static void setPropertyValueOrThrowException(IAgent agent, String propertyName, Object value) throws AgentException {
		try {
			getPropertyOrThrowException(agent, propertyName).setValue(value);
		} catch (InvalidPropertyOperationException e) {
			throw new AgentException(String.format("Unable to update agent's %1$s property", propertyName), e);
		}
	}
}
