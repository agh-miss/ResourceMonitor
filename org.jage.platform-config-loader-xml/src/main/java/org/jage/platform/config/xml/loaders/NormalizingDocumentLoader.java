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
 * File: NormalizingDocumentLoader.java
 * Created: 2012-01-28
 * Author: Krzywicki
 * $Id: NormalizingDocumentLoader.java 16 2012-01-29 14:39:31Z krzywick $
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
import org.jage.platform.config.xml.ConfigNamespaces;
import org.jage.platform.config.xml.ConfigTags;

import static org.jage.platform.config.xml.ConfigAttributes.IS_SINGLETON;
import static org.jage.platform.config.xml.ConfigTags.COMPONENT;

/**
 * This decorator transforms {@code <agent>} and {@code <strategy>} tags to {@code <component>} ones, with appropriate
 * attributes.
 *
 * @author AGH AgE Team
 */
public final class NormalizingDocumentLoader implements IDocumentLoader {

	private final IDocumentLoader delegate;

	private final XPath agentXPath;

	private final XPath strategyXPath;

	/**
	 * Creates a NormalizingDocumentLoader.
	 *
	 * @param delegate
	 *            the loader to be decorated
	 */
	public NormalizingDocumentLoader(IDocumentLoader delegate) {
		this.delegate = delegate;

		ConfigNamespaces namespace = ConfigNamespaces.DEFAULT;
		agentXPath = createXPath(namespace, ConfigTags.AGENT);
		strategyXPath = createXPath(namespace, ConfigTags.STRATEGY);
	}

	private XPath createXPath(ConfigNamespaces namespace, ConfigTags tag) {
		XPath xpath = DocumentHelper.createXPath(format("//%1$s:%2$s", namespace.getPrefix(), tag.toString()));
		xpath.setNamespaceURIs(singletonMap(namespace.getPrefix(), namespace.getUri()));
		return xpath;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Document loadDocument(String path) throws ConfigurationException {
		Document document = delegate.loadDocument(path);

		for (Element element : (List<Element>)agentXPath.selectNodes(document)) {
			element.setName(COMPONENT.toString());
			element.addAttribute(IS_SINGLETON.toString(), "false");
		}
		for (Element element : (List<Element>)strategyXPath.selectNodes(document)) {
			element.setName(COMPONENT.toString());
			element.addAttribute(IS_SINGLETON.toString(), "true");
		}
		return document;
	}
}
