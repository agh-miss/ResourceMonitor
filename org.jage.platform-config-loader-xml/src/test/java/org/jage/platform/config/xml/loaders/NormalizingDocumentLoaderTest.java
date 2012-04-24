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
 * File: NormalizingDocumentLoaderTest.java
 * Created: 2012-01-28
 * Author: Krzywicki
 * $Id: NormalizingDocumentLoaderTest.java 15 2012-01-29 07:43:32Z krzywick $
 */

package org.jage.platform.config.xml.loaders;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.util.NodeComparator;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.config.xml.ConfigAttributes;
import org.jage.platform.config.xml.ConfigTags;

import static org.jage.platform.config.xml.ConfigTestUtils.createEmptyDocument;

/**
 * Unit tests for NormalizingDocumentLoaderTest.
 *
 * @author AGH AgE Team
 */
public class NormalizingDocumentLoaderTest  {

	private IDocumentLoader delegate;

	private NormalizingDocumentLoader loader;

	public NormalizingDocumentLoaderTest() {
		delegate = mock(IDocumentLoader.class);
		loader = new NormalizingDocumentLoader(delegate);
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
	public void testReplaceAgentDefinitions() throws ConfigurationException {
		// given
		String path = "somePath";
		Document original = createEmptyDocument();
		original.getRootElement().addElement(ConfigTags.AGENT.toString());
		given(delegate.loadDocument(path)).willReturn(original);

		// when
		Document document = loader.loadDocument(path);

		// then
		Element element = document.getRootElement().element(ConfigTags.COMPONENT.toString());
		assertNotNull(element);
		assertThat(element.attributeValue(ConfigAttributes.IS_SINGLETON.toString()), is("false"));
	}

	@Test
	public void testReplaceStrategyDefinitions() throws ConfigurationException {
		// given
		String path = "somePath";
		Document original = createEmptyDocument();
		original.getRootElement().addElement(ConfigTags.STRATEGY.toString());
		given(delegate.loadDocument(path)).willReturn(original);

		// when
		Document document = loader.loadDocument(path);

		// then
		Element element = document.getRootElement().element(ConfigTags.COMPONENT.toString());
		assertNotNull(element);
		assertThat(element.attributeValue(ConfigAttributes.IS_SINGLETON.toString()), is("true"));
	}


}
