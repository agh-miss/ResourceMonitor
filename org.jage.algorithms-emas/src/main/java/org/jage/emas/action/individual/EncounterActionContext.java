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
 * File: EncounterActionContext.java
 * Created: 2012-03-12
 * Author: Krzywicki
 * $Id: EncounterActionContext.java 193 2012-04-05 13:58:36Z krzywick $
 */

package org.jage.emas.action.individual;

import org.jage.action.context.AgentActionContext;
import org.jage.emas.util.ChainingContext;

/**
 * This context defines an encounter action. To use it, you should declare a handler strategy named 'encounterAction'
 * in the workplace context.
 *
 * @author AGH AgE Team
 */
@AgentActionContext(EncounterActionContext.Properties.ENCOUNTER_ACTION)
public class EncounterActionContext extends ChainingContext {
	/**
	 * EncounterActionContext properties.
	 *
	 * @author AGH AgE Team
	 */
	public static class Properties {
		/**
		 * The action name.
		 */
		public static final String ENCOUNTER_ACTION = "encounterAction";
	}
}
