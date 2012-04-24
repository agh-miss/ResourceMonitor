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
 * File: IDoubleSymmetricGenerator.java
 * Created: 2011-11-03
 * Author: Krzywicki
 * $Id: IDoubleSymmetricGenerator.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.random;

/**
 * This interface introduces additional semantics to the {@link IDoubleRandomGenerator} one. <br />
 * <br />
 * It is intended for random distributions which take two additional parameters, a location and a scale. The statistical
 * interpretation of these parameters is left to implementations. <br />
 * <br />
 * The <code>nextDouble()</code> method is now assumed to use some default values for these parameters.
 *
 * @author AGH AgE Team
 */
public interface IDoubleSymmetricGenerator extends IDoubleRandomGenerator {

	/**
	 * Returns a random double value arbitrarily distributed in the range
	 * <code>[getLowerDouble(), getUpperDouble()]</code>, accordingly to the provided parameters.
	 *
	 * @param location
	 *            The location of the distribution.
	 * @param scale
	 *            The scale of the distribution
	 *
	 * @return A random value.
	 */
	public double nextDouble(double location, double scale);
}
