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
 * File: XMLBasedPropertyProvider.java
 * Created: 2010-06-23
 * Author: lukasik
 * $Id: XMLBasedPropertyProvider.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.property.xml;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jage.property.MetaPropertiesSet;
import org.jage.property.PropertyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to create XMLBased*MetaProperties for external classes using properly prepared XML file describing that
 * class.
 *
 * @author AGH AgE Team
 *
 * @since 2.4.0
 */
public class XMLBasedPropertyProvider {

	/** Holds MetaPropertiesSet for each class, that was already required. */
	private Map<Class<?>, MetaPropertiesSet> metaProperties = new HashMap<Class<?>, MetaPropertiesSet>();

	/** Logger. */
	protected static Logger log = LoggerFactory.getLogger(XMLBasedPropertyProvider.class);

	/** This class is singleton, so it has to have instance. */
	private static XMLBasedPropertyProvider instance = null;

	/**
	 * Gets the single instance of XMLBasedPropertyProvider.
	 *
	 * @return single instance of XMLBasedPropertyProvider
	 */
	public static XMLBasedPropertyProvider getInstance() {
		if (instance == null) {
			instance = new XMLBasedPropertyProvider();
		}
		return instance;
	}

	/**
	 * Gets the meta properties set for specified class. If this method wasn't invoked for that class, method will try
	 * to find appropriate xml file and create MetaPropertiesSet. If the method was invoked before, the
	 * MetaPropertiesSet for clazz already exists in map.
	 *
	 * @param clazz
	 *            the clazz, for which MetaPropertiesSet is required
	 *
	 * @return the MetaPropertiesSet for class clazz
	 *
	 * @throws PropertyException
	 *             the configuration exception
	 */
	public MetaPropertiesSet getMetaPropertiesSet(Class<?> clazz) throws PropertyException {
		MetaPropertiesSet set = metaProperties.get(clazz);
		if (set == null) {
			set = parseAttributes(clazz);
			metaProperties.put(clazz, set);
		}
		return set;
	}

	/**
	 * Gets the XML describing clazz as stream.
	 *
	 * @param clazz
	 *            the clazz
	 *
	 * @return A {@link InputStream} object or null if no XML for this class is found
	 */
	private InputStream getXMLStreamForClass(Class<?> clazz) {
		String resourceStr = clazz.getSimpleName() + ".contract.xml";
		return clazz.getResourceAsStream(resourceStr);
	}

	/**
	 * Checks if clazz can be adapted to {@link XMLBasedPropertyContainer}.
	 *
	 * @param clazz
	 *            the class to be adapted
	 *
	 * @return true, if is adaptable
	 */
	public boolean isAdaptable(Class<?> clazz) {
		return getXMLStreamForClass(clazz) != null;
	}

	/**
	 * Parses the XML file with a contract assigned to the clazz class and retrieves a set of properties basing on the
	 * attributes specified in contract file.
	 *
	 * @param clazz
	 *            the clazz, for which the MetaProperiesSet is created
	 *
	 * @return the MetaPropertiesSet for the clazz
	 *
	 * @throws PropertyException
	 *             the configuration exception
	 */
	@SuppressWarnings("unchecked")
	private MetaPropertiesSet parseAttributes(Class<?> clazz) throws PropertyException {
		try {
			InputStream in = getXMLStreamForClass(clazz);
			if (in == null) {
				throw new FileNotFoundException("XML defining class" + clazz.getName() + " not found in classpath");
			}

			SAXReader reader = new SAXReader();
			Document xmlDocument = reader.read(in);
			Element root = xmlDocument.getRootElement();
			String classFromXML = root.attributeValue("class");
			if (!classFromXML.equals(clazz.getName())) {
				throw new PropertyException("Expected class (" + clazz.getName()
				        + ") differs from the one specified in xml (" + classFromXML + ")!");
			}
			List<Element> extendList = root.elements("extends");
			MetaPropertiesSet propertiesSet;
			if (extendList.size() > 1) {
				throw new PropertyException("[" + clazz.getName() + "] There can be only one parent class.");
			} else if (extendList.size() == 1) {
				Element parentElement = extendList.get(0);
				String parentClassName = parentElement.attributeValue("class");
				Class<?> parentClass = Class.forName(parentClassName);
				propertiesSet = parseAttributes(parentClass);
			} else {
				propertiesSet = new MetaPropertiesSet();
			}

			List<Element> attributes = root.element("declaration").elements("attribute");
			for (Element attribute : attributes) {
				String name = attribute.attributeValue("name");
				String access = attribute.attributeValue("access");
				if (propertiesSet.hasMetaProperty(name)) {
					propertiesSet.removeMetaProperty(propertiesSet.getMetaProperty(name));
				}
				if (access.equals("setter")) {
					propertiesSet.addMetaProperty(createXMLGetterSetterMetaProperty(clazz, attribute));
				} else if (access.equals("direct")) {
					propertiesSet.addMetaProperty(createXMLFieldMetaProperty(clazz, attribute));
				} else {
					throw new PropertyException("Error parsing xml configuration for class: " + clazz.getName() + "\n");
				}
			}
			return propertiesSet;
		} catch (FileNotFoundException e) {
			log.error(e.getLocalizedMessage());
			throw new PropertyException(e);
		} catch (ClassNotFoundException e) {
			log.error("Parent class for " + clazz.getName() + " not found. " + e.getLocalizedMessage());
			throw new PropertyException(e);
		} catch (Exception e) {
			log.error(clazz.getName() + " " + e.getLocalizedMessage());
			throw new PropertyException("Error parsing xml configuration for class: " + clazz.getName() + "\n"
			        + e.getMessage(), e);
		}

	}

	/**
	 * Creates the XMLBasedFieldMetaProperty basing on the information in attribute element and it's children.
	 *
	 * @param clazz
	 *            the clazz
	 * @param attribute
	 *            the attribute
	 *
	 * @return the XMLBasedFieldMetaProperty created with data gained from attribute
	 *
	 * @throws SecurityException
	 *             the security exception
	 * @throws NoSuchFieldException
	 *             the no such field exception
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 * @throws PropertyException
	 *             the configuration exception
	 */
	private XMLBasedFieldMetaProperty createXMLFieldMetaProperty(Class<?> clazz, Element attribute)
	        throws SecurityException, NoSuchFieldException, ClassNotFoundException, PropertyException {
		String propertyName = attribute.attributeValue("name");
		Element typeElement = attribute.element("type");
		String fieldName = attribute.attributeValue("fieldName");
		if (fieldName == null) {
			fieldName = propertyName;
		}
		Field propertyField;
		try {
			propertyField = clazz.getDeclaredField(fieldName);
		} catch (Exception e) {
			log.debug("No field found for property: " + clazz.getName() + "::" + propertyName, e);
			throw new PropertyException(
			        "No field could be found for property " + clazz.getName() + "::" + propertyName, e);
		}
		Class<?> propertyClass = getAttributeClass(typeElement);
		if (!propertyField.getType().isAssignableFrom(propertyClass)) {
			throw new PropertyException("Field type for " + clazz.getName() + "::" + fieldName
			        + " declared in XML doesn't match the real one");
		}
		return new XMLBasedFieldMetaProperty(propertyName, propertyField);
	}

	/**
	 * Creates the XMLBasedGetterSetterMetaProperty basing on the information in attribute element and it's children.
	 *
	 * @param clazz
	 *            the clazz
	 * @param attribute
	 *            the attribute
	 *
	 * @return the XMLBasedGetterSetterMetaProperty created with data gained from attribute
	 *
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 * @throws PropertyException
	 *             the configuration exception
	 */
	private XMLBasedGetterSetterMetaProperty createXMLGetterSetterMetaProperty(Class<?> clazz, Element attribute)
	        throws ClassNotFoundException, PropertyException {

		String propertyName = attribute.attributeValue("name");
		String setterMethodName = attribute.attributeValue("setter");
		String getterMethodName = attribute.attributeValue("getter");
		Method setterMethod = null;
		Method getterMethod = null;
		Element typeElement = attribute.element("type");
		Class<?> propertyClass = getAttributeClass(typeElement);
		if (setterMethodName == null || setterMethodName.isEmpty()) {
			setterMethodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
		}
		try {
			setterMethod = clazz.getMethod(setterMethodName, propertyClass);
		} catch (Exception e) {
			log.debug("No setter metod found for property: " + clazz.getName() + "::" + propertyName, e);
			log.debug("Setting property" + clazz.getName() + "::" + propertyName + "as non-writeable");
		}
		if (getterMethodName == null || getterMethodName.isEmpty()) {
			getterMethodName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
		}
		try {
			getterMethod = clazz.getMethod(getterMethodName, new Class[0]);
		} catch (Exception e) {
			log.debug("No setter metod found for property: " + clazz.getName() + "::" + propertyName, e);
			throw new PropertyException("No getter method could be found for property " + clazz.getName() + "::"
			        + propertyName, e);
		}
		if(setterMethod != null) {
			return new XMLBasedGetterSetterMetaProperty(propertyName, propertyClass, getterMethod, setterMethod);
		} else {
			return new XMLBasedGetterSetterMetaProperty(propertyName, propertyClass, getterMethod);
		}
	}

	/**
	 * Gets type of property basing on the information in typeElement
	 *
	 * @param typeElement
	 *            the type element
	 *
	 * @return type of property
	 *
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 * @throws PropertyException
	 *             the configuration exception
	 */
	private Class<?> getAttributeClass(Element typeElement) throws ClassNotFoundException, PropertyException {
		Element classElement = (Element)typeElement.elements().get(0);
		String type = classElement.getName();
		if (type.equals("primitive")) {
			return PrimitiveTypeProvider.getPrimitiveType(classElement.attributeValue("name"));
		} else if (type.equals("simpleClass") || type.equals("parameterized")) {
			return Class.forName(classElement.attributeValue("class"));
		} else if (type.equals("array")) {
			int dims = Integer.parseInt(classElement.attributeValue("dims"));
			Class<?> componentClass = getAttributeClass(classElement.element("type"));
			return Array.newInstance(componentClass, (int[])Array.newInstance(int.class, dims)).getClass();
		} else {
			throw new PropertyException("Wrong type element present in XML file");
		}
	}

}
