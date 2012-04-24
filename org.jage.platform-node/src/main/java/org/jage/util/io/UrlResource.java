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
 * File: UrlResource.java
 * Created: 2011-08-20
 * Author: faber
 * $Id: UrlResource.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * The representation of a resource that is located under some URL. This class can handle all URLs that {@link URL} can.
 * For the minimal list of protocols see {@link URL#URL(String, String, int, String)}.
 * 
 * @author AGH AgE Team
 */
public class UrlResource implements Resource {

	private URL url;

	/**
	 * Constructs a new URL resource.
	 * 
	 * @param uri
	 *            URI to the resource. It must be a correct URL with a known protocol.
	 * @throws MalformedURLException
	 *             If provided URI is not a correct URL.
	 */
	public UrlResource(String uri) throws MalformedURLException {
		this.url = new URL(uri);
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return url.openStream();
	}

	@Override
	public URI getUri() throws URISyntaxException {
		return url.toURI();
	}

}
