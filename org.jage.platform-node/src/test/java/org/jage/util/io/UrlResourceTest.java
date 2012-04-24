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
 * File: UrlResourceTest.java
 * Created: 2011-08-20
 * Author: faber
 * $Id: UrlResourceTest.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.util.io;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests for the {@link ClasspathResource} class.
 *
 * @author AGH AgE Team
 */
public class UrlResourceTest {

	private static final String FILE_RESOURCE_EXISTING_REL = "testResource.res";

	private static final String HTTP_RESOURCE_EXISTING = "http://google.com/";

	private static final String RESOURCE_NOT_EXISTING = "file:///this/does/not/exist.s";

	private static final String RESOURCE_INCORRECT_URI = "unknown://google.com/";

	/**
	 * Tests getting of an input stream for an existing resource in the file: scheme.
	 * <p>
	 * TO be able to run this test both form Maven and Eclipse, we firstly locate the file in the classpath and then we
	 * obtain its absolute path.
	 */
	@Test
	public void testGetInputStreamForAbsoluteFile() throws IOException {
		String absoluteFileUri = getClass().getResource(FILE_RESOURCE_EXISTING_REL).toExternalForm();
		UrlResource urlResource = new UrlResource(absoluteFileUri);
		InputStream inputStream = urlResource.getInputStream();
		assertNotNull(inputStream);
	}

	/**
	 * Tests getting of a URI for an existing resource.
	 * <p>
	 *
	 * @see #testGetInputStreamForAbsoluteFile()
	 */
	@Test
	public void testGetUriForAbsoluteFile() throws MalformedURLException, URISyntaxException {
		String absoluteFileUri = getClass().getResource(FILE_RESOURCE_EXISTING_REL).toExternalForm();
		UrlResource urlResource = new UrlResource(absoluteFileUri);
		assertNotNull(urlResource.getUri());
	}

	/**
	 * Tests getting of an input stream for an existing resource in the http: scheme.
	 * <p>
	 * Warning: this test requires an Internet connection. This is why it is @Ignore-d.
	 */
	@Test
	@Ignore
	public void testGetInputStreamForHttp() throws IOException {
		UrlResource urlResource = new UrlResource(HTTP_RESOURCE_EXISTING);
		InputStream inputStream = urlResource.getInputStream();
		assertNotNull(inputStream);
	}

	/**
	 * Tests getting of a URI for a HTTP resource.
	 */
	@Test
	public void testGetUriForHttp() throws MalformedURLException, URISyntaxException {
		UrlResource urlResource = new UrlResource(HTTP_RESOURCE_EXISTING);
		assertNotNull(urlResource.getUri());
	}

	/**
	 * Tests handling of a not existing resource.
	 *
	 * @throws IOException
	 */
	@Test(expected = IOException.class)
	public void testNotExisitngResource() throws URISyntaxException, UnknownSchemeException, IOException {
		UrlResource urlResource = new UrlResource(RESOURCE_NOT_EXISTING);

		assertNull(urlResource.getInputStream());
		assertNotNull(urlResource.getUri());
	}

	/**
	 * Tests handling of incorrect URIs.
	 *
	 * @throws MalformedURLException
	 */
	@SuppressWarnings("unused")
	@Test(expected = MalformedURLException.class)
	public void testIncorrectUriResource() throws MalformedURLException {
		UrlResource urlResource = new UrlResource(RESOURCE_INCORRECT_URI);
	}

}
