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
 * File: package-info.java
 * Created: 2011-10-20
 * Author: faber
 * $Id: package-info.java 166 2012-03-30 08:26:53Z faber $
 */

/**
 * Provides action mechanism for agents.
 * <p>
 * Actions are task which can be requested by an agent to its parent. Usually are executed on invoking agent by parent
 * agent (f.e. agent migration, agent clone, agent die). Sometimes actions such as getting resources can be executed on
 * different agents.
 * Each agent structure change is proceeded by action executing. Actions are considered only for lightweight agents.
 */
package org.jage.action;

