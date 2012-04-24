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
 * File: ConfigurationAssert.java
 * Created: 2009-04-20
 * Author: pkarolcz
 * $Id: ConfigurationAssert.java 183 2012-03-31 20:33:32Z krzywick $
 */

package org.jage.platform.component.definition;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ConfigurationAssert {

	public static void assertConfigurationsEqual(final List<IComponentDefinition> expected,
			final List<IComponentDefinition> actual) {
		assertEquals(expected.size(), actual.size());
		assertObjectDefinitionListsEqual(expected, actual);
	}

	public static void assertObjectDefinitionListsEqual(
			final List<IComponentDefinition> expected, final List<IComponentDefinition> actual) {
		assertEquals(expected.size(), actual.size());
		for (int i = 0; i < expected.size(); i++) {
			assertGenericObjectDefinitionsEqual(expected.get(i), actual.get(i));
		}
	}

	public static void assertGenericObjectDefinitionsEqual(
			final IComponentDefinition expected, final IComponentDefinition actual) {
		assertEquals(expected.getClass(), actual.getClass());
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getType(), actual.getType());
		assertEquals(expected.isSingleton(), actual.isSingleton());

		if (expected instanceof ComponentDefinition) {
			assertObjectDefinitionsEqual((ComponentDefinition) expected,
					(ComponentDefinition) actual);
		}
		if (expected instanceof CollectionDefinition) {
			assertCollectionDefinitionsEqual((CollectionDefinition) expected,
					(CollectionDefinition) actual);
		}

		if (expected instanceof MapDefinition) {
			assertMapDefinitionsEqual((MapDefinition) expected,
					(MapDefinition) actual);
		}
	}

	public static void assertObjectDefinitionsEqual(final ComponentDefinition expected,
			final ComponentDefinition actual) {
		assertEquals(expected.getConstructorArguments().size(), actual
				.getConstructorArguments().size());
		assertEquals(expected.getPropertyArguments().size(), actual
				.getPropertyArguments().size());

		for (int i = 0; i < expected.getConstructorArguments().size(); i++) {
			assertValueProvidersEqual(expected.getConstructorArguments()
					.get(i), actual.getConstructorArguments().get(i));
		}

		for (String key : expected.getPropertyArguments().keySet()) {
			assertValueProvidersEqual(expected.getPropertyArguments()
					.get(key), actual.getPropertyArguments().get(key));
		}

		assertObjectDefinitionListsEqual(expected.getInnerComponentDefinitions(),
				actual.getInnerComponentDefinitions());
	}

	public static void assertCollectionDefinitionsEqual(final CollectionDefinition expected,
			final CollectionDefinition actual) {

		assertEquals(expected.getType(), actual.getType());

		assertEquals(expected.getConstructorArguments().size(), actual
				.getConstructorArguments().size());

		for (int i = 0; i < expected.getConstructorArguments().size(); i++) {
			assertValueProvidersEqual(expected.getConstructorArguments()
					.get(i), actual.getConstructorArguments().get(i));
		}


		assertEquals(expected.getItems().size(), actual.getItems().size());

		for (int i = 0; i < expected.getItems().size(); i++) {
			assertValueProvidersEqual(expected.getItems().get(i), actual
					.getItems().get(i));
		}
		assertObjectDefinitionListsEqual(expected.getInnerComponentDefinitions(),
				actual.getInnerComponentDefinitions());
	}

	public static void assertMapDefinitionsEqual(final MapDefinition expected,
			final MapDefinition actual) {
		assertEquals(expected.getItems(), actual.getItems());
		assertObjectDefinitionListsEqual(expected.getInnerComponentDefinitions(),
				actual.getInnerComponentDefinitions());
	}

	public static void assertValueProvidersEqual(final IArgumentDefinition expected,
			final IArgumentDefinition actual) {
		assertEquals(expected.getClass(), actual.getClass());
		if (expected instanceof ValueDefinition) {
			assertSimpleTypeValueProvidersEqual(
					(ValueDefinition) expected,
					(ValueDefinition) actual);
		}
		if (expected instanceof ReferenceDefinition) {
			assertReferenceProvidersEqual((ReferenceDefinition) expected,
					(ReferenceDefinition) actual);
		}
	}

	public static void assertSimpleTypeValueProvidersEqual(
			final ValueDefinition expected, final ValueDefinition actual) {
		assertEquals(expected.getStringValue(), actual.getStringValue());
		assertEquals(expected.getDesiredClass(), actual.getDesiredClass());
	}

	public static void assertReferenceProvidersEqual(
			final ReferenceDefinition expected, final ReferenceDefinition actual) {
		assertEquals(expected.getTargetName(), actual.getTargetName());
	}
}
