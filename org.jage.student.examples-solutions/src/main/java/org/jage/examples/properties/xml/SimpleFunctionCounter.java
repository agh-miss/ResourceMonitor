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
 * File: SimpleFunctionCounter.java
 * Created: 2011-03-15
 * Author: kpietak
 * $Id: SimpleFunctionCounter.java 124 2012-03-18 10:27:39Z krzywick $
 */

package org.jage.examples.properties.xml;

import org.jage.platform.component.annotation.Inject;
import org.jage.platform.component.annotation.Require;

/**
 * The sample component to use with XML-defined properties. It is described in the file
 * SimpleFunctionCounter.contract.xml.
 *
 * @author AGH AgE Team
 */
public class SimpleFunctionCounter {

	@SuppressWarnings("unused")
	private final String version = "1.0.0";

	private int xParameter;

	private int yParameter;

	/**
	 * Creates a SimpleFunctionCounter instance.
	 */
	public SimpleFunctionCounter() {
		xParameter = 0;
		yParameter = 0;
	}

	/**
	 * Creates a SimpleFunctionCounter instance.
	 *
	 * @param x
	 *            X parameter.
	 * @param y
	 *            Y parameter.
	 */
	public SimpleFunctionCounter(final int x, final int y) {
		xParameter = x;
		yParameter = y;
	}

	@Require
	public int getXParameter() {
		return xParameter;
	}

	@Inject
	public void setXParameter(final int xParameter) {
		this.xParameter = xParameter;
	}

	/**
	 * Returns a result of <code>x^2 + y^2</code> and increases values of x and y.
	 *
	 * @return A result of computation.
	 */
	public int countSquareSum() {
		final int result = xParameter * xParameter + yParameter * yParameter;
		xParameter++;
		yParameter++;
		return result;
	}

	@Require
	public int getY() {
		return yParameter;
	}

	@Inject
	public void setYParameter(final int yParameter) {
		this.yParameter = yParameter;
	}

	@Override
	public String toString() {
		return xParameter + "^2 + " + yParameter + "^2";
	}
}
