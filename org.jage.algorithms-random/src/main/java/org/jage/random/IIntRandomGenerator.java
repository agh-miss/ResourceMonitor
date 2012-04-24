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
 * File: IIntRandomGenerator.java
 * Created: 2011-11-03
 * Author: Krzywicki
 * $Id: IIntRandomGenerator.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.random;

import org.jage.strategy.IStrategy;

/**
 * An interface for generating random integer values. <br />
 * <br />
 * The contract of this interface is that values returned by <code>nextInt()</code> must be included between those
 * returned by <code>getLowerInt()</code> and <code>getUpperInt()</code>. <br />
 * <br />
 * Apart from that, those values can follow an arbitrary distribution, i.e. does not have to be uniformly distributed.
 *
 * @author AGH AgE Team
 */
public interface IIntRandomGenerator extends IStrategy {

	/**
	 * Returns a random integer value arbitrarily distributed in the range <code>[getLowerInt(), getUpperInt()]</code>.
	 *
	 * @return A random value.
	 */
	public int nextInt();

	/**
	 * Returns a random integer value arbitrarily distributed in the range <code>[0, range]</code>.
	 *
	 * @param range
	 *            The distribution range
	 * @return A random value.
	 */
	public int nextInt(int range);

	/**
	 * Specifies the lower bound of the values that can be returned by <code>nextInt()</code>.
	 *
	 * @return This generator's lower bound.
	 */
	public int getLowerInt();

	/**
	 * Specifies the upper bound of the values that can be returned by <code>nextInt()</code>.
	 *
	 * @return This generator's upper bound.
	 */
	public int getUpperInt();
}
