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
 * File: ActionComparator.java
 * Created: 2012-03-29
 * Author: faber
 * $Id: ActionComparator.java 177 2012-03-31 18:32:12Z faber $
 */

package org.jage.action.ordering;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jage.action.Action;
import org.jage.action.ActionException;
import org.jage.action.ComplexAction;
import org.jage.action.IActionContext;
import org.jage.action.SingleAction;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Table;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Predicates.in;
import static com.google.common.base.Predicates.or;
import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Sets.newHashSet;

/**
 * The comparator that is used by the {@link org.jage.agent.SimpleAggregate} to compare actions priorities.
 * <p>
 * Following aspects are considered during comparison:
 * <ul>
 * <li>a type of actions (single vs. complex),
 * <li>for single actions --- their context types,
 * <li>for complex classes --- the first action to execute (as defined by their iterator).
 * </ul>
 *
 * @author AGH AgE Team
 */
public class ActionComparator implements Comparator<Action> {

	private final Table<Class<? extends IActionContext>, Class<? extends IActionContext>, Relation> contextRelations
		= HashBasedTable.create();

	private final Set<Class<? extends IActionContext>> alwaysLast = newHashSet();

	private final Set<Class<? extends IActionContext>> alwaysFirst = newHashSet();

	@Override
	public int compare(final Action a1, final Action a2) {
		if (a1.equals(a2)) {
			return 0;
		}

		if (a1 instanceof SingleAction && a2 instanceof SingleAction) {
			return compareSingleActions((SingleAction)a1, (SingleAction)a2);
		} else if (a1 instanceof ComplexAction && a2 instanceof ComplexAction) {
			return compareComplexActions((ComplexAction)a1, (ComplexAction)a2);
		} else if (a1 instanceof SingleAction && a2 instanceof ComplexAction) {
			return compareSingleToComplex((SingleAction)a1, (ComplexAction)a2);
		} else if (a1 instanceof ComplexAction && a2 instanceof SingleAction) {
			return -compareSingleToComplex((SingleAction)a2, (ComplexAction)a1);
		}
		return 0;
	}

	/**
	 * Adds a binary relation between two context types. Reverse and transitive relations are added automatically.
	 * <p>
	 * The relation is constructed as: {@code <c1><relation><c2>}.
	 *
	 * @param c1
	 *            the left side of the relation.
	 * @param c2
	 *            the right side of the relation.
	 * @param relation
	 *            the relation.
	 */
	public void addContextRelation(final Class<? extends IActionContext> c1, final Class<? extends IActionContext> c2,
	        final Relation relation) {
		throwAlreadyConfiguredIfTrue(any(ImmutableSet.of(c1, c2), or(in(alwaysFirst), in(alwaysLast))));

		contextRelations.put(checkNotNull(c1), checkNotNull(c2), checkNotNull(relation));
		contextRelations.put(c2, c1, Relation.reverseTo(relation));

		// Ensure transitivity
		final Map<Class<? extends IActionContext>, Relation> rowRight = contextRelations.row(c2);
		for (final Entry<Class<? extends IActionContext>, Relation> entry : rowRight.entrySet()) {
			if (!entry.getKey().equals(c1) && entry.getValue().equals(relation)) {
				addContextRelation(c1, entry.getKey(), relation);
			}
		}

		final Map<Class<? extends IActionContext>, Relation> rowLeft = contextRelations.row(c1);
		for (final Entry<Class<? extends IActionContext>, Relation> entry : rowLeft.entrySet()) {
			if (!entry.getKey().equals(c2) && entry.getValue().isReverseOf(relation)) {
				addContextRelation(c2, entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * Adds context types with the relation <em>before</em> in the given order. For example:
	 * {@code addContextsAsOrdered(class1, class2, class3)} is equal to executing:
	 * <code>addContextRelation(class1, class2, IS_BEFORE);<br/>
	 * addContextRelation(class2, class3, IS_BEFORE);</code>
	 *
	 * @param contexts
	 *            context types to add.
	 */
	public void addContextsAsOrdered(final Class<? extends IActionContext>... contexts) {
		throwAlreadyConfiguredIfTrue(any(ImmutableSet.copyOf(contexts), or(in(alwaysFirst), in(alwaysLast))));

		for (int i = 0; i < contexts.length; i++) {
			final Class<? extends IActionContext> leftContext = contexts[i];
			for (int j = i + 1; j < contexts.length; j++) {
				addContextRelation(leftContext, contexts[j], Relation.IS_BEFORE);
			}
		}
	}

	/**
	 * Adds context types with the relation <em>before</em> in the order as given in the list.
	 *
	 * @param contexts
	 *            a list of context types to add.
	 * @see #addContextsAsOrdered(Class...)
	 */
	@SuppressWarnings("unchecked")
    public void addContextsAsOrdered(final List<Class<? extends IActionContext>> contexts) {
		addContextsAsOrdered(checkNotNull(contexts).toArray(new Class[0]));
	}

	/**
	 * Adds context that should be always ordered as last, i.e. for any other contexts the relation is
	 * {@code other_context IS_BEFORE context}.
	 * <p>
	 *
	 * Note: if more than one context types are added "as last" the relation between them is "independent".
	 *
	 * @param context
	 *            the context type.
	 */
	public void addAlwaysLastContext(final Class<? extends IActionContext> context) {
		checkNotNull(context);
		throwAlreadyConfiguredIfTrue(contextRelations.containsColumn(context) || alwaysFirst.contains(context));
		alwaysLast.add(context);
	}

	/**
	 * Adds context that should be always ordered as first, i.e. for any other contexts the relation is
	 * {@code context IS_BEFORE other_context}.
	 * <p>
	 *
	 * Note: if more than one context types are added "as first" the relation between them is "independent".
	 *
	 * @param context
	 *            the context type.
	 */
	public void addAlwaysFirstContext(final Class<? extends IActionContext> context) {
		checkNotNull(context);
		throwAlreadyConfiguredIfTrue(contextRelations.containsColumn(context) || alwaysLast.contains(context));
		alwaysFirst.add(context);
	}

	private int compareSingleActions(final SingleAction a1, final SingleAction a2) {
		final Class<? extends IActionContext> c1 = a1.getContext().getClass();
		final Class<? extends IActionContext> c2 = a2.getContext().getClass();

		if (c1.equals(c2)) {
			return 0;
		}

		final Set<Class<? extends IActionContext>> both = ImmutableSet.of(c1, c2);

		if (alwaysFirst.containsAll(both) || alwaysLast.containsAll(both)) {
			return 0;
		}
		if (alwaysFirst.contains(c1) || alwaysLast.contains(c2)) {
			return -1;
		}
		if (alwaysFirst.contains(c2) || alwaysLast.contains(c1)) {
			return 1;
		}

		final Relation relation = contextRelations.get(c1, c2);
		if (relation == null) {
			return 0;
		}
		switch (relation) {
			case IS_BEFORE:
				return -1;
			case IS_AFTER:
				return 1;
			case IS_INDEPENDENT:
			default:
				return 0;
		}
	}

	private int compareComplexActions(final ComplexAction a1, final ComplexAction a2) {
		final SingleAction sa1 = a1.iterator().next();
		final SingleAction sa2 = a2.iterator().next();

		return compareSingleActions(sa1, sa2);
	}

	private int compareSingleToComplex(final SingleAction a1, final ComplexAction a2) {
		final SingleAction sa2 = a2.iterator().next();
		return compareSingleActions(a1, sa2);
	}

	private static void throwAlreadyConfiguredIfTrue(final boolean condition) {
		if (condition) {
			throw new ActionException("Context already configured in the comparator.");
		}
	}

	static enum Relation {
		IS_BEFORE, IS_INDEPENDENT, IS_AFTER;

		public static Relation reverseTo(final Relation relation) {
			switch (checkNotNull(relation)) {
				case IS_BEFORE:
					return IS_AFTER;
				case IS_INDEPENDENT:
					return IS_INDEPENDENT;
				case IS_AFTER:
					return IS_BEFORE;
				default:
					// Empty
			}
			throw new IllegalArgumentException("Unknown relation.");
		}

		public boolean isReverseOf(final Relation relation) {
			return reverseTo(this).equals(relation);
		}
	}
}
