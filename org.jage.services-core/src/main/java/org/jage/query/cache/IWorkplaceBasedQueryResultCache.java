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
 * File: IWorkplaceBasedQueryResultCache.java
 * Created: 2011-09-20
 * Author: faber
 * $Id: IWorkplaceBasedQueryResultCache.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.query.cache;

import org.jage.query.IQuery;
import org.jage.workplace.IWorkplace;

/**
 * A proxy-like extension of {@link IQuery} that allows to cache results of query execution. It uses a workplace as a
 * indicator of the passing time.
 * <p>
 * It is used as a normal query, but needs to be initialized with a workplace instance before executing.
 * 
 * @param <Q>
 *            A type of a queried object.
 * @param <R>
 *            A type of results.
 * @author AGH AgE Team
 */
public interface IWorkplaceBasedQueryResultCache<Q, R> extends IQuery<Q, R> {

	/**
	 * Initializes the cache with the workplace.
	 * <p>
	 * It can be called multiple times but it invalidates all previous results.
	 * 
	 * @param workplaceToUse
	 *            A workplace to use as a point of reference.
	 */
	void init(IWorkplace workplaceToUse);

	/**
	 * Ivalidates all results stored in this cache.
	 */
	void invalidateAllResults();

	/**
	 * Returns the underlying query.
	 * 
	 * @return A query that is proxied by this cache.
	 */
	IQuery<Q, R> getRealQuery();
}
