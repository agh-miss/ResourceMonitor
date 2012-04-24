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

import java.util.ArrayList;
import java.util.List;

import org.jage.property.IPropertyContainer;
import org.jage.property.PropertyException;
import org.jage.property.PropertyNamesHelper;

/**
 * Default implementation for IFunctionArgumentsResolver interface.
 * A valid pattern for this resolver is a list o comma-separated paths.
 * Each path must be a regular property path (see PropertyPathParser class
 * for more details), with one exception - as and array index, a star ('*') char
 * can be used, what means that all elements from this array will be resolved as
 * function arguments.
 * Example:
 * resolveArguments with pattern "children[*].fitness" will return a separate
 * 	FunctionArgument object for each element from the children[] array.
 *
 * Grammar for valid patterns:
 * A grammar for property paths is as follows:
 *  pattern : path ( ',' path )*;
 * 	path : (propertyIdentifier ( '.' propertyIdentifier )*)? lastPropertyIdentifier;
 * 	propertyIdentifier : identifier arrayIndex*;
 *  lastPropertyIdentifier : identifier;
 * 	identifier : ('@')? (propertyChar)+
 * 	arrayIndex : '[' ( number | '*' )']';
 * 	number : digit+;
 *  star : '*';
 *
 * @author Tomek
 *
 */
public class DefaultFunctionArgumentsResolver implements IFunctionArgumentsResolver {

	private IPropertyContainer _rootObject;
	private String _currentPattern;
	private int _currentIndex;

	/**
	 * Constructor.
	 * @param rootObject
	 */
	public DefaultFunctionArgumentsResolver(IPropertyContainer rootObject) {
		_rootObject = rootObject;
	}

	/**
	 * Finds all function arguments for the given pattern.
	 * @param argumentsPattern pattern that decribes function arguments.
	 * @return list of all resolved arguments.
	 * @throws InvalidArgumentsPatternException the given pattern is not correct according
	 * to used grammar.
	 */
	public List<FunctionArgument> resolveArguments(String argumentsPattern)
	throws InvalidArgumentsPatternException {
		_currentPattern = argumentsPattern;
		_currentIndex = 0;
		List<FunctionArgument> result = null;
		try {
			result = pattern();
		} catch (PropertyException e) {
			throw new InvalidArgumentsPatternException(e);
		}
		return result;
	}

	/**
	 * Method for pattern grammar rule.
	 * @return resolved function arguments.
	 * @throws InvalidArgumentsPatternException
	 * @throws PropertyException
	 */
	private List<FunctionArgument> pattern() throws InvalidArgumentsPatternException, PropertyException {
		ArrayList<FunctionArgument> result = new ArrayList<FunctionArgument>();
		path(result);
		while (!isEndOfPattern()) {
			match(",");
			path(result);
		}
		return result;
	}

	/**
	 * Method for 'path' rule in the grammar. It finds arguments for a single path
	 * (paths in pattern are separated by the comma).
	 * @param result collection where arguments will be added.
	 * @throws InvalidArgumentsPatternException
	 * @throws PropertyException
	 */
	private void path(List<FunctionArgument> result)
	throws InvalidArgumentsPatternException, PropertyException {

		ArrayList<IPropertyContainer> currentContainers = new ArrayList<IPropertyContainer>();
		currentContainers.add(_rootObject);
		IdentifierReference currentIdentifier = propertyIdentifier();

		while (nextChar() == '.') {
			match(".");
			currentContainers = mergeContainersForPropertyIdentifier(currentContainers, currentIdentifier);
			currentIdentifier = propertyIdentifier();
		}

		// The last identifier on the path cannot be indexed. Excluding lastPropertyIdentifier
		// from the grammar makes it LA(1) and simplifies parsing.
		assertArgumentsPatternIsValid(!currentIdentifier.isArray());

		// Constructing result.
		for (IPropertyContainer container : currentContainers) {
			if (container.getProperties().containsProperty(currentIdentifier.getIdentifierName())) {
				result.add(new FunctionArgument(container, currentIdentifier.getIdentifierName()));
			}
		}
	}

	/**
	 * Merges all resolved property containers from previous identifiers on the path
	 * with new identifier.
	 * For example, when we have path:
	 * a[*].b[*].c
	 * Array 'a' has three elements that are property containers:
	 * a1, a2, a3
	 * And array 'b' for each 'a' has two elements that are property containers:
	 * b1, b2
	 * Then, when "b" identifier is found, this method will be invoked with arguments:
	 * containers: ( a1, a2, a3)
	 * identifier: "b, '*'"
	 * And will return six objects: (a1.b1), (a1.b2), (a2.b1), (a2.b2), (a3.b1), (a3.b2)
	 * @param containers resolved containers from previous indentifiers on the path.
	 * @param identifier new identifier.
	 * @return
	 * @throws InvalidArgumentsPatternException
	 * @throws PropertyException
	 */
	private ArrayList<IPropertyContainer> mergeContainersForPropertyIdentifier(
			List<IPropertyContainer> containers, IdentifierReference identifier)
	throws InvalidArgumentsPatternException, PropertyException {
		ArrayList<IPropertyContainer> newContainers = new ArrayList<IPropertyContainer>();
		for (IPropertyContainer previousLevelContainer : containers) {
			newContainers.addAll(getContainersForIdentifier(previousLevelContainer, identifier));
		}
		return newContainers;
	}

	private List<IPropertyContainer> getContainersForIdentifier(IPropertyContainer root,
			IdentifierReference propertyDefinition)
	throws InvalidArgumentsPatternException, PropertyException {
		List<IPropertyContainer> result = new ArrayList<IPropertyContainer>();

 		for (Object container : propertyDefinition.getAllValues(root)) {
			result.add((IPropertyContainer)container);
		}

		return result;
	}

	/**
	 * Method for the propertyIdentifier grammar rule.
	 * @return property identifier description recognized by this rule.
	 * @throws InvalidArgumentsPatternException
	 */
	private IdentifierReference propertyIdentifier()
	throws InvalidArgumentsPatternException {
		String propertyName = identifier();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		while (nextChar() == '[') {
			match("[");
			indices.add(arrayIndex());
			match("]");
		}

		return createIdentifierReference(propertyName, indices);
	}

	/**
	 * Factory method. Creates a new IdentifierReference object.
	 * @param propertyName identifier name.
	 * @param indices list of indices.
	 * @return
	 */
	protected IdentifierReference createIdentifierReference(String propertyName, List<Integer> indices) {
		return new IdentifierReference(propertyName, indices);
	}

	/**
	 * Method for the identifier grammar rule.
	 * @return property name recognized by this rule.
	 * @throws InvalidArgumentsPatternException
	 */
	private String identifier()
	throws InvalidArgumentsPatternException {
		StringBuilder builder = new StringBuilder();

		if (nextChar() == '@') {
			match("@");
			builder.append("@");
		}

		assertArgumentsPatternIsValid(!isEndOfPattern() &&
				PropertyNamesHelper.isValidPropertyNameCharacter((char)nextChar()));

		while (!isEndOfPattern() &&
				PropertyNamesHelper.isValidPropertyNameCharacter((char)nextChar())) {
			builder.append((char)nextChar());
			consume();
		}

		return builder.toString();
	}

	/**
	 * Method for arrayIndex grammar rule.
	 * @return -1, if star ('*') was used as index; otherwise, a number used
	 * as index.
	 * @throws InvalidArgumentsPatternException
	 */
	private Integer arrayIndex()
	throws InvalidArgumentsPatternException {
		if (nextChar() == '*') {
			consume();
			return new Integer(-1);
		}
		else {
			return number();
		}
	}

	/**
	 * Method for number grammar rule.
	 * @return integer number recognized by this rule.
	 * @throws InvalidArgumentsPatternException
	 */
	private Integer number()
	throws InvalidArgumentsPatternException {
		int result = 0;

		assertArgumentsPatternIsValid(!isEndOfPattern() && Character.isDigit((char)nextChar()));
		result = nextChar() - '0';
		consume();

		while (!isEndOfPattern() && Character.isDigit((char)nextChar())) {
			result *= 10;
			result += nextChar() - '0';
			consume();
		}
		return new Integer(result);

	}

	/**
	 * Gets next character from the pattern that is being recognized, or -1,
	 * if end of pattern was reached.
	 * @return next character from the current pattern or -1, if end of pattern
	 * was reached.
	 */
	private int nextChar() {
		return isEndOfPattern() ? -1 : _currentPattern.charAt(_currentIndex);
	}

	/**
	 * Consumes next character from the pattern.
	 */
	private void consume() {
		_currentIndex++;
	}

	/**
	 * Checks whether given text is a prefix of remainded pattern, and moves
	 * the next char pointer to first character after this prefix.
	 * @param prefix text to match.
	 * @throws InvalidArgumentsPatternException
	 */
	private void match(String prefix) throws InvalidArgumentsPatternException {
		for (int i = 0; i < prefix.length(); i++) {
			assertArgumentsPatternIsValid(nextChar() == prefix.charAt(i));
			consume();
		}
	}

	private void assertArgumentsPatternIsValid(boolean condition) throws InvalidArgumentsPatternException {
		if (!condition) {
			throw new InvalidArgumentsPatternException("Invalid property pattern: " + _currentPattern);
		}
	}

	/**
	 * Checks whether end of pattern was reached and no more characters are left
	 * to analyze.
	 * @return
	 */
	private boolean isEndOfPattern() {
		return _currentIndex >= _currentPattern.length();
	}

}
