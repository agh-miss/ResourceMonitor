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
 * File: IDocumentLoader.java
 * Created: 2012-01-28
 * Author: Krzywicki
 * $Id: IDocumentLoader.java 15 2012-01-29 07:43:32Z krzywick $
 */

package org.jage.platform.config.xml.loaders;

import org.dom4j.Document;

import org.jage.platform.component.definition.ConfigurationException;
import org.jage.util.io.ResourceLoader;

/**
 * Interface from loading a DOM document from some resource path. It is intended to be chained as consecutive decorators
 * applying changes to the resulting tree.
 *
 * @see ResourceLoader
 *
 * @author AGH AgE Team
 */
public interface IDocumentLoader {

	/**
	 * Loads a Document from the given resource path.
	 *
	 * @param path the path to the resource
	 * @return a DOM Document
	 * @throws ConfigurationException if an error happens during the loading
	 */
	public Document loadDocument(String path) throws ConfigurationException;
}
