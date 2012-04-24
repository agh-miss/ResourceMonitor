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
package org.jage.property.xml.testHelpers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.jage.property.testHelpers.ChangesNotifierStub;

public class AdvancedExampleComponent extends SimpleExampleComponent {

	@SuppressWarnings("unused")
	private static final String version = "1.0.0";

	private int _intProperty = 0;
	private String _stringProperty = "";
	private ChangesNotifierStub _changesNotifierProperty2 = new ChangesNotifierStub();

	@SuppressWarnings("unused")
	private ChangesNotifierStub _changesNotifierProperty = new ChangesNotifierStub();

	private float _floatProperty = 0.0f;

	private Object _objectProperty = new Object();

	@SuppressWarnings("unused")
	private Object _monitorableObjectProperty = new Object();

	@SuppressWarnings("unused")
	private String _monitorableStringProperty = "Monitorable string";

	private List<NormalComponent> _normalList;

	/**
	 * 
	 */
	public AdvancedExampleComponent() {
	}

	public int getIntProperty() {
		return _intProperty;
	}

	public void setIntProperty(int value) {
		_intProperty = value;
	}

	public ChangesNotifierStub getChangesNotifier() {
		return _changesNotifierProperty2;
	}

	public void setChangesNotifier(ChangesNotifierStub value) {
		_changesNotifierProperty2 = value;
	}

	public Method getIntPropertyGetter() {
		try {
			return AdvancedExampleComponent.class.getMethod("getIntProperty", new Class[] {});
		} catch (NoSuchMethodException ex) {
			return null;
		}
	}

	public Method getIntPropertySetter() {
		try {
			return AdvancedExampleComponent.class.getMethod("setIntProperty", new Class[] { int.class });
		} catch (NoSuchMethodException ex) {
			return null;
		}
	}

	public String getStringProperty() {
		return _stringProperty;
	}

	public void setStringProperty(String value) {
		_stringProperty = value;
	}

	public Method getStringPropertyGetter() {
		try {
			return AdvancedExampleComponent.class.getMethod("getStringProperty", new Class[] {});
		} catch (NoSuchMethodException ex) {
			return null;
		}
	}

	public Method getStringPropertySetter() {
		try {
			return AdvancedExampleComponent.class.getMethod("setStringProperty", new Class[] { String.class });
		} catch (NoSuchMethodException ex) {
			return null;
		}
	}

	public Field getFloatPropertyField() {
		try {
			return AdvancedExampleComponent.class.getDeclaredField("_floatProperty");
		} catch (NoSuchFieldException ex) {
			return null;
		}
	}

	public Field getObjectPropertyField() {
		try {
			return AdvancedExampleComponent.class.getDeclaredField("_objectProperty");

		} catch (NoSuchFieldException ex) {
			return null;
		}
	}

	public void setMonitorableObjectPropertyValue(Object newValue) {
		_monitorableObjectProperty = newValue;
	}

	public float getFloatProperty() {
		return _floatProperty;
	}

	public int getIntegerProperty() {
		return _intProperty;
	}

	public Object getObjectProperty() {
		return _objectProperty;
	}

	public List<NormalComponent> getNormalList() {
		return _normalList;
	}
}
