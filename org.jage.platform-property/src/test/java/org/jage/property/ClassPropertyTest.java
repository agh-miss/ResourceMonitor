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
import static org.junit.Assert.fail;

import java.lang.reflect.Method;

import org.jage.property.monitors.DefaultPropertyMonitorRule;
import org.jage.property.monitors.IPropertyMonitorRule;
import org.jage.property.testHelpers.ChangesNotifierStub;
import org.jage.property.testHelpers.ClassWithProperties;
import org.jage.property.testHelpers.PropertyMonitorStub;
import org.junit.Before;
import org.junit.Test;

public class ClassPropertyTest {

	private ClassWithProperties _propertiesObject;

	@Before
	public void setUp() throws Exception {
		_propertiesObject = new ClassWithProperties();
	}

	private GetterSetterProperty getIntProperty() throws Exception {
		Method intPropertyGetter = _propertiesObject.getIntPropertyGetter();
		Method intPropertySetter = _propertiesObject.getIntPropertySetter();
		PropertyGetter intAnnotation = intPropertyGetter.getAnnotation(PropertyGetter.class);
		GetterSetterMetaProperty metaProperty = new GetterSetterMetaProperty(
		        intAnnotation.propertyName(), intPropertyGetter, intPropertySetter, intAnnotation.isMonitorable());
		return new GetterSetterProperty(metaProperty, _propertiesObject);
	}

	private GetterSetterProperty getReadonlyIntProperty() throws Exception {
		Method intPropertyGetter = _propertiesObject.getIntPropertyGetter();
		PropertyGetter intAnnotation = intPropertyGetter.getAnnotation(PropertyGetter.class);
		GetterSetterMetaProperty metaProperty = new GetterSetterMetaProperty(
		        intAnnotation.propertyName(), intPropertyGetter, intAnnotation.isMonitorable());
		return new GetterSetterProperty(metaProperty, _propertiesObject);
	}

	@Test
	public void testGetSetValue() throws Exception {
		_propertiesObject.setIntProperty(0);
		Property property = getIntProperty();
		assertEquals(0, property.getValue());
		property.setValue(1);
		assertEquals(1, property.getValue());
		assertEquals(1, _propertiesObject.getIntProperty());
	}

	@Test
	public void testReadOnlyProperty() throws Exception {
		Property property = getReadonlyIntProperty();
		try {
			property.setValue(1);
			fail();
		} catch (InvalidPropertyOperationException ex) {
		}
	}

	@Test
	public void testGetMetaProperty() throws Exception {
		Property property = getIntProperty();
		MetaProperty metaProperty = property.getMetaProperty();
		assertTrue(int.class.equals(metaProperty.getPropertyClass()));
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

		Property property = getIntProperty();
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
				.getProperty("changesNotifierProperty2");
		property.addMonitor(monitor, new DefaultPropertyMonitorRule());

		ChangesNotifierStub notifier = (ChangesNotifierStub) property
				.getValue();
		notifier.notifyMonitorsAboutChange();

		assertEquals(1, monitor.getPropertyChangedInvokationCounter());
	}
}
