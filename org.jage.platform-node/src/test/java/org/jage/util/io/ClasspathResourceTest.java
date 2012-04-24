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
 * File: ClasspathResourceTest.java
 * Created: 2011-08-20
 * Author: faber
 * $Id: ClasspathResourceTest.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.util.io;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Tests for the {@link ClasspathResource} class.
 * 
 * @author AGH AgE Team
 */
public class ClasspathResourceTest {

	private static final String RESOURCE_EXISTING = "classpath:org/jage/util/io/testResource.res";

	private static final String RESOURCE_NOT_EXISTING = "classpath:org/jage/util/io/noResource.res";

	private static final String RESOURCE_INCORRECT_URI = "claspath:org/jage/util/io/testResource.res";

	/**
	 * Tests getting of an input stream for an existing resource.
	 */
	@Test
	public void testGetInputStream() throws UnknownSchemeException, FileNotFoundException {
		ClasspathResource classpathResource = new ClasspathResource(RESOURCE_EXISTING);
		InputStream inputStream = classpathResource.getInputStream();
		assertNotNull(inputStream);
	}

	/**
	 * Tests getting of a URI for an existing resource.
	 */
	@Test
	public void testGetUri() throws URISyntaxException, UnknownSchemeException {
		ClasspathResource classpathResource = new ClasspathResource(RESOURCE_EXISTING);
		assertNotNull(classpathResource.getUri());
	}

	/**
	 * Tests handling of a not existing resource.
	 */
	@Test(expected = FileNotFoundException.class)
	public void testNotExisitngResource() throws FileNotFoundException, UnknownSchemeException, URISyntaxException {
		ClasspathResource classpathResource = new ClasspathResource(RESOURCE_NOT_EXISTING);

		assertNotNull(classpathResource.getUri());
		classpathResource.getInputStream(); // Should throw
	}

	/**
	 * Tests handling of incorrect URIs.
	 */
	@SuppressWarnings("unused")
	@Test(expected = UnknownSchemeException.class)
	public void testIncorrectUriResource() throws UnknownSchemeException {
		ClasspathResource classpathResource = new ClasspathResource(RESOURCE_INCORRECT_URI); // Should throw
	}

}
