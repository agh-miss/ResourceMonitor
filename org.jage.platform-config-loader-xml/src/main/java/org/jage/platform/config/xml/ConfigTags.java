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
 * File: ConfigTags.java
 * Created: 2012-01-28
 * Author: Krzywicki
 * $Id: ConfigTags.java 15 2012-01-29 07:43:32Z krzywick $
 */

package org.jage.platform.config.xml;

/**
 * Enum for tag names in AgE configuration files.
 *
 * @author AGH AgE Team
 */
public enum ConfigTags {

	CONFIGURATION("configuration"),
	COMPONENT("component"),
	AGENT("agent"),
	STRATEGY("strategy"),
	ARRAY("array"),
	LIST("list"),
	SET("set"),
	MAP("map"),
	CONSTRUCTOR_ARG("constructor-arg"),
	PROPERTY("property"),
	VALUE("value"),
	REFERENCE("reference"),
	ITEM("item"),
	ITEM_KEY("itemKey"),
	ITEM_VALUE("itemValue"),
	INCLUDE("include");

	private final String value;

	private ConfigTags(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
