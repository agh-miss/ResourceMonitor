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

import java.util.List;

/**
 * Maximum property function.
 * @author Tomek
 *
 */
public class MaxFunction extends NumericFunction {

	private Double _defaultValue;

	/**
	 * Constructor.
	 * @param functionName name of the function.
	 * @param argumentsPattern pattern for arguments.
	 * @param defaultValue default value of function, returned when no arguments are passed to it.
	 */
	public MaxFunction(String functionName, String argumentsPattern, double defaultValue) {
		super(functionName, argumentsPattern);
		_defaultValue = new Double(defaultValue);
	}
	
	/**
	 * Computes function's value. It returns the greatest argument, or default value,
	 * if not arguments were given.
	 */
	@Override
	protected Object computeValue(List<FunctionArgument> arguments)
			throws InvalidFunctionArgumentException {
		if (arguments.size() == 0) {
			return _defaultValue;
		}
		
		double result = Double.MIN_VALUE;
		for (FunctionArgument argument : arguments) {
			double argumentValue = getArgumentValue(argument);
			if (argumentValue > result) {
				result = argumentValue;
			}
		}
		
		return new Double(result);
	}

	/**
	 * Returns Double.class.
	 */
	@Override
	protected Class<?> getReturnType() {
		return Double.class;
	}

}
