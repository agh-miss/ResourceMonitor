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
 * File: CollectionDefinition.java
 * Created: 2010-09-14
 * Author: Kamil
 * $Id: CollectionDefinition.java 59 2012-02-13 13:10:28Z krzywick $
 */

package org.jage.platform.component.definition;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.unmodifiableList;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newLinkedList;

/**
 * The definition of a collection component.
 *
 * @author AGH AgE Team
 */
public class CollectionDefinition extends AbstractComponentDefinition {

	private static final long serialVersionUID = 1L;

	private final List<IArgumentDefinition> items = newLinkedList();

	/**
	 * Creates a new collection definition with a given name, collection type and elements type.
	 *
	 * @param name
	 *            the name of the component
	 * @param type
	 *            the collection type of the component
	 * @param elementsType
	 *            the elements type of the component
	 * @param isSingleton
	 *            whether the component has a singleton scope
	 */
	public CollectionDefinition(String name, @SuppressWarnings("rawtypes") Class<? extends Collection> type,
	        Type elementsType, boolean isSingleton) {
		super(name, type, Collections.singletonList(elementsType), isSingleton);
	}

	/**
	 * Creates a new collection definition with a given name, collection type and default element type.
	 *
	 * @param name
	 *            the name of the component
	 * @param type
	 *            the collection type of the component
	 * @param isSingleton
	 *            whether the component has a singleton scope
	 */
	public CollectionDefinition(String name, @SuppressWarnings("rawtypes") Class<? extends Collection> type,
	        boolean isSingleton) {
		this(name, type, Object.class, isSingleton);
	}

	/**
	 * Returns the elements type of this collection definition.
	 *
	 * @return the elements type of this collection definition
	 */
	public Type getElementsType() {
		return getTypeParameters().get(0);
	}

	/**
	 * Adds an item to this collection definition.
	 *
	 * @param item
	 *            an item
	 */
	public void addItem(IArgumentDefinition item) {
		items.add(checkNotNull(item));
	}

	/**
	 * Returns a read only view of this collection definition's items.
	 *
	 * @return this collection definition's items
	 */
	public List<IArgumentDefinition> getItems() {
		return unmodifiableList(items);
	}
}
