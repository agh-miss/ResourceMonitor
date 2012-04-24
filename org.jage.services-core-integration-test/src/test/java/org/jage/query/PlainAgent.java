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
 * File: PlainAgent.java
 * Created: 2011-09-20
 * Author: faber
 * $Id: PlainAgent.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.query;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

import org.jage.agent.SimpleAgent;
import org.jage.platform.component.definition.ConfigurationException;
import org.jage.property.PropertyField;

/**
 * Helper agent with lots of properties.
 * 
 * @author AGH AgE Team
 */
@SuppressWarnings("javadoc")
public class PlainAgent extends SimpleAgent {

	private static final long serialVersionUID = 1L;

	@PropertyField(propertyName = "width")
	private float width;

	@PropertyField(propertyName = "height")
	private float height;

	@PropertyField(propertyName = "weight")
	private double weight;

	@PropertyField(propertyName = "upperLimit")
	private BigInteger upperLimit;

	@PropertyField(propertyName = "bestValue")
	private BigDecimal bestValue;

	private long stepCount;

	@PropertyField(propertyName = "staticFactor")
	private int staticFactor;

	@PropertyField(propertyName = "dynamicFactor")
	private short dynamicFactor;

	@PropertyField(propertyName = "mode")
	private byte mode;

	@PropertyField(propertyName = "description")
	private String description;

	@PropertyField(propertyName = "color")
	private Color color;

	private Collection<BitSet> solutions;

	private BitSet bestCode;

	private Random random;

	/**
	 * @throws ConfigurationException
	 */
	public PlainAgent() throws ConfigurationException {
		stepCount = 0;
		bestValue = new BigDecimal(0);
		bestCode = new BitSet();
		solutions = new LinkedList<BitSet>();
		random = new Random(Calendar.getInstance().getTimeInMillis());
	}

	/*
	 * @see org.jage.core.Agent#step()
	 */
	@Override
	public void step() {
		stepCount++;
		bestValue = new BigDecimal(random.nextDouble() * stepCount);
		bestCode.flip((int)(stepCount / ((random.nextLong() % stepCount) + 1)));
		if (bestValue.doubleValue() > 5.0 && bestValue.doubleValue() < 10.0) {
			solutions.add((BitSet)bestCode.clone());
		}
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public short getDynamicFactor() {
		return dynamicFactor;
	}

	public void setDynamicFactor(short dynamicFactor) {
		this.dynamicFactor = dynamicFactor;
	}

	public byte getMode() {
		return mode;
	}

	public void setMode(byte mode) {
		this.mode = mode;
	}

	public int getStaticFactor() {
		return staticFactor;
	}

	public void setStaticFactor(int staticFactor) {
		this.staticFactor = staticFactor;
	}

	public BigInteger getUpperLimit() {
		return upperLimit;
	}

	public void setUpperLimit(BigInteger upperLimit) {
		this.upperLimit = upperLimit;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public BitSet getBestCode() {
		return bestCode;
	}

    public BigDecimal getBestValue() {
		return bestValue;
	}

	public Collection<BitSet> getSolutions() {
		return solutions;
	}

	public long getStepCount() {
		return stepCount;
	}

}
