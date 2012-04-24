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
 * File: ClasspathResource.java
 * Created: 2011-08-20
 * Author: faber
 * $Id: ClasspathResource.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.util.io;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * The representation of a resource that is located somewhere in the classpath. This class can only handle URIs with
 * {@link #CLASSPATH_SCHEME} scheme.
 * 
 * @author AGH AgE Team
 */
public class ClasspathResource implements Resource {

	/**
	 * URI scheme of classpath resources.
	 * <p>
	 * Please, note that this contains a following colon.
	 */
	public static final String CLASSPATH_SCHEME = "classpath:";

	private static final int SCHEME_LENGTH = CLASSPATH_SCHEME.length();

	private ClassLoader classLoader;

	private String uri;

	/**
	 * Constructs a new classpath resource.
	 * 
	 * @param uri
	 *            URI to the file. It must start with {@link #CLASSPATH_SCHEME}.
	 * @throws UnknownSchemeException
	 *             If URI does not start with {@link #CLASSPATH_SCHEME}.
	 */
	public ClasspathResource(String uri) throws UnknownSchemeException {
		if (!uri.startsWith(CLASSPATH_SCHEME)) {
			throw new UnknownSchemeException(String.format("Scheme of %s URI is not known. %s was expected.", uri,
			        CLASSPATH_SCHEME));
		}
		this.classLoader = getDefaultClassLoader();
		this.uri = uri;
	}

	@Override
	public InputStream getInputStream() throws FileNotFoundException {
		InputStream is = classLoader.getResourceAsStream(uri.substring(SCHEME_LENGTH));
		if (is == null) {
			throw new FileNotFoundException(String.format("Requested file %s could not be found.", uri));
		}
		return is;
	}

	@Override
	public URI getUri() throws URISyntaxException {
		return new URI(uri);
	}

	/**
	 * Returns a default class loader.
	 * 
	 * @return A default (global if possible) class loader.
	 */
	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (SecurityException ex) {
			// Empty
		}
		if (cl == null) {
			cl = ClasspathResource.class.getClassLoader();
		}
		return cl;
	}

}
