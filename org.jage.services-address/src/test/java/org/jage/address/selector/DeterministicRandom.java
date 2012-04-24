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
 * File: DeterministicRandom.java
 * Created: 2009-03-14
 * Author: awos
 * $Id: DeterministicRandom.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.address.selector;

import java.util.Random;

import org.junit.Ignore;

/**
 * A deterministic extension of {@link Random}. Only supports {@link #next(int)} .
 * 
 * @author Adam Wos <adam.wos@gmail.com>
 */
@Ignore
final class DeterministicRandom extends Random {

	private static final long serialVersionUID = 5179378223463072380L;

	private final int[] results;

	private int i = 0;

	/**
	 * Default constructor.
	 * 
	 * @param results
	 *            array of integers that will be subsequently returned by {@link #nextInt()}.
	 */
	public DeterministicRandom(int[] results) {
		this.results = results;
	}

	/**
	 * Returns next integer from the results array.
	 * 
	 * @return deterministically next random.
	 * @throws IllegalStateException
	 *             if not enough integers were provided in the constructor.
	 */
	@Override
	public int nextInt() {
		if (i < results.length) {
			return results[i];
		} else {
			throw new IllegalStateException("results not big enough");
		}
	}
}
