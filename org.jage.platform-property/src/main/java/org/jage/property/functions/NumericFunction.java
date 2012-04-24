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

import org.jage.property.InvalidPropertyPathException;

/**
 * Base class for all numeric functions.
 * @author Tomek
 *
 */
public abstract class NumericFunction extends PropertyFunction {
	
	/**
	 * Constructor.
	 * @param functionName name of the function.
	 * @param argumentsPattern pattern for arguments.
	 */
	protected NumericFunction(String functionName, String argumentsPattern) {
		super(functionName, argumentsPattern);
	}
	
	/**
	 * Gets value of the argument converted to double value. This method
	 * accepts arguments that are of type double, float or integer.
	 * @param argument function's argument
	 * @return value of function's argument converted to double.
	 * @throws InvalidFunctionArgumentException the given function's argument is not 
	 * valid or it's value cannot be converted to double.
	 */
	protected double getArgumentValue(FunctionArgument argument) 
	throws InvalidFunctionArgumentException {
		try {
			Class<?> type = argument.getMetaProperty().getPropertyClass();
			Object value = argument.getValue();
			if (type == Double.class) return ((Double)value).doubleValue();
			if (type == Float.class) return ((Float)value).doubleValue();
			if (type == Integer.class) return ((Integer)value).doubleValue();
			throw new InvalidFunctionArgumentException("Cannot compute sum for not-numerical arguments");
		}
		catch (InvalidPropertyPathException ex) {
			throw new InvalidFunctionArgumentException("Invalid function argument.", ex);
		}
	}
}

