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

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for MetaProperty class.
 * 
 * @author AGH AgE Team
 */
public class MetaPropertyTest {

	private static final String META_PROPERTY_NAME = "name";

	/**
	 * Tests a simple meta property for a non-generic class.
	 * 
	 * @throws PropertyException
	 */
	@Test
	public void testMetaPropertyConstructorNoGeneric() throws PropertyException {
		MetaProperty metaProperty = new MetaProperty(META_PROPERTY_NAME, String.class);
		assertNotNull(metaProperty.getGenericClasses());
		assertTrue(metaProperty.getGenericClasses().isEmpty());
	}
	
	class InnerGenericClass<T> {
		// Empty
	}
	
	class InnerGenericClass2<T, V> {
		// Empty
	}

	@SuppressWarnings("unused")
	class OuterClass {
		
		private InnerGenericClass<?> field1;
		
        private InnerGenericClass2<String, Long> field2;
        
        private InnerGenericClass<? extends Integer> field3;
        
        private InnerGenericClass<List<Set<Integer>>> field4;
		 
	}
	
	/**
	 * Tests a meta property for a simple generic class (T<V>).
	 */
	@Test
	public void testMetaPropertyConstructorSimpleGeneric() throws PropertyException, SecurityException,
	        NoSuchFieldException {

		Field field = OuterClass.class.getDeclaredField("field2");

		MetaProperty metaProperty = new MetaProperty(META_PROPERTY_NAME, field.getGenericType());
		List<Class<?>> classes = metaProperty.getGenericClasses();

		assertNotNull(classes);
		assertTrue(classes.size() == 2);
		assertTrue(classes.contains(String.class));
		assertTrue(classes.contains(Long.class));
	}
	
	/**
	 * Tests a meta property for a nested generic class (T<V<Z>>).
	 */
	@Test
	@Ignore
	// FIXME: For AGE-29
	public void testMetaPropertyConstructorNestedGeneric() throws PropertyException, SecurityException,
	        NoSuchFieldException {

		Field field = OuterClass.class.getDeclaredField("field4");

		MetaProperty metaProperty = new MetaProperty(META_PROPERTY_NAME, field.getGenericType());
		List<Class<?>> classes = metaProperty.getGenericClasses();

		assertNotNull(classes);
		assertTrue(classes.size() == 1);
	
	}
	
	/**
	 * Tests a meta property for a wildcard generic class (T<?>).
	 */
	@Test
	@Ignore
	// FIXME: For AGE-29
	public void testMetaPropertyConstructorWildcardGeneric() throws PropertyException, SecurityException,
	        NoSuchFieldException {

		Field field = OuterClass.class.getDeclaredField("field1");

		MetaProperty metaProperty = new MetaProperty(META_PROPERTY_NAME, field.getGenericType());
		List<Class<?>> classes = metaProperty.getGenericClasses();

		assertNotNull(classes);
		assertTrue(classes.size() == 1);
		
	}

}
