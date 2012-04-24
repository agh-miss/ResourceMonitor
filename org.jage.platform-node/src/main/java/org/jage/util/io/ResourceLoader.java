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
 * File: ResourceLoader.java
 * Created: 2011-08-20
 * Author: faber
 * $Id: ResourceLoader.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.util.io;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * A simple resource loader.
 * <p>
 * At least following protocols are correctly recognised by this loader:
 * <ul>
 * <li>file
 * <li>http
 * <li>https
 * <li>ftp
 * <li>jar
 * </ul>
 * More schemes can be recognised if more handlers were registered for {@link java.net.URL} class. Additionally,
 * relative paths without a declared scheme (e.g. <code>../some/file</code>) are recognised for backward compatibility
 * and are interpreted as relative to the current working directory.
 * 
 * @author AGH AgE Team
 */
public class ResourceLoader {

	/**
	 * Loads and returns a resource specified by the provided string representation of the URI.
	 * 
	 * @param uri
	 *            A correct URI (not <code>null</code> nor empty string).
	 * @return A resource equivalent of the URI.
	 * @throws IncorrectUriException
	 *             if the URI could not be parsed or the scheme was not recognised.
	 */
	public static Resource getResource(String uri) throws IncorrectUriException {
		if (uri == null || uri.isEmpty()) {
			throw new IllegalArgumentException("URI cannot be null nor empty.");
		}

		if (uri.startsWith(ClasspathResource.CLASSPATH_SCHEME)) {
			try {
				return new ClasspathResource(uri);
			} catch (UnknownSchemeException e) {
				// If we are here it is very strange.
				assert false;
			}
		}

		try {
			URI parsedUri = new URI(uri);
			if (parsedUri.getScheme() == null) {
				return new UrlResource("file:" + uri);
			}
			return new UrlResource(uri);

		} catch (MalformedURLException e) {
			throw new IncorrectUriException(e);
		} catch (URISyntaxException e) {
			throw new IncorrectUriException(e);
		}
	}
}
