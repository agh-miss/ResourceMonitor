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
package org.jage.property;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser that analyses property paths.
 * 
 * A grammar for property paths is as follows:
 * 	path : propertyIdentifier ( '.' propertyIdentifier )*;
 * 	propertyIdentifier : identifier arrayIndex*;
 * 	identifier : (~('[' | ']' | '.'))*;
 * 	arrayIndex : '[' number ']';
 * 	number : digit+;
 * 
 * This parser uses simple, hand-made recursive LL(1) alogorithm.
 * @author Tomek
 */
public class PropertyPathParser {
	
	private IPropertyContainer _root;
	private String _currentPath;
	private int _currentIndex;
	
	/**
	 * Constructor.
	 * @param root PropertyContainer that is root of the properties tree.
	 */
	public PropertyPathParser(IPropertyContainer root) {
		_root = root;
	}
	
	/**
	 * Returns all properties on the given path.
	 * @param path Property path.
	 * @return list of all properties on the given path. For example, for path "first[0][1].second",
	 * 		   this method will return two properties: the first with name "first", and the second
	 * 		   with name "second". 
	 * @throws InvalidPropertyPathException the specified property path is incorrect.
	 */
	public List<Property> getPropertiesOnPath(String path)
	throws InvalidPropertyPathException {
		try {
			_currentPath = path;
			_currentIndex = 0;
			IPropertyContainer currentPropertyContainer = _root;

			ArrayList<Property> result = new ArrayList<Property>();

			do {
				PropertyIndicesPair currentProperty = propertyIdentifier(currentPropertyContainer);
				result.add(currentProperty.getProperty());
				if (!isEndOfPath()) {
					match(".");
					Object currentPropertyValue = currentProperty.getValue();
					assertPropertyPathIsValid(currentPropertyValue instanceof IPropertyContainer);
					currentPropertyContainer = (IPropertyContainer)currentPropertyValue;
				}
			}
			while (!isEndOfPath());

			return result;
		}
		catch (InvalidPropertyOperationException ex) {
			throw new InvalidPropertyPathException("Invalid property path: " + path, ex);
		}
	}

	/**
	 * Returns property specified by the given path.
	 * @param path path to the property.
	 * @return property specified by the given path.
	 * @throws InvalidPropertyPathException the specified path is incorrect.		   
	 */
	public Property getPropertyForPath(String path) throws InvalidPropertyPathException {
		List<Property> allPropertiesOnPath = getPropertiesOnPath(path);
		return allPropertiesOnPath.get(allPropertiesOnPath.size() - 1);
	}
	
	/**
	 * Method for the "propertyIdentifer" rule.
	 * @param currentPropertyContainer property container that should contain property with
	 * 		  parsed name. 
	 * @return pair that contains property and indices for accessing one element in the multidimensional
	 * 		   array.
	 * @throws InvalidPropertyPathException
	 * @throws InvalidPropertyOperationException
	 */
	private PropertyIndicesPair propertyIdentifier(IPropertyContainer currentPropertyContainer) throws InvalidPropertyPathException, InvalidPropertyOperationException {
		List<Integer> indices = new ArrayList<Integer>();
		
		String propertyName = identifier();
		Property currentProperty = currentPropertyContainer.getProperties().getProperty(propertyName);
		assertPropertyPathIsValid(currentProperty != null);		
				
		while (nextChar() == '[') {
			match("[");
			int arrayIndex = number();
			indices.add(new Integer(arrayIndex));
			match("]");
		}
		
		if (isEndOfPath()) {
			assertPropertyPathIsValid(indices.size() == 0);
		}
		return new PropertyIndicesPair(currentProperty, indices);					
	}
	
	/**
	 * Method for the "identifier" rule.
	 * @return text (name) of identifier. 
	 * @throws InvalidPropertyPathException
	 */
	private String identifier() throws InvalidPropertyPathException {
		StringBuilder result = new StringBuilder();
		assertPropertyPathIsValid(!isEndOfPath() && 
				PropertyNamesHelper.isValidPropertyNameCharacter((char)nextChar()));
		
		do
		{
			result.append((char)nextChar());
			consume();
		} while (!isEndOfPath() && 
				PropertyNamesHelper.isValidPropertyNameCharacter((char)nextChar()));
		
		return result.toString();	
	}
		
	/**
	 * Method fot the "number" rule.
	 * @return value of the number.
	 * @throws InvalidPropertyPathException
	 */
	private int number() throws InvalidPropertyPathException {
		int result = 0;

		assertPropertyPathIsValid(!isEndOfPath() && Character.isDigit((char)nextChar()));
		result = nextChar() - '0';
		consume();
		
		while (!isEndOfPath() && Character.isDigit((char)nextChar())) {
			result *= 10;
			result += nextChar() - '0';
			consume();
		}
		return result;
	}
	
	/**
	 * Matches and consumes given string.
	 * @param pattern text to mach and consume.
	 * @throws InvalidPropertyPathException
	 */
	private void match(String pattern) throws InvalidPropertyPathException {
		for (int i = 0; i < pattern.length(); i++) {
			assertPropertyPathIsValid(nextChar() == pattern.charAt(i));
			consume();
		}
	}
	
	/**
	 * Value of the next character on the input, or -1, if there is no next character.
	 * @return
	 */
	private int nextChar() {
		return isEndOfPath() ? -1 : _currentPath.charAt(_currentIndex);  
	}
	
	/**
	 * Consumes one character from the current path and moves the index.
	 */
	private void consume() {
		_currentIndex++;
	}

	/**
	 * Checks if there are more character in the current path.
	 * @return true, if there are no more characters to parse; otherwise, returns false.
	 */
	private boolean isEndOfPath() {
		return _currentIndex >= _currentPath.length();
	}
	
	/**
	 * Checks whether the given condition is true. If not, throws and InvalidPropertyPathException
	 * exception.
	 * @param condition
	 * @throws InvalidPropertyPathException
	 */
	private void assertPropertyPathIsValid(boolean condition) throws InvalidPropertyPathException {
		if (!condition) {
			throw new InvalidPropertyPathException("Invalid property path: " + _currentPath);
		}
	}
	
	/**
	 * Pair that stores property and indices that allow to access a single element if value
	 * of the property is multidimensional array.
	 * @author Tomek
	 */
	private class PropertyIndicesPair {
		
		private Property _property;
		private List<Integer> _arrayIndices;
		
		public PropertyIndicesPair(Property property, List<Integer> indices) {
			_property = property;
			_arrayIndices = indices;
		}
		
		public Property getProperty() {
			return _property;
		}
		
		public List<Integer> getIndices() {
			return _arrayIndices;
		}
		
		public Object getValue() throws InvalidPropertyPathException, InvalidPropertyOperationException {
			Object currentValue = _property.getValue();
			for (Integer index : _arrayIndices) {
				assertPropertyPathIsValid(currentValue != null && currentValue.getClass().isArray());
				currentValue = Array.get(currentValue, index.intValue());
			}
			return currentValue;
		}
	}
}


