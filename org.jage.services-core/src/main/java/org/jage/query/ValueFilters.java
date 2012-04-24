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
 * File: ValueFilters.java
 * Created: 2011-09-12
 * Author: faber
 * $Id: ValueFilters.java 213 2012-04-08 20:21:35Z krzywick $
 */

package org.jage.query;

import java.lang.reflect.Method;
import java.util.Map.Entry;

import org.jage.property.Property;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Common implementations of {@link IValueFilter}.
 *
 * @author AGH AgE Team
 */
public final class ValueFilters {
	/**
	 * Creates a filter that matches a string against a regular expression.
	 *
	 * @param pattern
	 *            A regular expression to test, cannot be <code>null</code>.
	 * @return A new value filter.
	 */
	public static IValueFilter<String> pattern(final String pattern) {
		checkNotNull(pattern, "Pattern cannot be null.");

		return new IValueFilter<String>() {
			@Override
			public boolean matches(String obj) {
				return obj.matches(pattern);
			}
		};
	}

	/**
	 * Creates a filter that matches an object equals to a value.
	 *
	 * @param value
	 *            A value for equality test.
	 * @param <T>
	 *            A type of the value to compare.
	 * @return A new value filter.
	 */
	public static <T> IValueFilter<T> eq(final T value) {
		return new IValueFilter<T>() {
			@Override
			public boolean matches(T obj) {
				return (value == null && obj == null) || (obj != null && obj.equals(value));
			}
		};
	}

	/**
	 * 
	 * Creates a filter that negates another filter.
	 * 
	 * @param filter
	 *            A filter to be negated.
	 * @param <T>
	 *            A type of the value to compare.
	 * @return A new value filter.
	 */
	public static <T> IValueFilter<T> not(final IValueFilter<T> filter) {
		return new IValueFilter<T>() {
			@Override
			public boolean matches(T obj) {
				return !filter.matches(obj);
			}
		};
	}

	/**
	 * Creates a filter that matches an object with value less than the provided value.
	 *
	 * @param value
	 *            A value to compare against, not <code>null</code>.
	 * @param <T>
	 *            A type of the value to compare.
	 * @return A new value filter.
	 */
	public static <T extends Comparable<T>> IValueFilter<T> lessThan(final T value) {
		checkNotNull(value, "Value cannot be null.");

		return new IValueFilter<T>() {
			@Override
			public boolean matches(T obj) {
				return value.compareTo(obj) > 0;
			}
		};
	}

	/**
	 * Creates a filter that matches an object with value less than or equal to the provided value.
	 *
	 * @param value
	 *            A value to compare against, not <code>null</code>.
	 * @param <T>
	 *            A type of the value to compare.
	 * @return A new value filter.
	 */
	public static <T extends Comparable<T>> IValueFilter<T> lessOrEqual(final T value) {
		checkNotNull(value, "Value cannot be null.");

		return new IValueFilter<T>() {
			@Override
			public boolean matches(T obj) {
				return value.compareTo(obj) >= 0;
			}
		};
	}

	/**
	 * Creates a filter that matches an object with value greater than the provided value.
	 *
	 * @param value
	 *            A value to compare against, not <code>null</code>.
	 * @param <T>
	 *            A type of the value to compare.
	 * @return A new value filter.
	 */
	public static <T extends Comparable<T>> IValueFilter<T> moreThan(final T value) {
		checkNotNull(value, "Value cannot be null.");

		return new IValueFilter<T>() {
			@Override
			public boolean matches(T obj) {
				return value.compareTo(obj) < 0;
			}
		};
	}

	/**
	 * Creates a filter that matches an object with value greater than or equal to the provided value.
	 *
	 * @param value
	 *            A value to compare against, not <code>null</code>.
	 * @param <T>
	 *            A type of the value to compare.
	 * @return A new value filter.
	 */
	public static <T extends Comparable<T>> IValueFilter<T> moreOrEqual(final T value) {
		checkNotNull(value, "Value cannot be null.");

		return new IValueFilter<T>() {
			@Override
			public boolean matches(T obj) {
				return value.compareTo(obj) <= 0;
			}
		};
	}

	/**
	 * Creates a filter that matches if any (at least one) of provided filters matches. This implementation
	 * short-circuits (tests to the first match).
	 *
	 * @param filters
	 *            Filters to test.
	 * @param <T>
	 *            A generic type of used filters.
	 * @return A new value filter.
	 */
	public static <T> IValueFilter<T> anyOf(final IValueFilter<? extends T>... filters) {
		return new IValueFilter<T>() {
			@SuppressWarnings("unchecked")
			@Override
			public boolean matches(T obj) {
				for (IValueFilter<T> valueFilter : (IValueFilter<T>[])filters) {
					if (valueFilter.matches(obj)) {
						return true;
					}
				}
				return false;
			}
		};
	}

	/**
	 * Creates a filter that matches if all of provided filters matches. This implementation short-circuits (tests to
	 * the first failure).
	 *
	 * @param filters
	 *            Filters to test.
	 * @param <T>
	 *            A generic type of used filters.
	 * @return A new value filter.
	 */
	public static <T> IValueFilter<T> allOf(final IValueFilter<? extends T>... filters) {
		return new IValueFilter<T>() {
			@SuppressWarnings("unchecked")
			@Override
			public boolean matches(T obj) {
				for (IValueFilter<T> valueFilter : (IValueFilter<T>[])filters) {
					if (!valueFilter.matches(obj)) {
						return false;
					}
				}
				return true;
			}
		};
	}

	/**
	 * Creates a filter that matches a field of the Java Bean with the provided filter. This implementation uses
	 * reflection.
	 *
	 * @param fieldName
	 *            A field to use.
	 * @param filter
	 *            A filter to test.
	 * @param <T>
	 *            A generic type of the checked object.
	 * @param <S>
	 *            A generic type of the field to test.
	 * @return A new value filter.
	 */
	public static <T, S> IValueFilter<T> fieldValue(final String fieldName, final IValueFilter<S> filter) {
		return new IValueFilter<T>() {
			@SuppressWarnings("unchecked")
			@Override
			public boolean matches(T obj) {
				try {
					String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
					Method method = obj.getClass().getMethod(methodName, (Class<?>[])null);
					S value = (S)method.invoke(obj);
					return filter.matches(value);

				} catch (Exception e) {
					throw new QueryException(e);
				}
			}

		};
	}

	/**
	 * Creates a filter that matches any value.
	 *
	 * @param klass
	 *            A type of tested objects.
	 * @param <T>
	 *            A generic type of the used class.
	 * @return A new value filter.
	 */
	public static <T> IValueFilter<T> any(Class<T> klass) {
		return new IValueFilter<T>() {
			@Override
			public boolean matches(T obj) {
				return true;
			}

		};

	}

	/**
	 * Creates a filter that matches any value.
	 *
	 * @param <T>
	 *            A generic type of the used class.
	 * @return A new value filter.
	 */
	public static <T> IValueFilter<T> any() {
		return new IValueFilter<T>() {
			@Override
			public boolean matches(T obj) {
				return true;
			}

		};

	}

	/**
	 * Creates a map filter that tests a provided filter against a key in a map.
	 *
	 * @param filter
	 *            A filter to use.
	 * @param <K>
	 *            A type of keys.
	 * @return A new value filter.
	 */
	public static <K> IValueFilter<Entry<K, ?>> key(final IValueFilter<K> filter) {
		return new IValueFilter<Entry<K, ?>>() {
			@Override
			public boolean matches(Entry<K, ?> obj) {
				return filter.matches(obj.getKey());
			}

		};
	}

	/**
	 * Creates a map filter that tests a provided filter against a value in a map.
	 *
	 * @param filter
	 *            A filter to use.
	 * @param <V>
	 *            A type of values.
	 * @return A new value filter.
	 */
	public static <V> IValueFilter<Entry<?, V>> value(final IValueFilter<V> filter) {
		return new IValueFilter<Entry<?, V>>() {
			@Override
			public boolean matches(Entry<?, V> obj) {
				return filter.matches(obj.getValue());
			}

		};
	}

	/**
	 * Creates a map filter that tests provided filters against an entry in a map.
	 *
	 * @param keyFilter
	 *            A filter to use against the key.
	 * @param valueFilter
	 *            A filter to use against the value.
	 * @param <V>
	 *            A type of values.
	 * @param <K>
	 *            A type of keys.
	 * @return A new value filter.
	 */
	public static <K, V> IValueFilter<Entry<K, V>> entry(final IValueFilter<K> keyFilter,
	        final IValueFilter<V> valueFilter) {

		return new IValueFilter<Entry<K, V>>() {
			@Override
			public boolean matches(Entry<K, V> obj) {
				return keyFilter.matches(obj.getKey()) && valueFilter.matches(obj.getValue());
			}

		};
	}

	/**
	 * Creates a map filter that tests a provided filter against a {@link Property} name.
	 *
	 * @param filter
	 *            A filter to use.
	 * @return A new value filter.
	 */
	public static IValueFilter<Property> name(final IValueFilter<String> filter) {
		return new IValueFilter<Property>() {

			@Override
			public boolean matches(Property obj) {
				return filter.matches(obj.getMetaProperty().getName());
			}

		};
	}

	/**
	 * Creates a map filter that tests a provided filter against a {@link Property} value.
	 *
	 * @param filter
	 *            A filter to use.
	 * @param <V>
	 *            A type of property value.
	 * @return A new value filter.
	 */
	public static <V> IValueFilter<Property> propertyValue(final IValueFilter<V> filter) {
		return new IValueFilter<Property>() {

			@SuppressWarnings("unchecked")
			@Override
			public boolean matches(Property property) {
				Object obj = property.getValue();
				return filter.matches((V)obj);
			}

		};
	}

	private ValueFilters() {
		// Empty
	}
}
