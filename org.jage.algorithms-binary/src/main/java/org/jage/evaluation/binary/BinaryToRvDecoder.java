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
 * File: BinaryToRvDecoder.java
 * Created: 2011-11-03
 * Author: Krzywicki
 * $Id: BinaryToRvDecoder.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.evaluation.binary;

import org.jage.property.ClassPropertyContainer;
import org.jage.solution.IVectorSolution;
import org.jage.solution.VectorSolution;

import it.unimi.dsi.fastutil.booleans.BooleanList;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;

/**
 * Decodes a binary solution to a real-valued one.
 *
 * @author AGH AgE Team
 */
public class BinaryToRvDecoder extends ClassPropertyContainer implements
        ISolutionDecoder<IVectorSolution<Boolean>, IVectorSolution<Double>> {

	@Override
	public IVectorSolution<Double> decodeSolution(IVectorSolution<Boolean> solution) {
		DoubleList representation = transformRepresentation((BooleanList)solution.getRepresentation());
		return new VectorSolution<Double>(representation);
	}

	protected DoubleList transformRepresentation(BooleanList representation) {
		int n = representation.size() / Double.SIZE;
		DoubleList decoded = new DoubleArrayList(n);
		for (int i = 0; i < n; i++) {
			decoded.set(i, binaryToDouble(representation, i * Double.SIZE, Double.SIZE));
		}
		return decoded;
	}

	protected double binaryToDouble(BooleanList representation, int offset, int length) {
		long longBits = binaryToLongBits(representation, offset, length);
		return Double.longBitsToDouble(longBits);
	}

	private long binaryToLongBits(BooleanList representation, int offset, int length) {
		long longBits = 0;
		for (int i = offset; i < offset + length; i++) {
			longBits <<= 1;
			longBits += representation.getBoolean(i) ? 1 : 0;
		}
		return longBits;
	}
}
