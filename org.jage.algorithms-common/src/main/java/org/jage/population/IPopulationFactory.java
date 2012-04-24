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
 * File: IPopulationFactory.java
 * Created: 2011-10-20
 * Author: Krzywicki
 * $Id: IPopulationFactory.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.population;

import org.jage.solution.ISolution;
import org.jage.strategy.IStrategy;

/**
 * A factory interface for creating {@link IPopulation} instances. <br />
 * <br />
 * This strategy allows to delegate the process of creating an initial population in an evolutionary algorithm. Various
 * implementation can then provide different initial conditions types.
 *
 * @param <S>
 *            The type of solutions created by this factory
 * @author AGH AgE Team
 */
public interface IPopulationFactory<S extends ISolution> extends IStrategy {

	/**
	 * Creates a population of solutions.
	 *
	 * @param <E>
	 *            the type of evaluation
	 *
	 * @return a population of solutions
	 */
	public <E> IPopulation<S, E> createPopulation();
}
