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
 * File: UpdateStatisticsActionContext.java
 * Created: 2011-05-10
 * Author: Krzywicki
 * $Id: StatisticsUpdateActionContext.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.genetic.action;

import org.jage.action.IActionContext;
import org.jage.action.context.AgentActionContext;

/**
 * This context defines a statistics update action. To use it, you should declare a handler bean named 'statisticsUpdateAction'
 * in the workplace context.
 *
 * @author AGH AgE Team
 */
@AgentActionContext(StatisticsUpdateActionContext.Properties.STATISTICS_UPDATE_ACTION)
public final class StatisticsUpdateActionContext implements IActionContext {
	/**
	 * UpdateStatisticsActionContext properties.
	 *
	 * @author AGH AgE Team
	 */
	public static class Properties {
		/**
		 * The action name.
		 */
		public static final String STATISTICS_UPDATE_ACTION = "statisticsUpdateAction";
	}
}
