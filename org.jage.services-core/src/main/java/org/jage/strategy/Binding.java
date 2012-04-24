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
/**
 * Evol Builder
 */
/*
 * File: Binding.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: Binding.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.strategy;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation definition. Defines the binding between two components.
 * 
 * Inherited Optional
 * 
 * @author AGH AgE Team
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Binding {
	Class<?>[] type() default Binding.class;

	Class<?>[] ref();
}
