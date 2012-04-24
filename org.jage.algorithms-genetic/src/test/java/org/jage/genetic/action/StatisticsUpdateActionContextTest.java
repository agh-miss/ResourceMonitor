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
 * File: StatisticsUpdateActionContextTest.java
 * Created: 2011-05-10
 * Author: Krzywicki
 * $Id: StatisticsUpdateActionContextTest.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.genetic.action;

import org.junit.Assert;
import org.junit.Test;

import org.jage.action.IActionContext;

import static org.jage.genetic.action.StatisticsUpdateActionContext.Properties.STATISTICS_UPDATE_ACTION;

/**
 * Test for {@link StatisticsUpdateActionContext}.
 *
 * @author AGH AgE Team
 */
public class StatisticsUpdateActionContextTest {

	@Test
	public void shouldImplementActionContext() {
		// given
		StatisticsUpdateActionContext context = new StatisticsUpdateActionContext();

		// then
		Assert.assertTrue(context instanceof IActionContext);
	}

	@Test
	public void testActionName() {
		Assert.assertEquals("statisticsUpdateAction", STATISTICS_UPDATE_ACTION);
	}
}
