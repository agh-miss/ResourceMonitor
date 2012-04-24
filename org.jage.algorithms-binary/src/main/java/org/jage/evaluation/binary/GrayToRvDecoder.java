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
 * File: GrayToRvDecoder.java
 * Created: 2011-11-03
 * Author: Krzywicki
 * $Id: GrayToRvDecoder.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.evaluation.binary;

import it.unimi.dsi.fastutil.booleans.BooleanList;

/**
 * Extends {@link BinaryToRvDecoder} one with Gray decoding.
 *
 * @author AGH AgE Team
 */
public final class GrayToRvDecoder extends BinaryToRvDecoder {

	@Override
	protected double binaryToDouble(BooleanList representation, int offset, int length) {
		long longBits = graycodeToLongBits(representation, offset, length);
		return Double.longBitsToDouble(longBits);
	}

	private long graycodeToLongBits(BooleanList representation, int offset, int length) {
		boolean currentBit = representation.getBoolean(offset);
		long longBits = currentBit ? 1 : 0;

		for (int i = offset + 1; i < offset + length; i++) {
			currentBit = currentBit ^ representation.getBoolean(i);
			longBits <<= 1;
			longBits += currentBit ? 1 : 0;
		}

		return longBits;
	}
}
