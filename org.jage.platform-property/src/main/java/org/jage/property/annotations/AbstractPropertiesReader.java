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

package org.jage.property.annotations;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jage.property.FieldMetaProperty;
import org.jage.property.GetterSetterMetaProperty;
import org.jage.property.InvalidPropertyDefinitionException;
import org.jage.property.PropertyException;
import org.jage.property.PropertyNamesHelper;

public abstract class AbstractPropertiesReader implements IPropertiesReader {

	@Override
	public final Set<FieldMetaProperty> readFieldMetaProperties(Class<?> clazz) throws InvalidPropertyDefinitionException, PropertyException {
		Map<String, FieldMetaProperty> fields = new HashMap<String, FieldMetaProperty>();
		Validator validator = new Validator(clazz, fields);

		for (Field field : getAllFields(clazz)) {
			String propertyName = getFieldPropertyName(field);
			if (propertyName != null) {
				validator.validateFieldDefinition(propertyName);
				fields.put(propertyName, createFieldMetaProperty(field));
			}
		}

		return new HashSet<FieldMetaProperty>(fields.values());
	}

	@Override
	public final Set<GetterSetterMetaProperty> readGetterSetterMetaProperties(Class<?> clazz)
	        throws InvalidPropertyDefinitionException, PropertyException {
		Map<String, Method> getters = new HashMap<String, Method>();
		Map<String, Method> setters = new HashMap<String, Method>();
		Validator validator = new Validator(clazz, getters, setters);

		for (Method method : getAllMethods(clazz)) {
			String getterPropertyName = getGetterPropertyName(method);
			if (getterPropertyName != null) {
				validator.validateGetterDefinition(getterPropertyName, method);
				getters.put(getterPropertyName, method);
			}
			String setterPropertyName = getSetterPropertyName(method);
			if (setterPropertyName != null) {
				validator.validateSetterDefinition(setterPropertyName, method);
				setters.put(setterPropertyName, method);
			}
		}

		Set<GetterSetterMetaProperty> metaProperties = new HashSet<GetterSetterMetaProperty>();

		HashSet<String> pairs = new HashSet<String>(getters.keySet());
		pairs.retainAll(setters.keySet());
		for(String propertyName: pairs) {
			Method getter = getters.remove(propertyName);
			Method setter = setters.remove(propertyName);

			validator.validateGetterSetterPair(propertyName, getter, setter);
			metaProperties.add(createGetterSetterMetaProperty(propertyName, getter, setter));
		}

		for(String propertyName: getters.keySet()) {
			Method getter = getters.get(propertyName);
			metaProperties.add(createGetterMetaProperty(propertyName, getter));
		}

		for(String propertyName: setters.keySet()) {
			Method setter = setters.get(propertyName);
			metaProperties.add(createSetterMetaProperty(propertyName, setter));
		}

		return metaProperties;
	}

	// These methods are a stub for some future abstract/subclassing. They are the only ones with a dependency to actual
	// annotations

	// The former 3 are needed, as we do not have one consistent way of getting the property name of an annotation.
	// They are supposed to return null if a member does not represent a property.

	abstract protected String getFieldPropertyName(Field field);

	abstract protected String getGetterPropertyName(Method method);

	abstract protected String getSetterPropertyName(Method method);

	// The latter 4 are template methods for creating the actual MetaProperty

	abstract protected FieldMetaProperty createFieldMetaProperty(Field field) throws PropertyException;

	abstract protected GetterSetterMetaProperty createGetterSetterMetaProperty(String propertyName, Method getter, Method setter) throws PropertyException;

	abstract protected GetterSetterMetaProperty createGetterMetaProperty(String propertyName, Method getter) throws PropertyException;

	abstract protected GetterSetterMetaProperty createSetterMetaProperty(String propertyName, Method setter) throws PropertyException;

	// ----------------------

	private Set<Field> getAllFields(Class<?> clazz) {
		Set<Field> fields = new HashSet<Field>();

		while (clazz != null) {
			fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
			clazz = clazz.getSuperclass();
		}

		return fields;
	}

	private Set<Method> getAllMethods(Class<?> clazz) {
		Set<Method> methods = new HashSet<Method>();

		while (clazz != null) {
			methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
			clazz = clazz.getSuperclass();
		}

		return methods;
	}

	private class Validator {

		private Class<?> clazz;

		private Map<String, FieldMetaProperty> fields;

		private Map<String, Method> getters;

		private Map<String, Method> setters;

		public Validator(Class<?> clazz, Map<String, FieldMetaProperty> fields) {
			this.clazz = clazz;
			this.fields = fields;
		}

		public Validator(Class<?> clazz, Map<String, Method> getters, Map<String, Method> setters) {
			this.clazz = clazz;
			this.getters = getters;
			this.setters = setters;
		}

		public void validatePropertyName(String propertyName) throws InvalidPropertyDefinitionException {
			if (!PropertyNamesHelper.isValidPropertyName(propertyName)) {
				throwExceptionWithMessage(propertyName, "Invalid property name.");
			}
		}

		public void validateFieldDefinition(String propertyName) throws InvalidPropertyDefinitionException {
			validatePropertyName(propertyName);
			if (fields.containsKey(propertyName)) {
				throwExceptionWithMessage(propertyName, "Duplicate field.");
			}
		}

		// TODO: Enforce javabeans naming convention
		public void validateGetterDefinition(String propertyName, Method method)
		        throws InvalidPropertyDefinitionException {
			validatePropertyName(propertyName);
			if (getters.containsKey(propertyName)) {
				throwExceptionWithMessage(propertyName, "Duplicate getter.");
			}
			if (method.getReturnType() == void.class) {
				throwExceptionWithMessage(propertyName, "A getter must return a value.");
			}
			if (method.getParameterTypes().length != 0) {
				throwExceptionWithMessage(propertyName, "A getter can't have any parameters.");
			}
		}

		// TODO: Enforce javabeans naming convention
		public void validateSetterDefinition(String propertyName, Method method)
		        throws InvalidPropertyDefinitionException {
			validatePropertyName(propertyName);
			if (setters.containsKey(propertyName)) {
				throwExceptionWithMessage(propertyName, "Duplicate setter.");
			}
			if (method.getReturnType() != void.class) {
				throwExceptionWithMessage(propertyName, "A setter can't return a value.");
			}
			if (method.getParameterTypes().length != 1) {
				throwExceptionWithMessage(propertyName, "A getter must have exactly one parameter.");
			}
		}

		public void validateGetterSetterPair(String propertyName, Method getter, Method setter)
		        throws InvalidPropertyDefinitionException {
			if (getter.getReturnType() != setter.getParameterTypes()[0]) {
				throwExceptionWithMessage("Getter and setter have incompatible types");
			}
		}

		private void throwExceptionWithMessage(String message) throws InvalidPropertyDefinitionException {
			throw new InvalidPropertyDefinitionException(String.format("In class %1$s: %2$s", clazz, message));
		}

		private void throwExceptionWithMessage(String propertyName, String message)
		        throws InvalidPropertyDefinitionException {
			throwExceptionWithMessage(String.format("Property %1$s: %2$s", propertyName, message));
		}
	}

}
