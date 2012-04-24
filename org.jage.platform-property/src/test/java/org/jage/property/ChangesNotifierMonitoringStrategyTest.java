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

import org.jage.property.testHelpers.ChangesNotifierStub;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for the ChangesNotifierMonitoringStartegy class.
 * 
 * @author Tomek
 */
public class ChangesNotifierMonitoringStrategyTest extends
		PropertyValueMonitoringStrategyTest {

	private ChangesNotifierMonitoringStrategy _strategy;
	private ChangesNotifierStub _changesNotifier;
	private TestProperty _propertyMock;

	/**
	 * Inititalizes all fields used in this test.
	 */
	@Before
	public void setUp() {
		_changesNotifier = new ChangesNotifierStub();
		_propertyMock = new TestProperty(_changesNotifier);
		_strategy = new ChangesNotifierMonitoringStrategy(_propertyMock);
	}

	/**
	 * Scenario: value of the property, which implements IChangesNotifier,
	 * changes its internal state. The strategy should inform the property about
	 * it.
	 */
	@Test
	public void testValueInternalStateChanged() {
		_propertyMock.expectNotifyMonitors(_changesNotifier);
		_changesNotifier.notifyMonitorsAboutChange();
		assertEquals(1, _propertyMock.getNotifyMonitorsInvokationCount());
	}

	/**
	 * Scenario: value of the property is beeing deleted, and informs the
	 * startegy about it. The strategy should detach monitor and stop receiving
	 * messages from it.
	 */
	@Test
	public void testValueDeleted() {
		_changesNotifier.notifyMonitorsAboutDeletion();
		_changesNotifier.notifyMonitorsAboutChange();
		assertEquals(0, _propertyMock.getNotifyMonitorsInvokationCount());
	}

	/**
	 * Scenario: value of the property changes (the old object is beeing
	 * replaced with a new one). The strategy should remove monitors from the
	 * old value and attach them to the new one.
	 */
	@Test
	public void testPropertyValueChanged() {
		ChangesNotifierStub newValue = new ChangesNotifierStub();
		_strategy.propertyValueChanged(newValue);
		assertEquals(0, _changesNotifier.getNumberOfAttachedMonitors());
		assertEquals(1, newValue.getNumberOfAttachedMonitors());

		// Change in the old value. Property should not be informed about that.
		_changesNotifier.notifyMonitorsAboutChange();
		assertEquals(0, _propertyMock.getNotifyMonitorsInvokationCount());

		// Change in the new value. Property should be informed about that.
		_propertyMock.expectNotifyMonitors(newValue);
		_propertyMock.setValue(newValue);
		newValue.notifyMonitorsAboutChange();
	}

}
