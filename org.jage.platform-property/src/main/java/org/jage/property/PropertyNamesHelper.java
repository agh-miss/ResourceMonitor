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

import java.util.HashSet;

/**
 * Class with static methods that allows to validate property names.
 * @author Tomek
 */
public final class PropertyNamesHelper {
	
	private static final HashSet<Character> _reservedCharacters = new HashSet<Character>();
	
	/**
	 * Static constructor.
	 */
	static {
		char[] characters = new char[] { '.', ',', '[', ']', '*', '@' };
		for (char character : characters) {
			_reservedCharacters.add(new Character(character));
		}
	}
	
	/**
	 * Checks whether given string is valid property name.
	 * @param name property name to check.
	 * @return true, if given string is valid property name; otherwise, returns
	 * false.
	 */
	public static boolean isValidPropertyName(String name) {
		for (int i = 0; i < name.length(); i++) {
			if (_reservedCharacters.contains(new Character(name.charAt(i)))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Checks whether given character is valid for property name. 
	 * @param c character to check.
	 * @return true, is given character is valid for property name; otherwise, returns false.
	 */
	public static boolean isValidPropertyNameCharacter(char c) {
		return !_reservedCharacters.contains(new Character(c));
	}
}
