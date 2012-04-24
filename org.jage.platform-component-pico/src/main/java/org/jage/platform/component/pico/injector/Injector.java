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
 * File: Injector.java
 * Created: 2010-09-14
 * Author: Kamil
 * $Id: Injector.java 124 2012-03-18 10:27:39Z krzywick $
 */

package org.jage.platform.component.pico.injector;

import java.lang.reflect.Type;

/**
 * A component adapter that is responsible for instantiating components and injecting dependencies.
 *
 * @param <T>
 *            the type of components this injector can create or inject into
 *
 * @author AGH AgE Team
 */
public interface Injector<T> extends org.picocontainer.Injector<T> {

	/**
	 * Checks whether this injector can produce an instance of a parametrised class.
	 *
	 * @param type
	 *            A raw class that should be produced.
	 * @param typeParameters
	 *            An array of type parameters.
	 * @return True, if {@link Injector#getParametrizedInstance} with the same parameters will return some instance.
	 */
	public boolean canProduceParametrizedInstance(Class<?> type, Type[] typeParameters);

	public <TT> TT getParametrizedInstance(final Class<TT> type, final Type[] typeParameters);
}
