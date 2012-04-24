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
 * File: OnePointRecombination.java
 * Created: 21-03-2012
 * Author: Krzywicki
 * $Id: OnePointRecombination.java 213 2012-04-08 20:21:35Z krzywick $
 */

package org.jage.variation.recombination.realvalued;

import static java.lang.System.arraycopy;

import org.jage.platform.component.annotation.Inject;
import org.jage.random.IIntRandomGenerator;
import org.jage.solution.IVectorSolution;
import org.jage.strategy.AbstractStrategy;
import org.jage.variation.recombination.IRecombine;

import it.unimi.dsi.fastutil.doubles.DoubleList;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Recombines the representations of two real-valued solutions at a random point.
 *
 * @author AGH AgE Team
 */
public class OnePointRecombination extends AbstractStrategy implements IRecombine<IVectorSolution<Double>> {

	@Inject
	private IIntRandomGenerator rand;

    @Override
    public void recombine(final IVectorSolution<Double> solution1, final IVectorSolution<Double> solution2) {
    	double[] representation1 = ((DoubleList)solution1.getRepresentation()).toDoubleArray();
    	double[] representation2 = ((DoubleList)solution2.getRepresentation()).toDoubleArray();
    	checkArgument(representation1.length == representation2.length);

    	int recombinationPoint = rand.nextInt(representation1.length);

		double[] tmp = new double[recombinationPoint];
		arraycopy(representation1, 0, tmp, 0, recombinationPoint);
		arraycopy(representation2, 0, representation1, 0, recombinationPoint);
		arraycopy(tmp, 0, representation2, 0, recombinationPoint);
    }
}
