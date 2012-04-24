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
 * File: Resource.java
 * Created: 2011-08-20
 * Author: faber
 * $Id: Resource.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * The interface for common operations on many different resources.
 * 
 * @author AGH AgE Team
 */
public interface Resource {

	/**
	 * Returns an input stream backed by this resource.
	 * 
	 * @return An input stream, never <code>null</code>.
	 * @throws IOException
	 *             If the input stream could not be obtained.
	 */
	InputStream getInputStream() throws IOException;

	/**
	 * Returns a URI of this resource.
	 * 
	 * @return A URI of this resource.
	 * @throws URISyntaxException
	 *             When a location of the resource could not be parsed into correct URI.
	 * 
	 * @see URI
	 */
	URI getUri() throws URISyntaxException;
}
