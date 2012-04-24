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
 * File: RawDocumentLoader.java
 * Created: 2012-01-28
 * Author: Krzywicki
 * $Id: RawDocumentLoader.java 15 2012-01-29 07:43:32Z krzywick $
 */

package org.jage.platform.config.xml.loaders;

import java.io.IOException;
import java.io.InputStream;

import static java.lang.String.format;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import org.jage.platform.component.definition.ConfigurationException;
import org.jage.util.io.IncorrectUriException;
import org.jage.util.io.Resource;
import org.jage.util.io.ResourceLoader;

import com.google.common.io.Closeables;

/**
 * Loads a raw DOM document from the provided resource path. Uses {@link ResourceLoader} to locate the actual resource.
 *
 * @author AGH AgE Team
 */
public final class RawDocumentLoader implements IDocumentLoader {

	private static final Logger LOG = LoggerFactory.getLogger(RawDocumentLoader.class);

	private final SAXReader reader;

	/**
	 * Creates a {@link RawDocumentLoader}.
	 *
	 * @throws ConfigurationException if an error happens when configuring the SAXReader.
	 */
	public RawDocumentLoader() throws ConfigurationException {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setValidating(true);

			SAXParser parser = factory.newSAXParser();
			parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
			        "http://www.w3.org/2001/XMLSchema");
			parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", getClass().getClassLoader()
			        .getResourceAsStream("age.xsd"));

			reader = new SAXReader(parser.getXMLReader());
			reader.setValidation(true);

		} catch (ParserConfigurationException e) {
			throw newConfigurationException(e);
		} catch (SAXException e) {
			throw newConfigurationException(e);
		}
	}

	@Override
	public Document loadDocument(String path) throws ConfigurationException {
		InputStream input = null;
		try {
			Resource resource = ResourceLoader.getResource(path);
			input = resource.getInputStream();
			reader.setErrorHandler(new AgEXmlErrorHandler(path));
			return reader.read(input);
		} catch (IncorrectUriException e) {
			throw new ConfigurationException("Could not locate resource with path " + path, e);
		} catch (IOException e) {
			throw new ConfigurationException("Could not open resource with path " + path, e);
		} catch (DocumentException e) {
			throw new ConfigurationException("Could not read resource with path " + path, e);
		} finally {
			Closeables.closeQuietly(input);
		}
	}

	private ConfigurationException newConfigurationException(Exception e) throws ConfigurationException {
		return new ConfigurationException("Could not instantiate loader", e);
    }

	/**
	 * Custom error handler.
	 *
	 * @author AGH AgE Team
	 */
	private static final class AgEXmlErrorHandler implements ErrorHandler {

		private String path;

		private AgEXmlErrorHandler(String path) {
			this.path = path;
		}

		@Override
		public void warning(SAXParseException e) throws SAXException {
			LOG.warn(createLogString(e));
			throw e;
		}

		@Override
		public void error(SAXParseException e) throws SAXException {
			LOG.error(createLogString(e));
			throw e;
		}

		@Override
		public void fatalError(SAXParseException e) throws SAXException {
			LOG.error(createLogString(e));
			throw e;
		}

		private String createLogString(SAXParseException e) {
			return format("'%1$s':%2$s:%3$s: %4$s", path, e.getLineNumber(), e.getColumnNumber(),
			        cleanMessage(e.getMessage()));
		}

		private String cleanMessage(String message) {
			String[] split = message.split(":");
			String cleanedMessage = split.length > 1 ? split[1] : split[0];
			return cleanedMessage.trim();
		}
	}
}
