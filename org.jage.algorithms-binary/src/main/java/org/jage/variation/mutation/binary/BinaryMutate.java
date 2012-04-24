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
 * File: BinaryMutate.java
 * Created: 2011-11-03
 * Author: Krzywicki
 * $Id: BinaryMutate.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.variation.mutation.binary;

import java.util.List;

import org.jage.variation.mutation.AbstractStochasticMutate;

import it.unimi.dsi.fastutil.booleans.BooleanList;

/**
 * Flips the given binary feature.
 *
 * @author AGH AgE Team
 */
public final class BinaryMutate extends AbstractStochasticMutate<Boolean> {

	@Override
	protected void doMutate(List<Boolean> representation, int index) {
		BooleanList list = (BooleanList)representation;
		list.set(index, !list.getBoolean(index));
	}
}
