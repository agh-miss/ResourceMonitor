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
/**
 *
 */
/*
 * File: SimplePropertiesComponent.java
 * Created: 2010-03-10
 * Author: kpietak
 * $Id: SimplePropertiesComponent.java 183 2012-03-31 20:33:32Z krzywick $
 */

package org.jage.platform.component.sample;

import org.jage.platform.component.annotation.Inject;

/**
 * Sample component which contains all available simple property types.
 *
 * @author AGH AgE Team
 *
 */
public class SimplePropertiesComponent {

	@Inject
	private String stringProperty;

	@Inject
	private Integer integerProperty1;

	@Inject
	private int integerProperty2;

	@Inject
	private Long longProperty1;

	@Inject
	private long longProperty2;

	@Inject
	private Float floatProperty1;

	@Inject
	private float floatProperty2;

	@Inject
	private Double doubleProperty1;

	@Inject
	private double doubleProperty2;

	@Inject
	private Short shortProperty1;

	@Inject
	private short shortProperty2;

	@Inject
	private Byte byteProperty1;

	@Inject
	private byte byteProperty2;

	@Inject
	private Boolean booleanProperty1;

	@Inject
	private boolean booleanProperty2;

	@Inject
	private Class<?> classProperty;

	public String getStringProperty() {
		return stringProperty;
	}

	public void setStringProperty(final String stringProperty) {
		this.stringProperty = stringProperty;
	}

	public Integer getIntegerProperty1() {
		return integerProperty1;
	}

	public void setIntegerProperty1(final Integer integerProperty1) {
		this.integerProperty1 = integerProperty1;
	}

	public int getIntegerProperty2() {
		return integerProperty2;
	}

	public void setIntegerProperty2(final int integerProperty2) {
		this.integerProperty2 = integerProperty2;
	}

	public Long getLongProperty1() {
		return longProperty1;
	}

	public void setLongProperty1(final Long longProperty1) {
		this.longProperty1 = longProperty1;
	}

	public long getLongProperty2() {
		return longProperty2;
	}

	public void setLongProperty2(final long longProperty2) {
		this.longProperty2 = longProperty2;
	}

	public Float getFloatProperty1() {
		return floatProperty1;
	}

	public void setFloatProperty1(final Float floatProperty1) {
		this.floatProperty1 = floatProperty1;
	}

	public float getFloatProperty2() {
		return floatProperty2;
	}

	public void setFloatProperty2(final float floatProperty2) {
		this.floatProperty2 = floatProperty2;
	}

	public Double getDoubleProperty1() {
		return doubleProperty1;
	}

	public void setDoubleProperty1(final Double doubleProperty1) {
		this.doubleProperty1 = doubleProperty1;
	}

	public double getDoubleProperty2() {
		return doubleProperty2;
	}

	public void setDoubleProperty2(final double doubleProperty2) {
		this.doubleProperty2 = doubleProperty2;
	}

	public Short getShortProperty1() {
		return shortProperty1;
	}

	public void setShortProperty1(final Short shortProperty1) {
		this.shortProperty1 = shortProperty1;
	}

	public short getShortProperty2() {
		return shortProperty2;
	}

	public void setShortProperty2(final short shortProperty2) {
		this.shortProperty2 = shortProperty2;
	}

	public Byte getByteProperty1() {
		return byteProperty1;
	}

	public void setByteProperty1(final Byte byteProperty1) {
		this.byteProperty1 = byteProperty1;
	}

	public byte getByteProperty2() {
		return byteProperty2;
	}

	public void setByteProperty2(final byte byteProperty2) {
		this.byteProperty2 = byteProperty2;
	}

	public Boolean getBooleanProperty1() {
		return booleanProperty1;
	}

	public void setBooleanProperty1(final Boolean booleanProperty1) {
		this.booleanProperty1 = booleanProperty1;
	}

	public boolean isBooleanProperty2() {
		return booleanProperty2;
	}

	public void setBooleanProperty2(final boolean booleanProperty2) {
		this.booleanProperty2 = booleanProperty2;
	}

	public Class<?> getClassProperty() {
		return classProperty;
	}

	public void setClassProperty(final Class<?> classProperty) {
		this.classProperty = classProperty;
	}
}
