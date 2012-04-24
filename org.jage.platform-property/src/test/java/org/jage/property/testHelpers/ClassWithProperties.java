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
package org.jage.property.testHelpers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.jage.property.ClassPropertyContainer;
import org.jage.property.PropertyField;
import org.jage.property.PropertyGetter;
import org.jage.property.PropertySetter;

public class ClassWithProperties extends ClassPropertyContainer {
		
		private int _intProperty = 0;
		private String _stringProperty = "";
		private ChangesNotifierStub _changesNotifierProperty2 = new ChangesNotifierStub();
		
		@SuppressWarnings("unused")
		@PropertyField(propertyName="complexProperty")
		private InnerClassWithProperties _complexProperty = new InnerClassWithProperties();
		
		@SuppressWarnings("unused")
		@PropertyField(propertyName="complexArrayProperty")
		private InnerClassWithProperties[] _complexArrayProperty = {
				new InnerClassWithProperties(),
				new InnerClassWithProperties()
		};
		
		@SuppressWarnings("unused")
		@PropertyField(propertyName="changesNotifierProperty", isMonitorable=true)
		private ChangesNotifierStub _changesNotifierProperty = new ChangesNotifierStub();
		
		@SuppressWarnings("unused")
		@PropertyField(propertyName="floatProperty", isMonitorable=true)
		private float _floatProperty = 0.0f;
		
		@SuppressWarnings("unused")
		@PropertyField(propertyName="objectProperty")
		private Object _objectProperty = new Object();
		
		@SuppressWarnings("unused")
		@PropertyField(propertyName="monitorableObjectProperty")
		private Object _monitorableObjectProperty = new Object();
		
		@SuppressWarnings("unused")
		@PropertyField(propertyName="monitorableStringProperty")
		private String _monitorableStringProperty = "Monitorable string";
		
		public ClassWithProperties() {
		}
		
		@PropertyGetter(propertyName="intProperty", isMonitorable=true)
		public int getIntProperty() {
			return _intProperty;
		}
		
		@PropertySetter(propertyName="intProperty")
		public void setIntProperty(int value) {
			_intProperty = value;
			notifyMonitorsForChangedProperty("intProperty");
		}
		
		@PropertyGetter(propertyName="changesNotifierProperty2", isMonitorable=true)
		public ChangesNotifierStub getChangesNotifier() {
			return _changesNotifierProperty2;
		}
		
		@PropertySetter(propertyName="changesNotifierProperty2")
		public void setChangesNotifier(ChangesNotifierStub value) {
			_changesNotifierProperty2 = value;
			notifyMonitorsForChangedProperty("changesNotifier2");
		}
		
		public Method getIntPropertyGetter() {
			try {
				return ClassWithProperties.class.getMethod(
						"getIntProperty", new Class[] { });
			}
			catch (NoSuchMethodException ex) {
				return null;
			}
		}
		
		public Method getIntPropertySetter() {
			try {
				return ClassWithProperties.class.getMethod(
						"setIntProperty", new Class[] { int.class }); 
			}
			catch (NoSuchMethodException ex) {
				return null;
			}
		}
		
		@PropertyGetter(propertyName="stringProperty", isMonitorable=false)
		public String getStringProperty() {
			return _stringProperty;
		}
		
		@PropertySetter(propertyName="stringProperty")
		public void setStringProperty(String value) {
			_stringProperty = value;
			notifyMonitorsForChangedProperty("stringProperty");
		}
		
		public Method getStringPropertyGetter() {
			try {
				return ClassWithProperties.class.getMethod(
						"getStringProperty", new Class[] { });
			}
			catch (NoSuchMethodException ex) {
				return null;
			}
		}
		
		public Method getStringPropertySetter() {
			try {
				return ClassWithProperties.class.getMethod(
						"setStringProperty", new Class[] { String.class });
			}
			catch (NoSuchMethodException ex) {
				return null;
			}
		}
		
		public Field getFloatPropertyField() {
			try {
				return ClassWithProperties.class.getDeclaredField("_floatProperty");
			}
			catch (NoSuchFieldException ex) {
				return null;
			}
		}
		
		public Field getObjectPropertyField() {
			try {
				return ClassWithProperties.class.getDeclaredField("_objectProperty");
				
			}
			catch (NoSuchFieldException ex) {
				return null;
			}
		}
		
		public void invokeNotifyMonitorsForChangedProperties() {
			notifyMonitorsForChangedProperties();
		}
		
		public void setMonitorableObjectPropertyValue(Object newValue) {
			_monitorableObjectProperty = newValue;
		}
	}
	
