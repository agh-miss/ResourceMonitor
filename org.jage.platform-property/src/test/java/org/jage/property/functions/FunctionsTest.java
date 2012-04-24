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
package org.jage.property.functions;

import junit.framework.TestCase;

import org.junit.Test;

public class FunctionsTest extends TestCase {

	@Test
	public void testSumFunction() throws Exception {
		FunctionsTestPropertyContainer propertyContainer = new FunctionsTestPropertyContainer();
		propertyContainer.addProperty("doubleProperty1", new Double(2.0));
		propertyContainer.addProperty("intProperty", new Integer(1));
		propertyContainer.addProperty("doubleProperty2", new Double(3.0));
		SumFunction function = new SumFunction("sum", "doubleProperty");
		function.setArgumentsResolver(propertyContainer);

		double functionValue = ((Double) function.getValue()).doubleValue();
		assertEquals(5.0, functionValue);
		assertTrue(function.getMetaProperty().isMonitorable());
	}

	@Test
	public void testAvgFunction() throws Exception {
		FunctionsTestPropertyContainer container = new FunctionsTestPropertyContainer();
		container.addProperty("a_doubleProperty1", new Double(2.0));
		container.addProperty("a_intProperty", new Integer(1));
		container.addProperty("a_doubleProperty2", new Double(3.0));
		container.addProperty("a_floatProperty", new Float(2.0f));
		container.addProperty("b_double", new Double(4.0));

		AvgFunction function1 = new AvgFunction("avg", "a_", 1.0);
		function1.setArgumentsResolver(container);
		assertEquals(Double.class, function1.getMetaProperty()
				.getPropertyClass());
		assertEquals(2.0, ((Double) function1.getValue()).doubleValue());
		assertTrue(function1.getMetaProperty().isMonitorable());

		AvgFunction function2 = new AvgFunction("avg", "c_", 0.44);
		function2.setArgumentsResolver(container);
		assertEquals(0.44, ((Double) function2.getValue()).doubleValue());
		assertTrue(function2.getMetaProperty().isMonitorable());
	}

	@Test
	public void testCountFunction() throws Exception {
		FunctionsTestPropertyContainer propertyContainer = new FunctionsTestPropertyContainer();
		propertyContainer.addProperty("a_doubleProperty1", new Double(2.0));
		propertyContainer.addProperty("a_intProperty", new Integer(1));
		propertyContainer.addProperty("a_doubleProperty2", new Double(3.0));
		propertyContainer.addProperty("a_floatProperty", new Float(2.0f));
		propertyContainer.addProperty("b_double", new Double(4.0));

		CountFunction function1 = new CountFunction("avg", "a_");
		function1.setArgumentsResolver(propertyContainer);
		assertEquals(4, function1.getValue());
		assertTrue(function1.getMetaProperty().isMonitorable());

		CountFunction function2 = new CountFunction("avg", "c_");
		function2.setArgumentsResolver(propertyContainer);
		assertEquals(0, function2.getValue());
		assertTrue(function2.getMetaProperty().isMonitorable());
	}
}
