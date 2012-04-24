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
 * File: SimpleTypeParsers.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: SimpleTypeParsers.java 175 2012-03-31 17:21:19Z krzywick $
 */

package org.jage.platform.component.definition;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Converter for parsing strings into value classes.
 *
 * @author AGH AgE Team
 */
public final class SimpleTypeParsers {

	private static final Map<Class<?>, SimpleTypeParser<?>> parsers;
	static {
		Map<Class<?>, SimpleTypeParser<?>> map = new HashMap<Class<?>, SimpleTypeParser<?>>();
		map.put(String.class, new StringParser());
		map.put(Integer.class, new IntegerParser());
		map.put(int.class, new IntegerParser());
		map.put(Long.class, new LongParser());
		map.put(long.class, new LongParser());
		map.put(Short.class, new ShortParser());
		map.put(short.class, new ShortParser());
		map.put(Byte.class, new ByteParser());
		map.put(byte.class, new ByteParser());
		map.put(Float.class, new FloatParser());
		map.put(float.class, new FloatParser());
		map.put(Double.class, new DoubleParser());
		map.put(double.class, new DoubleParser());
		map.put(Boolean.class, new BooleanParser());
		map.put(boolean.class, new BooleanParser());
		map.put(Class.class, new ClassParser());
		// XXX temporary turned off vide AGE-48
		// map.put(IAgentAddress.class, new AgentAddressParser());
		// map.put(AgentAddress.class, new AgentAddressParser());
		parsers = Collections.unmodifiableMap(map);
	}

	/**
	 * Parses a given string value to produce a value of the desired class.
	 *
	 * @param stringValue a string value to be parsed
	 * @param desiredClass the class to be looked for when parsing
	 * @return A value of the desired class
	 * @throws ConfigurationException if the desired class is not supported or a given string cannot be parsed.
	 */
	public static <T> T parse(final String stringValue, final Class<T> desiredClass) throws ConfigurationException {
		// We can ignore the warning as we are sure that classes and parsers are correctly mapped
		@SuppressWarnings("unchecked")
		SimpleTypeParser<T> parser = (SimpleTypeParser<T>)parsers.get(desiredClass);
		if (parser == null) {
			throw new ConfigurationException("Unsupported type of property: " + desiredClass);
		}
		return parser.parse(stringValue);
	}

	private abstract static class SimpleTypeParser<T> {
		protected static final String ERROR_MESSAGE = "Could not parse value %s";

		abstract public T parse(String value) throws ConfigurationException;
	}

	private static class StringParser extends SimpleTypeParser<String> {

		@Override
		public String parse(final String value) {
			return value;
		}
	}

	private static class IntegerParser extends SimpleTypeParser<Integer> {

		@Override
		public Integer parse(final String value) throws ConfigurationException {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException e) {
				throw new ConfigurationException(String.format(ERROR_MESSAGE, value), e);
			}
		}
	}

	private static class LongParser extends SimpleTypeParser<Long> {

		@Override
		public Long parse(final String value) throws ConfigurationException {
			try {
				return Long.parseLong(value);
			} catch (NumberFormatException e) {
				throw new ConfigurationException(String.format(ERROR_MESSAGE, value), e);
			}
		}
	}

	private static class ShortParser extends SimpleTypeParser<Short> {

		@Override
		public Short parse(final String value) throws ConfigurationException {
			try {
				return Short.parseShort(value);
			} catch (NumberFormatException e) {
				throw new ConfigurationException(String.format(ERROR_MESSAGE, value), e);
			}
		}
	}

	private static class ByteParser extends SimpleTypeParser<Byte> {

		@Override
		public Byte parse(final String value) throws ConfigurationException {
			try {
				return Byte.parseByte(value);
			} catch (NumberFormatException e) {
				throw new ConfigurationException(String.format(ERROR_MESSAGE, value), e);
			}
		}
	}

	private static class FloatParser extends SimpleTypeParser<Float> {

		@Override
		public Float parse(final String value) throws ConfigurationException {
			try {
				return Float.parseFloat(value);
			} catch (NumberFormatException e) {
				throw new ConfigurationException(String.format(ERROR_MESSAGE, value), e);
			}
		}
	}

	private static class DoubleParser extends SimpleTypeParser<Double> {

		@Override
		public Double parse(final String value) throws ConfigurationException {
			try {
				return Double.parseDouble(value);
			} catch (NumberFormatException e) {
				throw new ConfigurationException(String.format(ERROR_MESSAGE, value), e);
			}
		}
	}

	private static class BooleanParser extends SimpleTypeParser<Boolean> {

		@Override
		public Boolean parse(final String value) {
			return Boolean.parseBoolean(value);
		}
	}

	private static class ClassParser extends SimpleTypeParser<Class<?>> {

		@Override
		public Class<?> parse(final String value) throws ConfigurationException {
			try {
				return Class.forName(value);
			} catch (ClassNotFoundException e) {
				throw new ConfigurationException(String.format(ERROR_MESSAGE, value), e);
			}
		}
	}

	// XXX temporary turned off vide AGE-48
	// private static class AgentAddressParser extends ISimpleTypeParser {

	// @Override
	// public Object parse(String value) {
	// return new AgentAddress(value);
	// }
	// }

	// END Simple type parsers
}
