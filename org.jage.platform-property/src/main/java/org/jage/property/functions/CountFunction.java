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
package org.jage.property.functions;

import java.util.Iterator;
import java.util.List;

/**
 * Count property function.
 * @author Tomek
 *
 */
public class CountFunction extends NumericFunction {

	/**
	 * Constructor.
	 * @param functionName name of the function.
	 * @param argumentsPattern pattern for arguments.
	 */
	public CountFunction(String functionName, String argumentsPattern) {
		super(functionName, argumentsPattern);
	}

	/**
	 * Computes function's value. It returns number of arguments.
	 */
	@Override
	protected Object computeValue(List<FunctionArgument> arguments) {
		int result = 0;
		Iterator<FunctionArgument> iterator = arguments.iterator();
		while (iterator.hasNext()) {
			iterator.next();
			result++;
		}
		return new Integer(result);
	}

	/**
	 * Returns Integer.class.
	 */
	@Override
	protected Class<?> getReturnType() {
		return Integer.class;
	}

}

