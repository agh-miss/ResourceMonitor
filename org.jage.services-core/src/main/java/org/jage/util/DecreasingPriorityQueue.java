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
 * File: DecreasingPriorityQueue.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: DecreasingPriorityQueue.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.util;

import java.util.NoSuchElementException;

/**
 * The queue to store objects with their priority. Only one object is available at the time (the one with the highest
 * priority). The highest priority has a value of 1; the lowest priority has a value of Integer.MAX_VALUE - 1. All
 * priorities can be decreased using decreasePriorities() method (in fact all values will be increased). If a priority
 * of an element is lower than priorityLimit it will be removed from the queue.
 * 
 * @author AGH AgE Team
 */
public class DecreasingPriorityQueue {

	/**
	 * The first entry of the queue.
	 */
	private Entry head;

	/**
	 * The last entry of the queue.
	 */
	private Entry tail;

	/**
	 * Number of entries in the queue.
	 */
	private int size;

	/**
	 * Value to decrease priority of the entries.
	 */
	private int priorityDecrease = 1;

	/**
	 * The minimum value of priority for the entries. Any values with a lower priority will not be added or will be
	 * removed if they are already in the queue.
	 */
	private int priorityLimit = Integer.MAX_VALUE - 1;

	/**
	 * Constructor.
	 */
	public DecreasingPriorityQueue() {
		head = null;
		tail = null;
		size = 0;
	}

	/**
	 * Constructor.
	 * 
	 * @param priorityDecrease
	 *            value to decrease priority of the entries
	 * @param priorityLimit
	 *            the minimum value of priority for the entries
	 */
	public DecreasingPriorityQueue(int priorityDecrease, int priorityLimit) {
		this();
		this.priorityDecrease = priorityDecrease;
		this.priorityLimit = Math.min(Math.max(priorityLimit, 1), Integer.MAX_VALUE - 1);
	}

	/**
	 * Adds a value to the queue with the specified priority. The value will not be added if the priority is lower than
	 * priorityLimit (the value of aPriority is higher than priorityLimit).
	 * 
	 * @param value
	 *            a value to add
	 * @param priority
	 *            a priority
	 */
	public void add(Object value, int priority) {
		if (priority <= priorityLimit) {
			Entry entry = new Entry(priority, value);

			if (head == null) { // empty queue
				entry.setNextEntry(null);
				head = tail = entry;
			} else if (tail.getPriority() <= priority) { // adding at the end
				entry.setNextEntry(null);
				tail.setNextEntry(entry);
				tail = entry;
			} else {
				Entry prevEntry = null;
				Entry curEntry = head;
				while (curEntry.getPriority() <= priority) {
					prevEntry = curEntry;
					curEntry = curEntry.getNextEntry();
				}

				if (prevEntry == null) { // first element
					entry.setNextEntry(head);
					head = entry;
				} else {
					prevEntry.setNextEntry(entry);
					entry.setNextEntry(curEntry);
				}
			}

			size++;
		}
	}

	/**
	 * Decreases values of priority for the entries by priorityDecrease. All entries with the priority lower than
	 * priorityLimit (the value of priority higher than priorityLimit) will be removed from the queue.
	 */
	public void decreasePriorities() {
		Entry prevEntry = null;
		Entry curEntry = head;
		int curSize = 0;
		while (curEntry != null) {
			curEntry.decreasePriority(priorityDecrease);
			if (curEntry.getPriority() > priorityLimit) {
				if (prevEntry == null) {
					head = tail = null;
				} else {
					prevEntry.setNextEntry(null);
					tail = prevEntry;
				}
				size = curSize;
				break;
			}
			curSize++;
			prevEntry = curEntry;
			curEntry = curEntry.getNextEntry();
		}
	}

	/**
	 * Gets value to decrease priority of the entries.
	 * 
	 * @return value to decrease priority of the entries
	 */
	public int getPriorityDecrease() {
		return priorityDecrease;
	}

	/**
	 * Gets the minimum value of priority for the entries.
	 * 
	 * @return the minimum value of priority for the entries
	 */
	public int getPriorityLimit() {
		return priorityLimit;
	}

	/**
	 * Returns true if the queue is empty or false - otherwise.
	 * 
	 * @return true if the queue is empty or false - otherwise
	 */
	public boolean isEmpty() {
		return head == null;
	}

	/**
	 * Removes and returns the first element of the queue (with the highest priority).
	 * 
	 * @return the first element of the queue
	 * @throws NoSuchElementException
	 *             occurs when the queue is empty
	 */
	public Object popFirst() throws NoSuchElementException {
		if (head == null) {
			throw new NoSuchElementException();
		}

		Object first = head.getValue();
		head = head.getNextEntry();
		size--;
		return first;
	}

	/**
	 * Sets value to decrease priority of the entries.
	 * 
	 * @param priorityDecrease
	 *            value to decrease priority of the entries
	 */
	public void setPriorityDecrease(int priorityDecrease) {
		this.priorityDecrease = Math.min(Math.max(priorityDecrease, 0), Integer.MAX_VALUE - 1);
	}

	/**
	 * Set the minimum value of priority for the entries. Any values with a lower priority will not be added or will be
	 * removed if they are already in the queue.
	 * 
	 * @param priorityLimit
	 *            the minimum value of priority for the entries
	 */
	public void setPriorityLimit(int priorityLimit) {
		this.priorityLimit = priorityLimit;
	}

	/**
	 * Returns size of the queue.
	 * 
	 * @return size of the queue
	 */
	public int size() {
		return size;
	}

	/**
	 * Represents a single entry in a priority queue. It contains a priority and a value of the entry.
	 * 
	 * @author KrzS
	 */
	protected class Entry {
		/**
		 * Reference to the next entry in the priority queue or null if it is the last entry.
		 */
		private Entry nextEntry;

		/**
		 * The priority of the entry.
		 */
		private int priority;

		/**
		 * The value of the entry.
		 */
		private Object value;

		/**
		 * Constructor.
		 * 
		 * @param priority
		 *            priority of the entry
		 * @param value
		 *            value of the entry
		 */
		public Entry(int priority, Object value) {
			this.priority = priority;
			this.value = value;
		}

		/**
		 * Decreases the priority by the given value.
		 * 
		 * @param decrease
		 *            value to decrease the priority
		 */
		public void decreasePriority(int decrease) {
			if (Integer.MAX_VALUE - decrease < this.priority) {
				this.priority = Integer.MAX_VALUE;
			} else {
				this.priority += decrease;
			}
		}

		/**
		 * Gets the next entry in the priority queue or null if it is the last entry.
		 * 
		 * @return Returns the next entry in the priority queue or null if it is the last entry
		 */
		public Entry getNextEntry() {
			return nextEntry;
		}

		/**
		 * Gets the priority of the entry.
		 * 
		 * @return Returns the priority.
		 */
		public int getPriority() {
			return priority;
		}

		/**
		 * Gets the value of the entry.
		 * 
		 * @return Returns the value.
		 */
		public Object getValue() {
			return value;
		}

		/**
		 * Sets the next entry in the priority queue or null if it is the last entry.
		 * 
		 * @param nextEntry
		 *            the next entry in the priority queue
		 */
		public void setNextEntry(Entry nextEntry) {
			this.nextEntry = nextEntry;
		}
	}
}
