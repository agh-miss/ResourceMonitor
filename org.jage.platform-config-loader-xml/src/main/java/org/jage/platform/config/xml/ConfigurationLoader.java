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
 * File: ConfigurationLoader.java
 * Created: 2012-01-28
 * Author: Krzywicki
 * $Id: ConfigurationLoader.java 15 2012-01-29 07:43:32Z krzywick $
 */

package org.jage.platform.config.xml;

import java.util.List;

import org.dom4j.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.component.definition.IComponentDefinition;
import org.jage.platform.config.loader.IConfigurationLoader;
import org.jage.platform.config.xml.loaders.IDocumentLoader;
import org.jage.platform.config.xml.loaders.IncludingDocumentLoader;
import org.jage.platform.config.xml.loaders.NormalizingDocumentLoader;
import org.jage.platform.config.xml.loaders.RawDocumentLoader;
import org.jage.platform.config.xml.readers.DocumentReader;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Implementation of {@link IConfigurationLoader}. It uses a decorated chain of {@link IDocumentLoader} to load a
 * resolved, normalized Document from some resource path, then a composed IDefinitionReader to read definitions from the
 * DOM tree.
 *
 * @author AGH AgE Team
 */
public final class ConfigurationLoader implements IConfigurationLoader {

	private static final Logger LOG = LoggerFactory.getLogger(ConfigurationLoader.class);

	private final DocumentReader reader;

	private final IDocumentLoader loader;

	/**
	 * Creates a ConfigurationLoader.
	 *
	 * @throws ConfigurationException
	 *             if something goes wrong during construction.
	 */
	public ConfigurationLoader() throws ConfigurationException {
		this(new NormalizingDocumentLoader(new IncludingDocumentLoader(new RawDocumentLoader())), new DocumentReader());
	}

	ConfigurationLoader(IDocumentLoader loader, DocumentReader reader) {
		this.loader = loader;
		this.reader = reader;
	}

	@Override
	public List<IComponentDefinition> loadConfiguration(Object source) throws ConfigurationException {
		checkArgument(source instanceof String);
		String path = (String)source;

		LOG.debug("Loading document from '{}'", path);
		Document document = loader.loadDocument(path);

		List<IComponentDefinition> definitions = reader.readDocument(document);
		LOG.debug("Read {} component definitions.", definitions.size());

		return definitions;
	}
}
