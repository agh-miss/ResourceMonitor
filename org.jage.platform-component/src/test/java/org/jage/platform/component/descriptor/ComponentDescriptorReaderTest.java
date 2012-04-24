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
 * File: ComponentDescriptorReaderTest.java
 * Created: 2010-03-10
 * Author: kpietak
 * $Id: ComponentDescriptorReaderTest.java 183 2012-03-31 20:33:32Z krzywick $
 */

package org.jage.platform.component.descriptor;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.component.sample.ComponentWithConstructorsOnly;
import org.jage.platform.component.sample.EmptyComponent;
import org.jage.platform.component.sample.EmptyComponent2;
import org.jage.platform.component.sample.ExtendedComponentWithConstructors;
import org.jage.platform.component.sample.ExtendedComponentWithConstructorsAndFieldProperties;

@Ignore
public class ComponentDescriptorReaderTest {

	IComponentDescriptorReader reader;

	@Before
	public void setUp() throws Exception {
		reader = new ComponentDescriptorReader();
	}

	@Test
	public void testComponentWithConstructorsOnly() throws IllegalArgumentException, ConfigurationException {
		IComponentDescriptor descriptor = reader.readDescritptor(ComponentWithConstructorsOnly.class);
		assertNotNull(descriptor);

		assertTrue(descriptor.getComponentType().equals(ComponentWithConstructorsOnly.class));

		// constructors
		Set<List<Class<?>>> expectedConstructors = new HashSet<List<Class<?>>>();
		List<Class<?>> tempList = new LinkedList<Class<?>>();
		expectedConstructors.add(tempList);
		tempList = new LinkedList<Class<?>>();
		tempList.add(String.class);
		expectedConstructors.add(tempList);

		List<List<Class<?>>> constructors = descriptor.getConstructorParametersTypes();
		Set<List<Class<?>>> descriptorConstructors = new HashSet<List<Class<?>>>(constructors);

		assertTrue(expectedConstructors.equals(descriptorConstructors));
		//

		// properties
//		assertTrue(descriptor.getOptionalProperties().isEmpty());
//		assertTrue(descriptor.getProperties().isEmpty());
//		assertTrue(descriptor.getRequriedProperties().isEmpty());
		//
	}

	@Test
	public void testEmptyComponent() throws IllegalArgumentException, ConfigurationException {
		IComponentDescriptor descriptor = reader.readDescritptor(EmptyComponent.class);
		assertNotNull(descriptor);

		assertTrue(descriptor.getComponentType().equals(EmptyComponent.class));

		// constructors
		Set<List<Class<?>>> expectedConstructors = new HashSet<List<Class<?>>>();
		List<Class<?>> tempList = new LinkedList<Class<?>>();
		expectedConstructors.add(tempList);

		List<List<Class<?>>> constructors = descriptor.getConstructorParametersTypes();
		Set<List<Class<?>>> descriptorConstructors = new HashSet<List<Class<?>>>(constructors);

		assertTrue(expectedConstructors.equals(descriptorConstructors));
		//

		// properties
//		assertTrue(descriptor.getOptionalProperties().isEmpty());
//		assertTrue(descriptor.getProperties().isEmpty());
//		assertTrue(descriptor.getRequriedProperties().isEmpty());
		//
	}

	@Test
	public void testEmptyComponent2() throws IllegalArgumentException, ConfigurationException {
		IComponentDescriptor descriptor = reader.readDescritptor(EmptyComponent2.class);
		assertNotNull(descriptor);

		assertTrue(descriptor.getComponentType().equals(EmptyComponent2.class));

		// constructors
		Set<List<Class<?>>> expectedConstructors = new HashSet<List<Class<?>>>();
		List<Class<?>> tempList = new LinkedList<Class<?>>();
		expectedConstructors.add(tempList);

		List<List<Class<?>>> constructors = descriptor.getConstructorParametersTypes();
		Set<List<Class<?>>> descriptorConstructors = new HashSet<List<Class<?>>>(constructors);

		assertTrue(expectedConstructors.equals(descriptorConstructors));
		//

		// properties
//		assertTrue(descriptor.getOptionalProperties().isEmpty());
//		assertTrue(descriptor.getProperties().isEmpty());
//		assertTrue(descriptor.getRequriedProperties().isEmpty());
		//
	}

	@Test
	public void testExtendedComponentWithConstructorsAndFieldProperties() throws IllegalArgumentException,
	        ConfigurationException {
		IComponentDescriptor descriptor = reader
		        .readDescritptor(ExtendedComponentWithConstructorsAndFieldProperties.class);
		assertNotNull(descriptor);

		assertTrue(descriptor.getComponentType().equals(ExtendedComponentWithConstructorsAndFieldProperties.class));

		// constructors
		Set<List<Class<?>>> expectedConstructors = new HashSet<List<Class<?>>>();
		List<Class<?>> tempList = new LinkedList<Class<?>>();
		tempList.add(String.class);
		expectedConstructors.add(tempList);

		List<List<Class<?>>> constructors = descriptor.getConstructorParametersTypes();
		Set<List<Class<?>>> descriptorConstructors = new HashSet<List<Class<?>>>(constructors);

		assertTrue(expectedConstructors.equals(descriptorConstructors));
		//

		// fields
//		Set<MetaProperty> componentProperties = new HashSet<MetaProperty>();
//		MetaProperty tempProperty = new MetaProperty("receiver", String.class);
//		componentProperties.add(tempProperty);
//		tempProperty = new MetaProperty("message", String.class);
//		componentProperties.add(tempProperty);
//
//		Collection<MetaProperty> expectedOptionalProperties = createBaseComponentOptionalProperties();
//		Collection<MetaProperty> expectedRequiredProperties = createBaseComponentRequriedProperties();
//		expectedRequiredProperties.addAll((componentProperties));
//		Collection<MetaProperty> expectedProperties = new HashSet<MetaProperty>(expectedOptionalProperties);
//		expectedProperties.addAll(expectedRequiredProperties);
//
//		Collection<MetaProperty> descriptorRequiredProperties = new HashSet<MetaProperty>(
//		        descriptor.getRequriedProperties());
//		Collection<MetaProperty> descriptorProperties = new HashSet<MetaProperty>(descriptor.getProperties());
//		Collection<MetaProperty> descriptorOptionalProperties = new HashSet<MetaProperty>(
//		        descriptor.getOptionalProperties());
//
//		assertTrue(compare(expectedProperties, descriptorProperties));
//		assertTrue(compare(expectedOptionalProperties, descriptorOptionalProperties));
//		assertTrue(compare(expectedRequiredProperties, descriptorRequiredProperties));

	}

	@Test
	public void testExtendedComponentWithConstructors() throws IllegalArgumentException, ConfigurationException {
		IComponentDescriptor descriptor = reader.readDescritptor(ExtendedComponentWithConstructors.class);
		assertNotNull(descriptor);

		assertTrue(descriptor.getComponentType().equals(ExtendedComponentWithConstructors.class));

		// constructors
		Set<List<Class<?>>> expectedConstructors = new HashSet<List<Class<?>>>();
		List<Class<?>> tempList = new LinkedList<Class<?>>();
		expectedConstructors.add(tempList);
		tempList = new LinkedList<Class<?>>();
		tempList.add(String.class);
		expectedConstructors.add(tempList);

		List<List<Class<?>>> constructors = descriptor.getConstructorParametersTypes();
		Set<List<Class<?>>> descriptorConstructors = new HashSet<List<Class<?>>>(constructors);

		assertTrue(expectedConstructors.equals(descriptorConstructors));

		// fields
//		Collection<MetaProperty> expectedOptionalProperties = createBaseComponentOptionalProperties();
//		Collection<MetaProperty> expectedRequiredProperties = createBaseComponentRequriedProperties();
//		Collection<MetaProperty> expectedProperties = new HashSet<MetaProperty>(expectedOptionalProperties);
//		expectedProperties.addAll(expectedRequiredProperties);
//
//		Collection<MetaProperty> descriptorProperties = new HashSet<MetaProperty>(descriptor.getProperties());
//		Collection<MetaProperty> descriptorRequiredProperties = new HashSet<MetaProperty>(
//		        descriptor.getRequriedProperties());
//		Collection<MetaProperty> descriptorOptionalProperties = new HashSet<MetaProperty>(
//		        descriptor.getOptionalProperties());
//
//		assertTrue(compare(expectedProperties, descriptorProperties));
//		assertTrue(compare(expectedOptionalProperties, descriptorOptionalProperties));
//		assertTrue(compare(expectedRequiredProperties, descriptorRequiredProperties));

	}

//	@Test
//	public void testExtendedComponentWithConstructorsAndProperty() throws IllegalArgumentException,
//	        ConfigurationException, PropertyException {
//		IComponentDescriptor descriptor = reader.readDescritptor(ExtendedComponentWithConstructorsAndProperty.class);
//		assertNotNull(descriptor);
//
//		assertTrue(descriptor.getComponentType().equals(ExtendedComponentWithConstructorsAndProperty.class));
//
//		// constructors
//		Set<List<Class<?>>> expectedConstructors = new HashSet<List<Class<?>>>();
//		List<Class<?>> tempList = new LinkedList<Class<?>>();
//		expectedConstructors.add(tempList);
//		tempList = new LinkedList<Class<?>>();
//		tempList.add(String.class);
//		expectedConstructors.add(tempList);
//
//		List<List<Class<?>>> constructors = descriptor.getConstructorParametersTypes();
//		Set<List<Class<?>>> descriptorConstructors = new HashSet<List<Class<?>>>(constructors);
//
//		assertTrue(expectedConstructors.equals(descriptorConstructors));
//		//
//
//		// fields
//		Set<MetaProperty> componentProperties = new HashSet<MetaProperty>();
//		MetaProperty tempProperty;
//		tempProperty = new MetaProperty("actor", String.class);
//		componentProperties.add(tempProperty);
//
//		// fields
//		Collection<MetaProperty> expectedOptionalProperties = createBaseComponentOptionalProperties();
//		Collection<MetaProperty> expectedRequiredProperties = createBaseComponentRequriedProperties();
//		expectedRequiredProperties.addAll(componentProperties);
//		Collection<MetaProperty> expectedProperties = new HashSet<MetaProperty>(expectedOptionalProperties);
//		expectedProperties.addAll(expectedRequiredProperties);
//
//		Collection<MetaProperty> descriptorProperties = new HashSet<MetaProperty>(descriptor.getProperties());
//		Collection<MetaProperty> descriptorRequiredProperties = new HashSet<MetaProperty>(
//		        descriptor.getRequriedProperties());
//		Collection<MetaProperty> descriptorOptionalProperties = new HashSet<MetaProperty>(
//		        descriptor.getOptionalProperties());
//
//		assertTrue(compare(expectedProperties, descriptorProperties));
//		assertTrue(compare(expectedOptionalProperties, descriptorOptionalProperties));
//		assertTrue(compare(expectedRequiredProperties, descriptorRequiredProperties));
//
//	}
//
//	@Test
//	public void testExtendedComponentWithConstructorsAndMethodsProperties() throws IllegalArgumentException,
//	        ConfigurationException, PropertyException {
//		IComponentDescriptor descriptor = reader
//		        .readDescritptor(ExtendedComponentWithConstructorsAndMethodsProperties.class);
//		assertNotNull(descriptor);
//
//		assertTrue(descriptor.getComponentType().equals(ExtendedComponentWithConstructorsAndMethodsProperties.class));
//
//		// constructors
//		Set<List<Class<?>>> expectedConstructors = new HashSet<List<Class<?>>>();
//		List<Class<?>> tempList = new LinkedList<Class<?>>();
//		expectedConstructors.add(tempList);
//		tempList = new LinkedList<Class<?>>();
//		tempList.add(String.class);
//		expectedConstructors.add(tempList);
//
//		List<List<Class<?>>> constructors = descriptor.getConstructorParametersTypes();
//		Set<List<Class<?>>> descriptorConstructors = new HashSet<List<Class<?>>>(constructors);
//
//		assertTrue(expectedConstructors.equals(descriptorConstructors));
//
//		// fields
//		Set<MetaProperty> componentProperties = new HashSet<MetaProperty>();
//		MetaProperty tempProperty;
//		tempProperty = new MetaProperty("echoStr", IEchoStrategy.class);
//		componentProperties.add(tempProperty);
//
//		Collection<MetaProperty> expectedOptionalProperties = createBaseComponentOptionalProperties();
//		Collection<MetaProperty> expectedRequiredProperties = createBaseComponentRequriedProperties();
//		expectedRequiredProperties.addAll(componentProperties);
//
//		Collection<MetaProperty> expectedProperties = new HashSet<MetaProperty>(expectedOptionalProperties);
//		expectedProperties.addAll(expectedRequiredProperties);
//
//		Collection<MetaProperty> descriptorProperties = new HashSet<MetaProperty>(descriptor.getProperties());
//		Collection<MetaProperty> descriptorRequiredProperties = new HashSet<MetaProperty>(
//		        descriptor.getRequriedProperties());
//		Collection<MetaProperty> descriptorOptionalProperties = new HashSet<MetaProperty>(
//		        descriptor.getOptionalProperties());
//
//		assertTrue(compare(expectedProperties, descriptorProperties));
//		assertTrue(compare(expectedOptionalProperties, descriptorOptionalProperties));
//		assertTrue(compare(expectedRequiredProperties, descriptorRequiredProperties));
//
//	}
//
//	@Test
//	public void testComponentWithMethodProperties() throws IllegalArgumentException, ConfigurationException,
//	        PropertyException {
//		IComponentDescriptor descriptor = reader.readDescritptor(ComponentWithMethodProperties.class);
//		assertNotNull(descriptor);
//
//		assertTrue(descriptor.getComponentType().equals(ComponentWithMethodProperties.class));
//
//		// constructors
//		Set<List<Class<?>>> expectedConstructors = new HashSet<List<Class<?>>>();
//		List<Class<?>> tempList = new LinkedList<Class<?>>();
//		expectedConstructors.add(tempList);
//
//		List<List<Class<?>>> constructors = descriptor.getConstructorParametersTypes();
//		Set<List<Class<?>>> descriptorConstructors = new HashSet<List<Class<?>>>(constructors);
//
//		assertTrue(expectedConstructors.equals(descriptorConstructors));
//		//
//
//		// fields
//		Set<MetaProperty> componentProperties = new HashSet<MetaProperty>();
//		MetaProperty tempProperty;
//		tempProperty = new MetaProperty("increment", int.class);
//		componentProperties.add(tempProperty);
//		// fields
//		Collection<MetaProperty> expectedOptionalProperties = new HashSet<MetaProperty>();
//		Collection<MetaProperty> expectedRequiredProperties = componentProperties;
//
//		Collection<MetaProperty> expectedProperties = new HashSet<MetaProperty>(expectedOptionalProperties);
//		expectedProperties.addAll(expectedRequiredProperties);
//
//		Collection<MetaProperty> descriptorProperties = new HashSet<MetaProperty>(descriptor.getProperties());
//		Collection<MetaProperty> descriptorRequiredProperties = new HashSet<MetaProperty>(
//		        descriptor.getRequriedProperties());
//		Collection<MetaProperty> descriptorOptionalProperties = new HashSet<MetaProperty>(
//		        descriptor.getOptionalProperties());
//
//		assertTrue(compare(expectedProperties, descriptorProperties));
//		assertTrue(compare(expectedOptionalProperties, descriptorOptionalProperties));
//		assertTrue(compare(expectedRequiredProperties, descriptorRequiredProperties));
//
//	}
//
//	@Test
//	public void testComponentWithFieldProperties() throws IllegalArgumentException, ConfigurationException,
//	        PropertyException, SecurityException, NoSuchFieldException {
//		IComponentDescriptor descriptor = reader.readDescritptor(ComponentWithFieldProperties.class);
//		assertNotNull(descriptor);
//
//		assertTrue(descriptor.getComponentType().equals(ComponentWithFieldProperties.class));
//
//		// constructors
//		Set<List<Class<?>>> expectedConstructors = new HashSet<List<Class<?>>>();
//		List<Class<?>> tempList = new LinkedList<Class<?>>();
//		expectedConstructors.add(tempList);
//
//		List<List<Class<?>>> constructors = descriptor.getConstructorParametersTypes();
//		Set<List<Class<?>>> descriptorConstructors = new HashSet<List<Class<?>>>(constructors);
//
//		assertTrue(expectedConstructors.equals(descriptorConstructors));
//		//
//
//		// fields
//		Set<MetaProperty> expectedProperties = new HashSet<MetaProperty>();
//		MetaProperty tempProperty = new MetaProperty("receiver", String.class);
//		expectedProperties.add(tempProperty);
//		Field field = ComponentWithFieldProperties.class.getDeclaredField("receiverList");
//		tempProperty = new MetaProperty("receiverList", field.getGenericType());
//		expectedProperties.add(tempProperty);
//		Set<MetaProperty> expectedOptionalProperties = new HashSet<MetaProperty>();
//		Set<MetaProperty> expectedRequiredProperties = expectedProperties;
//
//		Set<MetaProperty> descriptorRequiredProperties = new HashSet<MetaProperty>(descriptor.getRequriedProperties());
//		Set<MetaProperty> descriptorProperties = new HashSet<MetaProperty>(descriptor.getProperties());
//		Set<MetaProperty> descriptorOptionalProperties = new HashSet<MetaProperty>(descriptor.getOptionalProperties());
//
//		assertTrue(compare(expectedProperties, descriptorProperties));
//		assertTrue(compare(expectedOptionalProperties, descriptorOptionalProperties));
//		assertTrue(compare(expectedRequiredProperties, descriptorRequiredProperties));
//		//
//	}
//
//	/**
//	 * Tests how generic fields (in properties) are parsed.
//	 */
//	@Test
//	public void testComponentWithGenericFieldProperties() throws IllegalArgumentException, ConfigurationException {
//		IComponentDescriptor descriptor = reader.readDescritptor(ComponentWithGenericFieldProperties.class);
//
//		Collection<MetaProperty> properties = descriptor.getProperties();
//		assertEquals(5, properties.size());
//
//		for (MetaProperty metaProperty : properties) {
//			if ("stringList".equals(metaProperty.getName())) {
//				List<Class<?>> classes = metaProperty.getGenericClasses();
//				assertEquals(1, classes.size());
//				assertTrue(classes.contains(String.class));
//			} else if ("questionList".equals(metaProperty.getName())) {
//				List<Class<?>> classes = metaProperty.getGenericClasses();
//				assertEquals(1, classes.size());
//				// XXX: What should be here?
//			} else if ("boundedList".equals(metaProperty.getName())) {
//				List<Class<?>> classes = metaProperty.getGenericClasses();
//				assertEquals(1, classes.size());
//				// XXX: What should be here?
//			} else if ("deepList".equals(metaProperty.getName())) {
//				List<Class<?>> classes = metaProperty.getGenericClasses();
//				assertEquals(1, classes.size());
//				// XXX: What should be here?
//			} else if ("rawList".equals(metaProperty.getName())) {
//				List<Class<?>> classes = metaProperty.getGenericClasses();
//				assertNotNull(classes);
//				assertEquals(0, classes.size());
//			} else {
//				fail("Unknown property returned.");
//			}
//		}
//	}
//
//	private boolean compare(final MetaProperty property1, final MetaProperty property2) {
//		if (property1.getClass() != null && !property1.getClass().equals(property2.getClass())) {
//	        return false;
//        }
//		if (property1.getClass() == null && property2.getClass() != null) {
//	        return false;
//        }
//		if (property1.getGenericClasses() != null
//		        && !property1.getGenericClasses().equals(property2.getGenericClasses())) {
//	        return false;
//        }
//		if (property1.getGenericClasses() == null && property2.getGenericClasses() != null) {
//	        return false;
//        }
//		if (property1.getName() != null && !property1.getName().equals(property2.getName())) {
//	        return false;
//        }
//		if (property1.getName() == null && property2.getName() != null) {
//	        return false;
//        }
//		if (property1.getPropertyClass() != null && !property1.getPropertyClass().equals(property2.getPropertyClass())) {
//	        return false;
//        }
//		if (property1.getPropertyClass() == null && property2.getPropertyClass() != null) {
//	        return false;
//        }
//		return true;
//	}
//
//	private boolean compare(final Collection<MetaProperty> set1, final Collection<MetaProperty> set2) {
//		boolean result = false;
//		for (MetaProperty property1 : set1) {
//			result = false;
//			for (MetaProperty property2 : set2) {
//				if (compare(property1, property2)) {
//	                result = true;
//                }
//			}
//			if (!result) {
//	            return false;
//            }
//		}
//		for (MetaProperty property2 : set2) {
//			result = false;
//			for (MetaProperty property1 : set1) {
//				if (compare(property1, property2)) {
//	                result = true;
//                }
//			}
//			if (!result) {
//	            return false;
//            }
//		}
//		return true;
//	}
//
//	private void checkPropertiesOfBaseComponent(final IComponentDescriptor descriptor) {
//		// properties inherited from BaseComponent
//		assertTrue(descriptor.containsProperty(BaseComponent.Properties.STRING_PROPERTY));
//		assertTrue(descriptor.containsProperty(BaseComponent.Properties.INTEGER_PROPERTY));
//	}
//
//	private Set<MetaProperty> createBaseComponentOptionalProperties() throws PropertyException {
//		Set<MetaProperty> res = new HashSet<MetaProperty>();
//		res.add(new MetaProperty(BaseComponent.Properties.INTEGER_PROPERTY, Integer.class));
//		return res;
//	}
//
//	private Set<MetaProperty> createBaseComponentRequriedProperties() throws PropertyException {
//		Set<MetaProperty> res = new HashSet<MetaProperty>();
//		res.add(new MetaProperty(BaseComponent.Properties.STRING_PROPERTY, String.class));
//		return res;
//	}

}
