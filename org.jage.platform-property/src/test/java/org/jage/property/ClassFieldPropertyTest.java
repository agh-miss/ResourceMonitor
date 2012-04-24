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

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import org.jage.property.monitors.DefaultPropertyMonitorRule;
import org.jage.property.monitors.IPropertyMonitorRule;
import org.jage.property.testHelpers.ChangesNotifierStub;
import org.jage.property.testHelpers.ClassWithProperties;
import org.jage.property.testHelpers.PropertyMonitorStub;
import org.junit.Before;
import org.junit.Test;

public class ClassFieldPropertyTest {
	private ClassWithProperties _propertiesObject;

	@Before
	public void setUp() throws Exception {
		_propertiesObject = new ClassWithProperties();
	}

	private FieldProperty getFloatProperty() throws Exception {
		Field field = _propertiesObject.getFloatPropertyField();
		PropertyField annotation = field.getAnnotation(PropertyField.class);
		FieldMetaProperty metaProperty = new FieldMetaProperty(annotation.propertyName(), field, annotation.isMonitorable());
		return new FieldProperty(metaProperty, _propertiesObject);
	}

	@Test
	public void testGetSetValue() throws Exception {
		_propertiesObject.setIntProperty(0);
		Property property = getFloatProperty();
		assertEquals(0.0, ((Float) property.getValue()).floatValue(), 0.0001f);
		property.setValue(1.0f);
		assertEquals(1.0f, ((Float) property.getValue()).floatValue(), 0.0001f);
	}

	@Test
	public void testGetMetaProperty() throws Exception {
		Property property = getFloatProperty();
		MetaProperty metaProperty = property.getMetaProperty();
		assertTrue(float.class.equals(metaProperty.getPropertyClass()));
		assertTrue(metaProperty.isMonitorable());
		assertTrue(metaProperty.isWriteable());
	}

	@Test
	public void testPropertyMonitors() throws Exception {

		IPropertyMonitorRule ruleMock1 = createMock(IPropertyMonitorRule.class);
		IPropertyMonitorRule ruleMock2 = createMock(IPropertyMonitorRule.class);

		expect(ruleMock1.isActive(anyObject(), anyObject())).andReturn(true).once();
		expect(ruleMock2.isActive(anyObject(), anyObject())).andReturn(true).once();
		expect(ruleMock1.isActive(anyObject(), anyObject())).andReturn(true).once();

		replay(ruleMock1, ruleMock2);

		PropertyMonitorStub monitor1 = new PropertyMonitorStub();
		PropertyMonitorStub monitor2 = new PropertyMonitorStub();

		Property property = getFloatProperty();
		property.addMonitor(monitor1, ruleMock1);
		property.addMonitor(monitor2, ruleMock2);
		property.notifyMonitors(null);

		property.removeMonitor(monitor2);
		property.notifyMonitors(null);

		verify(ruleMock1, ruleMock2);
		assertEquals(2, monitor1.getPropertyChangedInvokationCounter());
		assertEquals(1, monitor2.getPropertyChangedInvokationCounter());

	}

	/**
	 * Scenario: property value is object that implements IChangesNotifier
	 * interface. It changes its internal state and informs monitors about it.
	 * The property itself should inform its monitors about change.
	 *
	 * @throws Exception
	 */
	@Test
	public void testChangesNotifierProperty() throws Exception {
		PropertyMonitorStub monitor = new PropertyMonitorStub();

		Property property = _propertiesObject
				.getProperty("changesNotifierProperty");
		property.addMonitor(monitor, new DefaultPropertyMonitorRule());

		ChangesNotifierStub notifier = (ChangesNotifierStub) property
				.getValue();
		notifier.notifyMonitorsAboutChange();

		assertEquals(1, monitor.getPropertyChangedInvokationCounter());
	}
}
