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
package org.jage.property.monitors;

import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultPropertyMonitorRuleTest {

	@Test
	public void testDefaultPropertyMonitorRule() {
		DefaultPropertyMonitorRule rule = new DefaultPropertyMonitorRule();
		assertTrue(rule.isActive(1, 2));
		assertTrue(rule.isActive("aa", "bb"));
		assertTrue(rule.isActive(new Object(), new Object()));
		assertTrue(rule.isActive("same", "same"));
	}
}
