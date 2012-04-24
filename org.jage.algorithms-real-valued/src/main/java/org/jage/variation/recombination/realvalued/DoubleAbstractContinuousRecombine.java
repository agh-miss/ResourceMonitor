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
 * File: DoubleAbstractContinuousRecombine.java
 * Created: 2011-10-28
 * Author: Krzywicki
 * $Id: DoubleAbstractContinuousRecombine.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.variation.recombination.realvalued;

import java.util.List;

import org.jage.variation.recombination.AbstractContinuousRecombine;

import it.unimi.dsi.fastutil.doubles.DoubleList;

/**
 * Abstract class which efficiently unboxes Double AbstractContinuousRecombine.
 *
 * @author AGH AgE Team
 */
public abstract class DoubleAbstractContinuousRecombine extends AbstractContinuousRecombine<Double> {

	@Override
	protected void doRecombine(List<Double> representation1, List<Double> representation2, int index) {
		DoubleList list1 = (DoubleList)representation1;
		DoubleList list2 = (DoubleList)representation2;

		double oldValue1 = list1.getDouble(index);
		double oldValue2 = list2.getDouble(index);

		list1.set(index, doRecombine(oldValue1, oldValue2));
		list2.set(index, doRecombine(oldValue1, oldValue2));
	}

	/**
	 * Perform the actual recombination on primitive doubles.
	 *
	 * @param value1
	 *            The first value
	 * @param value2
	 *            The second value
	 * @return a recombinated value
	 */
	protected abstract double doRecombine(double value1, double value2);
}
