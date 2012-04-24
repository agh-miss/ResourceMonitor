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
 * Average property function.
 * @author Tomek
 *
 */
public class AvgFunction extends NumericFunction {

	private double _defaultValue;
	
	/**
	 * Constructor.
	 * @param functionName name of the function.
	 * @param argumentsPattern pattern for arguments.
	 * @param defaultValue default value, used when pattern is'n resolved to any arguments.
	 */
	public AvgFunction(String functionName, String argumentsPattern, double defaultValue) {
		super(functionName, argumentsPattern);
		_defaultValue = defaultValue;
	}
	
	/**
	 * Computes function's value. It returns average of all it's arguments.
	 */
	@Override
	protected Object computeValue(List<FunctionArgument> arguments) 
	throws InvalidFunctionArgumentException {
		int count = 0;
		double sum = 0.0;
		
		for (FunctionArgument argument : arguments) {
			sum += getArgumentValue(argument);
			count++;
		}
		
		if (count == 0)
			return _defaultValue;
		
		return new Double(sum / count);
	}

	/**
	 * Returns Double.class.
	 */
	@Override
	protected Class<?> getReturnType() {
		return Double.class;
	}

}

