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
 * File: RawDocumentLoaderTest.java
 * Created: 2012-01-28
 * Author: Krzywicki
 * $Id: RawDocumentLoaderTest.java 15 2012-01-29 07:43:32Z krzywick $
 */

package org.jage.platform.config.xml.loaders;

import org.junit.Test;

import org.jage.platform.component.definition.ConfigurationException;

/**
 * Unit tests for RawDocumentLoaderTest.
 *
 * @author AGH AgE Team
 */
public class RawDocumentLoaderTest {

	private final RawDocumentLoader loader;

	public RawDocumentLoaderTest() throws ConfigurationException {
		loader = new RawDocumentLoader();
    }

	@Test
	public void testValidIsParsed() throws ConfigurationException {
		// given
		String path = "classpath:valid.xml";

		// when
		loader.loadDocument(path);
	}

	@Test(expected = ConfigurationException.class)
	public void testInvalidThrowsException() throws ConfigurationException {
		// given
		String path = "classpath:invalid.xml";

		// when
		loader.loadDocument(path);
	}
}
