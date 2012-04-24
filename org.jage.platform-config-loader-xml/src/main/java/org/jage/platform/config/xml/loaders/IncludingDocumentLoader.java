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
 * File: IncludingDocumentLoader.java
 * Created: 2012-01-28
 * Author: Krzywicki
 * $Id: IncludingDocumentLoader.java 15 2012-01-29 07:43:32Z krzywick $
 */

package org.jage.platform.config.xml.loaders;

import java.util.List;

import static java.lang.String.format;
import static java.util.Collections.singletonMap;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;

import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.config.xml.ConfigAttributes;
import org.jage.platform.config.xml.ConfigNamespaces;
import org.jage.platform.config.xml.ConfigTags;

import static org.jage.platform.config.xml.ConfigUtils.getRequiredAttribute;

import static com.google.common.collect.Iterables.consumingIterable;

/**
 * This decorator resolves configuration files referred to by {@code <include>} tags.
 *
 * @author AGH AgE Team
 */
public final class IncludingDocumentLoader implements IDocumentLoader {

	private final IDocumentLoader delegate;

	private final XPath xPath;

	/**
	 * Creates a IncludingDocumentLoader.
	 *
	 * @param delegate
	 *            the loader to be decorated
	 */
	public IncludingDocumentLoader(IDocumentLoader delegate) {
		this.delegate = delegate;

		ConfigNamespaces namespace = ConfigNamespaces.DEFAULT;
		xPath = DocumentHelper.createXPath(format("//%1$s:%2$s", namespace.getPrefix(), ConfigTags.INCLUDE.toString()));
		xPath.setNamespaceURIs(singletonMap(namespace.getPrefix(), namespace.getUri()));
	}

	@Override
	@SuppressWarnings("unchecked")
	public Document loadDocument(String path) throws ConfigurationException {
		Document document = delegate.loadDocument(path);

		List<Element> includeElements = xPath.selectNodes(document);
		for (Element include : includeElements) {
			String fileAttribute = getRequiredAttribute(include, ConfigAttributes.FILE);

			Document includedDocument = loadDocument(fileAttribute);
			List<Element> includedElements = includedDocument.getRootElement().elements();

			Element indluding = include.getParent();
			List<Element> includingElements = indluding.elements();
			int includeIndex = includingElements.indexOf(include);
			indluding.remove(include);

			for (Element included : consumingIterable(includedElements)) {
				indluding.elements().add(includeIndex, included);
				includeIndex++;
			}
		}

		return document;
	}
}
