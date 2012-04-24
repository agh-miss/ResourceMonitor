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
 * File: RealValuedSolutionFactory.java
 * Created: 2011-11-03
 * Author: Krzywicki
 * $Id: RealValuedSolutionFactory.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.solution.realvalued;

import java.util.List;

import org.jage.platform.component.annotation.Inject;
import org.jage.problem.IVectorProblem;
import org.jage.property.ClassPropertyContainer;
import org.jage.random.INormalizedDoubleRandomGenerator;
import org.jage.solution.ISolutionFactory;
import org.jage.solution.IVectorSolution;
import org.jage.solution.VectorSolution;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;

/**
 * Factory for creating IVectorSolution<Double> instances.
 *
 * @author AGH AgE Team
 */
public class RealValuedSolutionFactory extends ClassPropertyContainer implements
        ISolutionFactory<IVectorSolution<Double>> {

	@Inject
	private INormalizedDoubleRandomGenerator rand;

	@Inject
	private IVectorProblem<Double> problem;

	@Override
	public IVectorSolution<Double> createEmptySolution() {
		double[] representation = new double[problem.getDimension()];
		return new VectorSolution<Double>(new FastDoubleArrayList(representation));
	}

	@Override
	public IVectorSolution<Double> createInitializedSolution() {
		double[] representation = new double[problem.getDimension()];
		for (int i = 0; i < problem.getDimension(); i++) {
			representation[i] = problem.lowerBound(i) + rand.nextDouble()
			        * (problem.upperBound(i) - problem.lowerBound(i));
		}

		return new VectorSolution<Double>(new FastDoubleArrayList(representation));
	}

	@Override
	public IVectorSolution<Double> copySolution(IVectorSolution<Double> solution) {
		DoubleArrayList representation = (DoubleArrayList)solution.getRepresentation();
		return new VectorSolution<Double>(new FastDoubleArrayList(representation));
	}

	/**
	 * Helper class with faster equals and compareTo methods.
	 *
	 * @author AGH AgE Team
	 */
	private static class FastDoubleArrayList extends DoubleArrayList {

        private static final long serialVersionUID = -162525815797087029L;

		public FastDoubleArrayList(double[] representation) {
	        super(representation);
        }

		public FastDoubleArrayList(DoubleArrayList representation) {
	        super(representation);
        }

		@Override
		public boolean equals(Object o) {
			if (o instanceof DoubleArrayList) {
				return super.equals((DoubleArrayList)o);
			} else {
				return super.equals(o);
			}
		}

		@Override
		public int hashCode() {
		    return super.hashCode();
		}

		@Override
		public int compareTo(List<? extends Double> l) {
			if (l instanceof DoubleArrayList) {
				return super.compareTo((DoubleArrayList)l);
			} else {
				return super.compareTo(l);
			}
		}
	}
}
