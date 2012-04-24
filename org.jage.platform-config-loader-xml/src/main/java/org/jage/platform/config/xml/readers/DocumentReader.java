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
 * File: DocumentReader.java
 * Created: 2012-01-28
 * Author: Krzywicki
 * $Id: DocumentReader.java 59 2012-02-13 13:10:28Z krzywick $
 */

package org.jage.platform.config.xml.readers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.component.definition.IComponentDefinition;
import org.jage.platform.component.definition.IArgumentDefinition;
import org.jage.platform.config.xml.ConfigTags;
import org.jage.platform.config.xml.ConfigUtils;

import com.google.common.collect.ImmutableMap;

import static com.google.common.collect.Lists.newArrayListWithCapacity;

/**
 * Reader for whole Document trees.
 * <p>
 * Uses {@link MappingDefinitionReader}s to connect suitable readers to reflect the hierarchy of component definition.
 *
 * @author AGH AgE Team
 */
public class DocumentReader {

	private final IDefinitionReader<IComponentDefinition> reader;

	/**
	 * Creates a DocumentReader.
	 */
	public DocumentReader() {
	    this(wireUpReaders());
    }

	DocumentReader(IDefinitionReader<IComponentDefinition> reader) {
	    this.reader = reader;
    }

	/**
	 * Returns a list of definitions for all elements under the root of the provided document.
	 *
	 * @param document
	 *            to document to be read
	 * @return a list of definitions
	 * @throws ConfigurationException
	 *             if an error happens when reading
	 */
	public List<IComponentDefinition> readDocument(Document document) throws ConfigurationException {
		Element root = document.getRootElement();
		List<Element> children = ConfigUtils.getAllChildren(root);
		List<IComponentDefinition> definitions = newArrayListWithCapacity(children.size());
		for (Element element : children) {
			definitions.add(reader.read(element));
		}
		return definitions;
	}

	private static IDefinitionReader<IComponentDefinition> wireUpReaders() {
		ComponentDefinitionReader componentReader = new ComponentDefinitionReader();
		ArrayDefinitionReader arrayReader = new ArrayDefinitionReader();
		CollectionDefinitionReader listReader = new CollectionDefinitionReader(ArrayList.class);
		CollectionDefinitionReader setReader = new CollectionDefinitionReader(HashSet.class);
		MapDefinitionReader mapReader = new MapDefinitionReader(HashMap.class);

		IDefinitionReader<IComponentDefinition> instanceReader = new MappingDefinitionReader<IComponentDefinition>(
		        ImmutableMap.<String, IDefinitionReader<IComponentDefinition>> of(
		        		ConfigTags.COMPONENT.toString(), componentReader,
		        		ConfigTags.ARRAY.toString(), arrayReader,
		        		ConfigTags.LIST.toString(), listReader,
		        		ConfigTags.SET.toString(), setReader,
		        		ConfigTags.MAP.toString(), mapReader));

		IDefinitionReader<IArgumentDefinition> valueReader = new ValueDefinitionReader();
		IDefinitionReader<IArgumentDefinition> referenceReader = new ReferenceDefinitionReader();
		IDefinitionReader<IArgumentDefinition> argumentReader = new MappingDefinitionReader<IArgumentDefinition>(
		        ImmutableMap.of(
		        		ConfigTags.VALUE.toString(), valueReader,
		        		ConfigTags.REFERENCE.toString(), referenceReader));

		componentReader.setArgumentReader(argumentReader);
		arrayReader.setArgumentReader(argumentReader);
		listReader.setArgumentReader(argumentReader);
		setReader.setArgumentReader(argumentReader);
		mapReader.setArgumentReader(argumentReader);

		componentReader.setInstanceReader(instanceReader);
		arrayReader.setInstanceReader(instanceReader);
		listReader.setInstanceReader(instanceReader);
		setReader.setInstanceReader(instanceReader);
		mapReader.setInstanceReader(instanceReader);

		return instanceReader;
	}
}
