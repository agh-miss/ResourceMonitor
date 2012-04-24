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
 * File: ISelfAwareComponentInstanceProvider.java
 * Created: 2010-04-29
 * Author: kpietak
 * $Id: ISelfAwareComponentInstanceProvider.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.platform.component.provider;

/**
 * A {@link IComponentInstanceProvider} which supplies a default component instance. This provider can return the
 * component instance.
 * 
 * @author AGH AgE Team
 */
public interface ISelfAwareComponentInstanceProvider extends IComponentInstanceProvider {

	/**
	 * Returns the component instance which is supplied by provider by default.
	 * 
	 * @return required object or null if object not found
	 */
	public Object getInstance();

	/**
	 * Returns name of component which is supplied by provider by default.
	 * 
	 * @return component name
	 */
	public String getName();

}
