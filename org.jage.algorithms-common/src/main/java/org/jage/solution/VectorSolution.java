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
 * File: VectorSolution.java
 * Created: 2011-10-20
 * Author: Krzywicki
 * $Id: VectorSolution.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.solution;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Default, generic implementation of {@link IVectorSolution}, capable of having an arbitrary representation type. <br />
 * <br />
 * If the representation type has to be a boxed one (because of java generics restrictions), i.e. Double instead of
 * double, consider using primitive supporting collections, like fastutil ones, as representation. You then still have
 * compile-time type safety, while being able, when needed, to cast at runtime to a more specific, yet still quite
 * abstract representation (DoubleList in the upper example). This way we achieve a good tradeoff between efficiency and
 * API cleanness.
 *
 * @param <R>
 *            The representation's type.
 * @author AGH AgE Team
 */
public final class VectorSolution<R> implements IVectorSolution<R> {

	private List<R> representation;

	/**
	 * Creates a VectorSolution with a given representation.
	 *
	 * @param representation
	 *            the representation of the new solution
	 */
	public VectorSolution(List<R> representation) {
		this.representation = representation;
	}

	@Override
	public final List<R> getRepresentation() {
		return representation;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
		        .append("representation", this.getRepresentation()).toString();
	}
}
