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
package org.jage.config;

import java.util.List;

import junit.framework.Assert;

import org.jage.platform.component.definition.ClassWithProperties;
import org.junit.Test;

/**
 * A sample test case utilising the {@link InstanceProvidingTestCase}.
 * 
 * @author Adam Wos <adam.wos@gmail.com>
 */
public class SampleTestCaseWithInstanceProvider extends InstanceProvidingTestCase {

	public SampleTestCaseWithInstanceProvider() {
		super("classpath:org/jage/config/instProvidingTC.xml");
	}
	
	@Test
	public void basicTest() {
		ClassWithProperties o1 = (ClassWithProperties) getInstance("obj1");
		Assert.assertNotNull(o1);
		Assert.assertEquals(1234, o1.getA());
		Assert.assertEquals(0.0, o1.getB(), 0.0);
	}
	
	@Test
	public void basicTest2() {
		ClassWithProperties o2 = (ClassWithProperties) getInstance("obj2");
		Assert.assertNotNull(o2);
		Assert.assertEquals(0, o2.getA());
		Assert.assertEquals(5678.0, o2.getB(), 0.0001);
		
		List<Object> listProperty = o2.getList();
		Assert.assertEquals(2, listProperty.size());
		
		Object list0 = listProperty.get(0);
		Assert.assertTrue(list0 instanceof ClassWithProperties);
		ClassWithProperties cwp0 = (ClassWithProperties) list0;
		Assert.assertEquals(123, cwp0.getA());
		
		Object list1 = listProperty.get(1);
		Assert.assertTrue(list1 instanceof ClassWithProperties);
		ClassWithProperties cwp1 = (ClassWithProperties) list1;
		Assert.assertEquals(-94, cwp1.getA());
		
	}
}
