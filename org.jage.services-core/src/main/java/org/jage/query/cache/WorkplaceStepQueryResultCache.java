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
 * File: WorkplaceStepQueryResultCache.java
 * Created: 2011-09-20
 * Author: faber
 * $Id: WorkplaceStepQueryResultCache.java 76 2012-02-27 20:55:05Z faber $
 */

package org.jage.query.cache;

import java.util.Map;

import org.jage.platform.component.annotation.Inject;
import org.jage.property.InvalidPropertyPathException;
import org.jage.query.IQuery;
import org.jage.query.QueryException;
import org.jage.workplace.IWorkplace;

import com.google.common.collect.Maps;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The default implementation of {@link IWorkplaceBasedQueryResultCache}. It schedules refresh of results after
 * particular number of steps.
 * 
 * @param <Q>
 *            A type of a queried object.
 * @param <R>
 *            A type of results.
 * @author AGH AgE Team
 */
public class WorkplaceStepQueryResultCache<Q, R> implements IWorkplaceBasedQueryResultCache<Q, R> {

	/**
	 * The default age of result after which refresh is needed.
	 */
	private static final long DEFAULT_REFRESH_AGE = 1;

	/**
	 * The age of the result in this cache after which refresh is needed.
	 */
	@Inject
	private long refreshAge = DEFAULT_REFRESH_AGE;

	/**
	 * Results of the queries stored in this cache for particular aggregates.
	 * 
	 * <ul>
	 * <li>Key: a target instance.
	 * <li>Value: a cached result.
	 * </ul>
	 */
	private Map<Q, CachedQueryResult<R>> results = Maps.newHashMap();

	/**
	 * Query for which results are stored in this cache.
	 */
	@Inject
	private IQuery<Q, R> query;

	private IWorkplace workplace;

	/**
	 * Constructs a new cache that is backed by the provided query.
	 * 
	 * @param query
	 *            A query for which results are stored in this cache.
	 */
	public WorkplaceStepQueryResultCache(IQuery<Q, R> query) {
		this(query, DEFAULT_REFRESH_AGE);
	}

	/**
	 * Constructs a new cache that is backed by the provided query.
	 * 
	 * @param query
	 *            A query for which results are stored in this cache.
	 * @param refreshAge
	 *            The age of the result in this cache after which refresh is needed.
	 */
	public WorkplaceStepQueryResultCache(IQuery<Q, R> query, long refreshAge) {
		this.query = query;
		this.refreshAge = refreshAge;
	}

	@Override
	public void init(IWorkplace workplaceToUse) {
		this.workplace = checkNotNull(workplaceToUse, "Workplace cannot be null.");
		invalidateAllResults();
	}

	@Override
	public R execute(Q target) {
		long lastStep = 0;
		try {
			lastStep = (Long)workplace.getProperty("step").getValue();
		} catch (InvalidPropertyPathException e) {
			throw new QueryException(e);
		}

		CachedQueryResult<R> cachedResults = results.get(target);
		if (cachedResults == null) {
			cachedResults = new CachedQueryResult<R>(null);
			results.put(target, cachedResults);
		}

		if (cachedResults.isForcedRefresh() || cachedResults.getLastRefresh() + refreshAge <= lastStep) {
			cachedResults.setResult(query.execute(target));
			cachedResults.setLastRefresh(lastStep);
			cachedResults.setForcedRefresh(false);
		}

		return cachedResults.getResult();
	}

	@Override
	public void invalidateAllResults() {
		for (CachedQueryResult<R> result : results.values()) {
			result.setForcedRefresh(true);
		}
	}

	@Override
	public IQuery<Q, R> getRealQuery() {
		return query;
	}

	/**
	 * Returns the number of workplace steps that are required to invalidate the cache.
	 * 
	 * @return A number of steps.
	 */
	public long getRefreshAge() {
		return refreshAge;
	}
}
