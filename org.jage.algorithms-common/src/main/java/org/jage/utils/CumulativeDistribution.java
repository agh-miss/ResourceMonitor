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
 * File: CumulatedDistribution.java
 * Created: 2011-05-05
 * Author: Krzywicki
 * $Id: CumulativeDistribution.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.utils;

import java.util.Arrays;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * A cumulative distribution. That is, a sequence of monotonic numbers in the range (0,..,1]
 *
 * @author AGH AgE Team
 */
public class CumulativeDistribution {

	private double[] data;

	/**
	 * Creates a CumulativeDistribution, using the provided data as internal representation. <br />
	 * <br />
	 * The assumptions on this data are that:
	 * <ul>
	 * <li>It is monotonic: data[i+1] >= data[i]</li>
	 * <li>It is included in the range (0,1]: data[0] >= 0.0, data[data.length] == 1.0</li>
	 * </ul>
	 * These assumptions are not checked, for efficiency purposes, but failing to meet them might result in undefined
	 * behavior.
	 *
	 * @param data
	 *            The distribution data, should be a monotonic range included in (0,1]
	 */
	public CumulativeDistribution(double[] data) {
		this.data = data;
	}

	/**
	 * Looks up this cumulative distribution for a given argument. This method returns the index of the first element
	 * which is equal or greater than the given argument. <br />
	 * <br />
	 * The assumption on this argument is that it is included in the distribution range, that is [0,1]. (It can be 0, as
	 * we are looking for greater or equal elements). This assumption is not checked, for efficiency purposes, but
	 * failing to meet it might result in undefined behavior.
	 *
	 *
	 * @param argument
	 *            The argument, should be contained in the range [0,1].
	 * @return The index of the first element equal or greater than the argument.
	 */
	public int getValueFor(double argument) {
		int search = Arrays.binarySearch(data, argument);
		return search >= 0 ? search : -search - 1;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append(data).toString();
	}
}
