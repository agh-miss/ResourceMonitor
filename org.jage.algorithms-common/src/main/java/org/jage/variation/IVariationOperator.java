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
 * File: IVariationOperator.java
 * Created: 2011-10-20
 * Author: Krzywicki
 * $Id: IVariationOperator.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.variation;

import org.jage.population.IPopulation;
import org.jage.solution.ISolution;

/**
 * A general strategy interface for transforming a population of solutions, using mutations, recombinations or any other
 * operators (or their combination).
 *
 * @param <S>
 *            The type of solutions to be transformed
 * @author AGH AgE Team
 */
public interface IVariationOperator<S extends ISolution> {

	/**
	 * Transforms a given population of solutions.
	 *
	 * @param population
	 *            The population to be transformed
	 */
	public void transformPopulation(IPopulation<S, ?> population);
}
