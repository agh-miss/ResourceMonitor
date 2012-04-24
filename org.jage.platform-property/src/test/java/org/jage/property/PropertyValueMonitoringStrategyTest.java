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

import org.jage.monitor.IChangesNotifier;

import static org.junit.Assert.*;

public abstract class PropertyValueMonitoringStrategyTest {

	protected class TestProperty extends Property {

		private Object _expectedNewValue;

		private Object _value;

		private int _notifyMonitorsInvokationCount = 0;

		public TestProperty(Object value) {
			_value = value;
		}

		@Override
		public MetaProperty getMetaProperty() {
			try {
				return new MetaProperty("propertyA", IChangesNotifier.class, true, true);
			} catch (PropertyException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Object getValue() {
			return _value;
		}

		@Override
		public void setValue(Object value) {
			_value = value;
		}

		@Override
		public void notifyMonitors(Object newValue) {
			_notifyMonitorsInvokationCount++;
			assertEquals(_expectedNewValue, newValue);
		}

		public void expectNotifyMonitors(Object newValue) {
			_expectedNewValue = newValue;
		}

		public int getNotifyMonitorsInvokationCount() {
			return _notifyMonitorsInvokationCount;
		}

		public void resetNotifyMonitorsInvokationCount() {
			_notifyMonitorsInvokationCount = 0;
		}
	}

}
