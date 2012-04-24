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
 * File: CachedQueryResult.java
 * Created: 2011-09-20
 * Author: faber
 * $Id: CachedQueryResult.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.query.cache;

/**
 * A container for query results. It encapsulates a query result and a marker of last refresh (e.g. step or time). It
 * can be used in different types of a query cache for storing a result and refresh data in the same object in a
 * transparent way.
 * 
 * @param <R>
 *            A type of results stored in this container.
 * @author AGH AgE Team
 */
public class CachedQueryResult<R> {

	/**
	 * Query result encapsulated by this cache
	 */
	private R result;

	/**
	 * Last refresh marker.
	 */
	private long lastRefresh = 0;

	/**
	 * If result should be refreshed next time.
	 */
	private boolean forcedRefresh;

	/**
	 * Default constructor.
	 * 
	 * @param result
	 *            Results to wrap.
	 */
	public CachedQueryResult(R result) {
		this.result = result;
		this.lastRefresh = 0;
		this.forcedRefresh = true;
	}

	/**
	 * Returns whether stored results must be refreshed.
	 * 
	 * @return True if a refresh is forced.
	 */
	public boolean isForcedRefresh() {
		return forcedRefresh;
	}

	/**
	 * Sets whether a refresh should be forced the next time.
	 * 
	 * @param refresh
	 *            True, if a refresh should be forced.
	 */
	public void setForcedRefresh(boolean refresh) {
		forcedRefresh = refresh;
	}

	/**
	 * Returns the cached result.
	 * 
	 * @return The chached result.
	 */
	public R getResult() {
		return result;
	}

	/**
	 * Sets a new result.
	 * 
	 * @param result
	 *            A new result.
	 */
	public void setResult(R result) {
		this.result = result;
	}

	/**
	 * Returns the time the stored result was refreshed.
	 * 
	 * @return The age of the result.
	 */
	public long getLastRefresh() {
		return lastRefresh;
	}

	/**
	 * Sets the time the stored result was refreshed.
	 * 
	 * @param lastRefresh
	 *            The time of the last refresh.
	 */
	public void setLastRefresh(long lastRefresh) {
		this.lastRefresh = lastRefresh;
	}

}
