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
package org.jage.property.xml;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.same;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.jage.event.ObjectChangedEvent;
import org.jage.monitor.IChangesNotifierMonitor;
import org.jage.property.InvalidPropertyPathException;
import org.jage.property.MetaPropertiesSet;
import org.jage.property.MetaProperty;
import org.jage.property.Property;
import org.jage.property.functions.CountFunction;
import org.jage.property.monitors.IPropertyMonitorRule;
import org.jage.property.testHelpers.ChangesNotifierStub;
import org.jage.property.testHelpers.InnerClassWithProperties;
import org.jage.property.testHelpers.PropertyMonitorStub;
import org.jage.property.xml.testHelpers.AdvancedExampleComponent;
import org.jage.property.xml.testHelpers.SubExampleComponent;
import org.junit.Test;

public class XMLBasedPropertyContainerTest {

	@Test
	public void testGetSetValue() throws Exception {
		SubExampleComponent component = new SubExampleComponent();
		XMLBasedPropertyContainer container = new XMLBasedPropertyContainer(XMLBasedPropertyProvider.getInstance()
				.getMetaPropertiesSet(component.getClass()), component);

		component.setIntProperty(0);
		assertEquals(0, container.getProperty("intProperty").getValue());
		container.getProperty("intProperty").setValue(5);
		assertEquals(5, container.getProperty("intProperty").getValue());

		component.setStringProperty("a");
		assertEquals("a", container.getProperty("stringProperty").getValue());
		container.getProperty("stringProperty").setValue("V");
		assertEquals("V", container.getProperty("stringProperty").getValue());

		container.getProperty("floatProperty").setValue(1.0f);
		assertEquals(1.0f, ((Float) container.getProperty("floatProperty").getValue()).floatValue(), 0.0f);
	}

	@Test
	public void testGetMetaProperty() throws Exception {
		AdvancedExampleComponent component = new AdvancedExampleComponent();
		XMLBasedPropertyContainer container = new XMLBasedPropertyContainer(XMLBasedPropertyProvider.getInstance()
				.getMetaPropertiesSet(component.getClass()), component);

		MetaProperty floatMetaProperty = container.getProperty("floatProperty").getMetaProperty();
		assertTrue(float.class.equals(floatMetaProperty.getPropertyClass()));
		assertTrue(floatMetaProperty.isWriteable());
		assertTrue(floatMetaProperty.isMonitorable());
		assertEquals("floatProperty", floatMetaProperty.getName());

		MetaProperty objectMetaProperty = container.getProperty("objectProperty").getMetaProperty();
		assertTrue(Object.class.equals(objectMetaProperty.getPropertyClass()));
		assertTrue(objectMetaProperty.isWriteable());
		assertTrue(objectMetaProperty.isMonitorable());
		assertEquals("objectProperty", objectMetaProperty.getName());
	}

	@Test
	public void testMonitors() throws Exception {
		AdvancedExampleComponent component = new AdvancedExampleComponent();
		XMLBasedPropertyContainer container = new XMLBasedPropertyContainer(XMLBasedPropertyProvider.getInstance()
				.getMetaPropertiesSet(component.getClass()), component);

		PropertyMonitorStub monitor1 = new PropertyMonitorStub();

		IPropertyMonitorRule ruleMock1 = createMock(IPropertyMonitorRule.class);

		expect(ruleMock1.isActive(anyObject(), anyObject())).andReturn(true).times(2);

		replay(ruleMock1);

		PropertyMonitorStub monitor2 = new PropertyMonitorStub();

		container.addPropertyMonitor("floatProperty", monitor1, ruleMock1);
		container.addPropertyMonitor("floatProperty", monitor2);
		container.getProperty("floatProperty").setValue(5);
		container.removePropertyMonitor("floatProperty", monitor2);
		container.getProperty("floatProperty").setValue(6);

		// Validating stubs
		verify(ruleMock1);
		assertEquals(2, monitor1.getPropertyChangedInvokationCounter());
		assertEquals(1, monitor2.getPropertyChangedInvokationCounter());
	}

	@Test
	public void testNotifyAboutChanges() throws Exception {
		AdvancedExampleComponent component = new AdvancedExampleComponent();
		XMLBasedPropertyContainer container = new XMLBasedPropertyContainer(XMLBasedPropertyProvider.getInstance()
				.getMetaPropertiesSet(component.getClass()), component);

		PropertyMonitorStub monitor1 = new PropertyMonitorStub();
		PropertyMonitorStub monitor2 = new PropertyMonitorStub();

		container.addPropertyMonitor("monitorableObjectProperty", monitor1);
		container.addPropertyMonitor("monitorableStringProperty", monitor2);
		component.setMonitorableObjectPropertyValue(new Object());
		// would not work, because by default setter methods of external components don't notify changes
		// assertEquals(1, monitor1.getPropertyChangedInvokationCounter());
		assertEquals(0, monitor2.getPropertyChangedInvokationCounter());
	}

	/**
	 * Scenario: property informs that it has been changed. Property container implements IChangesNotifier, and should
	 * inform it's monitors about the change.
	 * @throws Exception
	 */

	@Test
	public void testChangesNotification() throws Exception {
		final AdvancedExampleComponent component = new AdvancedExampleComponent();
		final XMLBasedPropertyContainer container = new XMLBasedPropertyContainer(XMLBasedPropertyProvider
				.getInstance().getMetaPropertiesSet(component.getClass()), component);

		IChangesNotifierMonitor monitorMock = createMock(IChangesNotifierMonitor.class);

		monitorMock.objectChanged(same(container), isA(ObjectChangedEvent.class));
		expectLastCall().once();

		replay(monitorMock);

		container.addMonitor(monitorMock);
		// Won't work, because "intProperty" is served by XMLSetterGetterProperty which doesn't support monitoring.
		// container.getProperty("intProperty").setValue(3);
		// Works, because "floatProperty" is served by XMLFieldProperty which support monitoring.
		container.getProperty("floatProperty").setValue(3);
		verify(monitorMock);

	}

	/**
	 * Scenario: accessing and monitoring complex property. 1. Subproperty is accessed using complex path. 2. Monitor is
	 * attached to the subproperty. 3. Monitor is attached to the parent property of the subproperty.
	 */
	@Test
	public void testComplexProperty() throws Exception {
		AdvancedExampleComponent component = new AdvancedExampleComponent();
		XMLBasedPropertyContainer container = new XMLBasedPropertyContainer(XMLBasedPropertyProvider.getInstance()
				.getMetaPropertiesSet(component.getClass()), component);

		// Retreiving value of the property.
		Property subProperty = container.getProperty("complexProperty.stringProperty");
		assertNotNull(subProperty);

		// Attaching monitor to the property.
		PropertyMonitorStub monitor = new PropertyMonitorStub();
		container.addPropertyMonitor("complexProperty.stringProperty", monitor);
		subProperty.setValue(subProperty.getValue() + " - new value");
		assertEquals(1, monitor.getPropertyChangedInvokationCounter());
		container.removePropertyMonitor("complexProperty.stringProperty", monitor);

		// Attaching monitor only to "complexProperty"
		monitor = new PropertyMonitorStub();
		container.addPropertyMonitor("complexProperty", monitor);
		subProperty.setValue("");
		assertEquals(1, monitor.getPropertyChangedInvokationCounter());
		container.removePropertyMonitor("complexProperty", monitor);

		// The monitor should not get the message now.
		subProperty.setValue("newValue");
		assertEquals(1, monitor.getPropertyChangedInvokationCounter());
	}

	/**
	 * Scenario: accesing and monitoring complex property that uses arrays. 1. Subproperty is accessed using complex
	 * path with with array indices. 2. Monitor is attached to the subproperty. 3. Monitor is attached to the parent
	 * property of the subproperty.
	 * @throws Exception
	 */
	@Test
	public void testComplexArrayProperty() throws Exception {
		AdvancedExampleComponent component = new AdvancedExampleComponent();
		XMLBasedPropertyContainer container = new XMLBasedPropertyContainer(XMLBasedPropertyProvider.getInstance()
				.getMetaPropertiesSet(component.getClass()), component);

		// Retreiving value of complex property.
		Property subProperty = container.getProperty("complexArrayProperty[0].intProperty");
		subProperty.setValue(0);
		assertNotNull(subProperty);

		// Attaching monitor to the property.
		PropertyMonitorStub monitor = new PropertyMonitorStub();
		container.addPropertyMonitor("complexArrayProperty[0].intProperty", monitor);
		subProperty.setValue(1);
		assertEquals(1, monitor.getPropertyChangedInvokationCounter());
		container.removePropertyMonitor("complexArrayProperty[0].intProperty", monitor);

		// Attaching monitor only to "complexArrayProperty"
		monitor = new PropertyMonitorStub();
		container.addPropertyMonitor("complexArrayProperty", monitor);
		subProperty.setValue(0);
		assertEquals(1, monitor.getPropertyChangedInvokationCounter());
		container.removePropertyMonitor("complexArrayProperty", monitor);

		// The monitor should not get the message now.
		subProperty.setValue(1);
		assertEquals(1, monitor.getPropertyChangedInvokationCounter());
	}

	/**
	 * Scenario: adding and removing functions. 1. New function is created. 2. The function is added to the container.
	 * 3. The container should contain the function. 4. Function should be able to access properties from the container.
	 * 5. Function is removed from the container. 6. The container shouldn't contain the function.
	 * @throws Exception
	 */
	@Test
	public void testAddingAndRemovingFunctions() throws Exception {
		AdvancedExampleComponent component = new AdvancedExampleComponent();
		XMLBasedPropertyContainer container = new XMLBasedPropertyContainer(XMLBasedPropertyProvider.getInstance()
				.getMetaPropertiesSet(component.getClass()), component);
		CountFunction function = new CountFunction("count", "complexArrayProperty[*].stringProperty");
		container.addFunction(function);
		assertSame(function, container.getProperty("count"));
		assertEquals(new Integer(2), function.getValue());
		container.removeFunction(function);
		try {
			container.getProperty("count");
			fail("Function should be removed from the container.");
		} catch (InvalidPropertyPathException ex) {
		}
		;
	}

	/**
	 * Scenario: getting all meta properties, before and after accessing any property.
	 */
	@Test
	public void testGetMetaProperties() throws Exception {
		AdvancedExampleComponent component = new AdvancedExampleComponent();
		XMLBasedPropertyContainer container = new XMLBasedPropertyContainer(XMLBasedPropertyProvider.getInstance()
				.getMetaPropertiesSet(component.getClass()), component);
		validateMetaProperties(container.getMetaProperties());
		container.getProperties();
		validateMetaProperties(container.getMetaProperties());
	}

	private void validateMetaProperties(MetaPropertiesSet metaProperties) {
		assertEquals(11, metaProperties.size());
		assertEquals(InnerClassWithProperties.class, metaProperties.getMetaProperty("complexProperty")
				.getPropertyClass());
		assertEquals(InnerClassWithProperties[].class, metaProperties.getMetaProperty("complexArrayProperty")
				.getPropertyClass());
		assertEquals(ChangesNotifierStub.class, metaProperties.getMetaProperty("changesNotifierProperty")
				.getPropertyClass());
		assertEquals(float.class, metaProperties.getMetaProperty("floatProperty").getPropertyClass());
		assertEquals(Object.class, metaProperties.getMetaProperty("objectProperty").getPropertyClass());
		assertEquals(ChangesNotifierStub.class, metaProperties.getMetaProperty("changesNotifierProperty2")
				.getPropertyClass());
		assertEquals(String.class, metaProperties.getMetaProperty("stringProperty").getPropertyClass());
		assertEquals(Object.class, metaProperties.getMetaProperty("monitorableObjectProperty").getPropertyClass());
		assertEquals(String.class, metaProperties.getMetaProperty("monitorableStringProperty").getPropertyClass());
		assertEquals(List.class, metaProperties.getMetaProperty("normalList").getPropertyClass());
	}
}
