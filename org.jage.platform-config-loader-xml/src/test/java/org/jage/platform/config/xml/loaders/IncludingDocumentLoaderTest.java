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
 * File: IncludingDocumentLoaderTest.java
 * Created: 2012-01-28
 * Author: Krzywicki
 * $Id: IncludingDocumentLoaderTest.java 15 2012-01-29 07:43:32Z krzywick $
 */

package org.jage.platform.config.xml.loaders;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.util.NodeComparator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.config.xml.ConfigAttributes;
import org.jage.platform.config.xml.ConfigTags;

import static org.jage.platform.config.xml.ConfigTestUtils.createEmptyDocument;

/**
 * Unit tests for IncludingDocumentLoader.
 *
 * @author AGH AgE Team
 */
public class IncludingDocumentLoaderTest  {

	private IDocumentLoader delegate;

	private IncludingDocumentLoader loader;

	public IncludingDocumentLoaderTest() {
		delegate = mock(IDocumentLoader.class);
		loader = new IncludingDocumentLoader(delegate);
    }

	@Test
	public void testIgnoresEmptyDocument() throws ConfigurationException {
		// given
		String path = "somePath";
		Document original = createEmptyDocument();
		given(delegate.loadDocument(path)).willReturn(original);

		// when
		Document document = loader.loadDocument(path);

		// then
		assertEquals(0, new NodeComparator().compare(original, document));
	}

	@Test
	public void testIncludesSpecifiedDocuments() throws ConfigurationException {
		// given
		String path = "somePath";
		String includingPath = "includingPath";
		Document includingDocument = createIncludingDocument(includingPath);
		Document includedDocument = createIncludedDocument();
		Document expectedDocument = createFullDocument();
		given(delegate.loadDocument(path)).willReturn(includingDocument);
		given(delegate.loadDocument(includingPath)).willReturn(includedDocument);

		// when
		Document document = loader.loadDocument(path);

		// then
		assertEquals(0, new NodeComparator().compare(expectedDocument, document));
	}

	private Document createIncludingDocument(String path) {
		Document document = createEmptyDocument();
		Element root = document.getRootElement();
		root.addElement(ConfigTags.ARRAY.toString());
		root.addElement(ConfigTags.INCLUDE.toString())
				.addAttribute(ConfigAttributes.FILE.toString(), path);
		root.addElement(ConfigTags.LIST.toString());
	    return document;
    }

	private Document createIncludedDocument() {
		Document document = createEmptyDocument();
		Element root = document.getRootElement();
		root.addElement(ConfigTags.COMPONENT.toString());
		root.addElement(ConfigTags.MAP.toString());
	    return document;
    }

	private Document createFullDocument() {
		Document document = createEmptyDocument();
		Element root = document.getRootElement();
		root.addElement(ConfigTags.ARRAY.toString());
		root.addElement(ConfigTags.COMPONENT.toString());
		root.addElement(ConfigTags.MAP.toString());
		root.addElement(ConfigTags.LIST.toString());
	    return document;
    }
}
