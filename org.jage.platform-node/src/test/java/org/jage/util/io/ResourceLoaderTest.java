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
 * File: ResourceLoaderTest.java
 * Created: 2011-08-20
 * Author: faber
 * $Id: ResourceLoaderTest.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.util.io;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link ResourceLoader} class.
 * 
 * @author AGH AgE Team
 */
public class ResourceLoaderTest {

	private static final String CLASSPATH_RESOURCE = "classpath:org/jage/util/io/testResource.res";

	private static final String FILE_RESOURCE = "file:///this/does/not/exist.s";

	private static final String RELATIVE_RESOURCE = "some/dir/exist.s";

	private static final String INCORRECT_RESOURCE = "incorrect:scheme";

	/**
	 * Tests obtaining resources for correct URIs.
	 */
	@Test
	public void testCorrectGetResource() throws IncorrectUriException {
		assertTrue(ResourceLoader.getResource(CLASSPATH_RESOURCE) instanceof ClasspathResource);

		assertTrue(ResourceLoader.getResource(FILE_RESOURCE) instanceof UrlResource);

		assertTrue(ResourceLoader.getResource(RELATIVE_RESOURCE) instanceof UrlResource);
	}

	/**
	 * Tests obtaining resources for incorrect URIs.
	 */
	@Test(expected = IncorrectUriException.class)
	public void testIncorrectGetResource() throws IncorrectUriException {
		ResourceLoader.getResource(INCORRECT_RESOURCE);
	}

	/**
	 * Tests obtaining a resource for null URI.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNullGetResource() throws IncorrectUriException {
		ResourceLoader.getResource(null);
	}

	/**
	 * Tests obtaining a resource for an empty string.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testEmptyGetResource() throws IncorrectUriException {
		ResourceLoader.getResource("");
	}
}
