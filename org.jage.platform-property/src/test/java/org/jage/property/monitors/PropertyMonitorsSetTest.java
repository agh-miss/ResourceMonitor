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
package org.jage.property.monitors;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.jage.property.testHelpers.PropertyMonitorStub;
import org.junit.Test;

public class PropertyMonitorsSetTest {

	@Test
	public void testMonitorsNotification() {
		
		IPropertyMonitorRule ruleMock1 = createMock(IPropertyMonitorRule.class);
		IPropertyMonitorRule ruleMock2 = createMock(IPropertyMonitorRule.class);
		
		expect(ruleMock1.isActive(anyObject(), anyObject())).andReturn(true).once();
		expect(ruleMock2.isActive(anyObject(), anyObject())).andReturn(true).once();
		expect(ruleMock1.isActive(anyObject(), anyObject())).andReturn(true).once();
		
		replay(ruleMock1, ruleMock2);
		
		PropertyMonitorStub monitor1 = new PropertyMonitorStub();
		PropertyMonitorStub monitor2 = new PropertyMonitorStub();

		PropertyMonitorsSet set = new PropertyMonitorsSet();
		set.addMonitor(monitor1, ruleMock1);
		set.addMonitor(monitor2, ruleMock2);
		set.notifyMonitors(null, null);

		set.removeMonitor(monitor2);
		set.notifyMonitors(null, null);

		verify(ruleMock1, ruleMock2);
		assertEquals(2, monitor1.getPropertyChangedInvokationCounter());
		assertEquals(1, monitor2.getPropertyChangedInvokationCounter());

	}

	@Test
	public void testNotificationWithFalseRule() {
		
		IPropertyMonitorRule ruleMock1 = createMock(IPropertyMonitorRule.class);

		expect(ruleMock1.isActive(anyObject(), anyObject())).andReturn(false).once();
		replay(ruleMock1);
		
		PropertyMonitorStub monitor = new PropertyMonitorStub();

		PropertyMonitorsSet set = new PropertyMonitorsSet();
		set.addMonitor(monitor, ruleMock1);
		set.notifyMonitors(null, null);

		verify(ruleMock1);
		assertEquals(0, monitor.getPropertyChangedInvokationCounter());
	}
}
