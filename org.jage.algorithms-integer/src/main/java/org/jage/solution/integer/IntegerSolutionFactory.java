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
 * File: IntegerSolutionFactory.java
 * Created: 2011-11-03
 * Author: Krzywicki
 * $Id: IntegerSolutionFactory.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.solution.integer;

import java.util.List;

import org.jage.platform.component.annotation.Inject;
import org.jage.problem.IVectorProblem;
import org.jage.property.ClassPropertyContainer;
import org.jage.random.IIntRandomGenerator;
import org.jage.solution.ISolutionFactory;
import org.jage.solution.IVectorSolution;
import org.jage.solution.VectorSolution;

import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * Factory for creating IVectorSolution<Integer> instances.
 *
 * @author AGH AgE Team
 */
public final class IntegerSolutionFactory extends ClassPropertyContainer implements
        ISolutionFactory<IVectorSolution<Integer>> {

	@Inject
	private IIntRandomGenerator rand;

	@Inject
	private IVectorProblem<Integer> problem;

	@Override
	public IVectorSolution<Integer> createEmptySolution() {
		int[] representation = new int[problem.getDimension()];
		return new VectorSolution<Integer>(new FastIntArrayList(representation));
	}

	@Override
	public IVectorSolution<Integer> createInitializedSolution() {
		int[] representation = new int[problem.getDimension()];
		for (int i = 0; i < problem.getDimension(); i++) {
			representation[i] = problem.lowerBound(i)
			        + rand.nextInt((problem.upperBound(i) + 1) - problem.lowerBound(i));
		}

		return new VectorSolution<Integer>(new FastIntArrayList(representation));
	}

	@Override
	public IVectorSolution<Integer> copySolution(IVectorSolution<Integer> solution) {
		IntArrayList representation = (IntArrayList)solution.getRepresentation();
		return new VectorSolution<Integer>(new FastIntArrayList(representation));
	}

	/**
	 * Helper class with faster equals and compareTo methods.
	 *
	 * @author AGH AgE Team
	 */
	private static class FastIntArrayList extends IntArrayList {

        private static final long serialVersionUID = -2132234650006853053L;

		public FastIntArrayList(int[] representation) {
	        super(representation);
        }

		public FastIntArrayList(IntArrayList representation) {
	        super(representation);
        }

		@Override
		public boolean equals(Object o) {
			if (o instanceof IntArrayList) {
				return super.equals(o);
			} else {
				return super.equals(o);
			}
		}

		@Override
		public int hashCode() {
		    return super.hashCode();
		}

		@Override
		public int compareTo(List<? extends Integer> l) {
			if (l instanceof IntArrayList) {
				return super.compareTo((IntArrayList)l);
			} else {
				return super.compareTo(l);
			}
		}
	}
}
