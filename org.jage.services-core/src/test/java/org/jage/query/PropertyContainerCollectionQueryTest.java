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
 * File: PropertyContainerCollectionQueryTest.java
 * Created: 2011-09-19
 * Author: faber
 * $Id: PropertyContainerCollectionQueryTest.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jage.property.ClassPropertyContainer;
import org.jage.property.PropertyField;

/**
 * Tests for the {@link PropertyContainerCollectionQuery} class.
 * 
 * @author AGH AgE Team
 */
public class PropertyContainerCollectionQueryTest {

	private List<ClassPropertyContainer> target;

	private PropertyContainerCollectionQuery<ClassPropertyContainer, ClassPropertyContainer> query;

	@Before
	public void setUp() {
		query = new PropertyContainerCollectionQuery<ClassPropertyContainer, ClassPropertyContainer>(
		        ClassPropertyContainer.class, Collection.class, ArrayList.class);

		target = new ArrayList<ClassPropertyContainer>();
		target.add(new ClassPropertyContainer() {
			@SuppressWarnings("unused")
			@PropertyField(propertyName = "lorem")
			private String lorem = "ipsum";
		});
		target.add(new ClassPropertyContainer() {
			@SuppressWarnings("unused")
			@PropertyField(propertyName = "lorem")
			private String lorem = "dolor";
		});
		target.add(new ClassPropertyContainer() {
			@SuppressWarnings("unused")
			@PropertyField(propertyName = "lorem")
			private String lorem = "sit";
		});
	}

	/**
	 * Tests the specialised {@link PropertyContainerCollectionQuery#matching(String, IValueFilter)} method for querying
	 * property values.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testMatchingForProperty() {
		IValueFilter<String> valueFilterMock = mock(IValueFilter.class);
		when(valueFilterMock.matches("ipsum")).thenReturn(true);
		query.matching("lorem", valueFilterMock);

		Collection<ClassPropertyContainer> result = query.execute(target);
		assertEquals(target.subList(0, 1), result);
	}

}
