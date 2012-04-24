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
 * File: DocumentReaderTest.java
 * Created: 2012-01-28
 * Author: Krzywicki
 * $Id: DocumentReaderTest.java 15 2012-01-29 07:43:32Z krzywick $
 */

package org.jage.platform.config.xml.readers;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.junit.Test;

import static org.dom4j.DocumentHelper.createElement;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.component.definition.IComponentDefinition;

import static org.jage.platform.config.xml.ConfigTestUtils.createEmptyDocument;

/**
 * Unit tests for DocumentReader.
 *
 * @author AGH AgE Team
 */
public class DocumentReaderTest {

	@Test
	public void testDelegates() throws ConfigurationException {
		// given
		Element firstElement = createElement("first");
		Element secondElement = createElement("second");

		IComponentDefinition firstDef = mock(IComponentDefinition.class);
		IComponentDefinition secondDef = mock(IComponentDefinition.class);

		@SuppressWarnings("unchecked")
		IDefinitionReader<IComponentDefinition> mock = mock(IDefinitionReader.class);
		given(mock.read(firstElement)).willReturn(firstDef);
		given(mock.read(secondElement)).willReturn(secondDef);

		Document document = createEmptyDocument();
		document.getRootElement().add(firstElement);
		document.getRootElement().add(secondElement);

		DocumentReader reader = new DocumentReader(mock);

		// when
		List<IComponentDefinition> list = reader.readDocument(document);

		// then
		assertNotNull(list);
		assertThat(list.size(), is(2));
		assertThat(list.get(0), is(firstDef));
		assertThat(list.get(1), is(secondDef));
	}
}
