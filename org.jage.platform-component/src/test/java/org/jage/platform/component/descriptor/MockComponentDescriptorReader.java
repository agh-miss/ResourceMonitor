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
 * File: MockComponentDescriptorReader.java
 * Created: 2010-03-10
 * Author: kpietak
 * $Id: MockComponentDescriptorReader.java 183 2012-03-31 20:33:32Z krzywick $
 */

package org.jage.platform.component.descriptor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;

import javax.xml.bind.PropertyException;

import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.component.sample.CollectionComponent;
import org.jage.platform.component.sample.ConstructorDependencyComponent;
import org.jage.platform.component.sample.FirstCycleComponent;
import org.jage.platform.component.sample.ReferenceViaPropertyComponent;
import org.jage.platform.component.sample.SecondCycleComponent;
import org.jage.platform.component.sample.SimplePropertiesComponent;

/**
 * Mock implementation of {@link IComponentDescriptorReader} which returns
 * sample component descriptors.
 *
 * @author AGH AgE Team
 *
 */
public class MockComponentDescriptorReader implements IComponentDescriptorReader {

	@Override
    public IComponentDescriptor readDescritptor(final Object source) throws ConfigurationException, IllegalArgumentException {

		if (source == null) {
			throw new IllegalArgumentException();
		}
		IComponentDescriptor res = null;
		if (source instanceof Class) {
			try {
				Class<?> componentType = (Class<?>) source;
				if (componentType.equals(SimplePropertiesComponent.class)) {
					res = createSimplePropertiesComponentDescriptor();
				} else if (componentType.equals(ReferenceViaPropertyComponent.class)) {
					res = createReferenceViaPropertyComponentDescriptor();
				} else if (componentType.equals(ConstructorDependencyComponent.class)) {
					res = createConstructorDependencyComponent();
				} else if (componentType.equals(CollectionComponent.class)) {
					res = createCollectionComponent();
				} else if (componentType.equals(FirstCycleComponent.class)) {
					res = createFirstCycleComponent();
				} else if (componentType.equals(SecondCycleComponent.class)) {
					res = createSecondCycleComponent();
				}
			} catch(PropertyException e) {
				throw new IllegalArgumentException(e);
			} catch (SecurityException e) {
				throw new IllegalArgumentException(e);
            } catch (NoSuchFieldException e) {
            	throw new IllegalArgumentException(e);
            }
		}

		return res;
	}

	private IComponentDescriptor createSimplePropertiesComponentDescriptor() throws PropertyException {

		ComponentDescriptor res = new ComponentDescriptor();
		res.setComponentType(SimplePropertiesComponent.class);
//
//		res.addRequiredProperty(new MetaProperty(SimplePropertiesComponent.Properties.STRING_PROPERTY, String.class,
//				true, false));
//
//		res.addRequiredProperty(new MetaProperty(SimplePropertiesComponent.Properties.INTEGER_PROPERTY_1,
//				Integer.class, true, false));
//
//		res.addRequiredProperty(new MetaProperty(SimplePropertiesComponent.Properties.INTEGER_PROPERTY_2, int.class,
//				true, false));
//
//		res.addRequiredProperty(new MetaProperty(SimplePropertiesComponent.Properties.LONG_PROPERTY_1, Long.class,
//				true, false));
//
//		res.addRequiredProperty(new MetaProperty(SimplePropertiesComponent.Properties.LONG_PROPERTY_2, long.class,
//				true, false));
//
//		res.addRequiredProperty(new MetaProperty(SimplePropertiesComponent.Properties.FLOAT_PROPERTY_1, Float.class,
//				true, false));
//
//		res.addRequiredProperty(new MetaProperty(SimplePropertiesComponent.Properties.FLOAT_PROPERTY_2, float.class,
//				true, false));
//
//		res.addOptionalProperty(new MetaProperty(SimplePropertiesComponent.Properties.DOUBLE_PROPERTY_1, Double.class,
//				true, false));
//
//		res.addOptionalProperty(new MetaProperty(SimplePropertiesComponent.Properties.DOUBLE_PROPERTY_2, double.class,
//				true, false));
//
//		res.addOptionalProperty(new MetaProperty(SimplePropertiesComponent.Properties.SHORT_PROPERTY_1, Short.class,
//				true, false));
//
//		res.addOptionalProperty(new MetaProperty(SimplePropertiesComponent.Properties.SHORT_PROPERTY_2, short.class,
//				true, false));
//
//		res.addOptionalProperty(new MetaProperty(SimplePropertiesComponent.Properties.BYTE_PROPERTY_1, Byte.class,
//				true, false));
//
//		res.addOptionalProperty(new MetaProperty(SimplePropertiesComponent.Properties.BYTE_PROPERTY_2, byte.class,
//				true, false));
//
//		res.addOptionalProperty(new MetaProperty(SimplePropertiesComponent.Properties.BOOLEAN_PROPERTY_1,
//				Boolean.class, true, false));
//
//		res.addOptionalProperty(new MetaProperty(SimplePropertiesComponent.Properties.BOOLEAN_PROPERTY_2,
//				boolean.class, true, false));
//
//		res.addOptionalProperty(new MetaProperty(SimplePropertiesComponent.Properties.CLASS_PROPERTY, Class.class,
//				true, false));

		return res;

	}

	private IComponentDescriptor createReferenceViaPropertyComponentDescriptor() throws PropertyException {

		ComponentDescriptor res = new ComponentDescriptor();

//		res.addRequiredProperty(new MetaProperty(ReferenceViaPropertyComponent.Properties.SIMPLE_COMPONENT_1,
//				SimplePropertiesComponent.class, true, false));
//
//		res.addRequiredProperty(new MetaProperty(ReferenceViaPropertyComponent.Properties.SIMPLE_COMPONENT_2,
//				SimplePropertiesComponent.class, true, false));
//
//		res.addOptionalProperty(new MetaProperty(ReferenceViaPropertyComponent.Properties.THE_SAME_TYPE_COMPONENT,
//				ReferenceViaPropertyComponent.class, true, false));

		res.setComponentType(ReferenceViaPropertyComponent.class);

		return res;
	}

	private IComponentDescriptor createConstructorDependencyComponent() {
		ComponentDescriptor res = new ComponentDescriptor();

		res.setComponentType(ConstructorDependencyComponent.class);

		// add constructor with parameters
		res.addConstructorParametersTypes(Arrays.asList(new Class<?>[] { SimplePropertiesComponent.class,
				ReferenceViaPropertyComponent.class }));
		// add no-parameters constructor
		res.addConstructorParametersTypes(new LinkedList<Class<?>>());

		return res;
	}

	private IComponentDescriptor createCollectionComponent() throws SecurityException, NoSuchFieldException, PropertyException {
		ComponentDescriptor res = new ComponentDescriptor();
		res.setComponentType(CollectionComponent.class);

		Field field = CollectionComponent.class.getDeclaredField(CollectionComponent.Properties.STRING_LIST);

//		res.addRequiredProperty(new MetaProperty(CollectionComponent.Properties.STRING_LIST,
//				field.getGenericType(), true, false));

		return res;

	}

	private IComponentDescriptor createFirstCycleComponent() throws PropertyException {
		ComponentDescriptor res = new ComponentDescriptor();
		res.setComponentType(FirstCycleComponent.class);

//		res.addRequiredProperty(new MetaProperty(FirstCycleComponent.Properties.FIRST_PROPERTY,
//				SecondCycleComponent.class, true, false));

		return res;

	}

	private IComponentDescriptor createSecondCycleComponent() throws PropertyException {
		ComponentDescriptor res = new ComponentDescriptor();
		res.setComponentType(SecondCycleComponent.class);

//		res.addRequiredProperty(new MetaProperty(SecondCycleComponent.Properties.SECOND_PROPERTY,
//				FirstCycleComponent.class, true, false));

		return res;

	}

	// END Helper Methods

}
