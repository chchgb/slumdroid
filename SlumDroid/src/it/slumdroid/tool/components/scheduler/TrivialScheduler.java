/* This file is part of SlumDroid <https://code.google.com/p/slumdroid/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License <http://www.gnu.org/licenses/gpl-3.0.txt>
 * for more details.
 * 
 * Copyright (C) 2014 Gennaro Imparato
 */

package it.slumdroid.tool.components.scheduler;

import static it.slumdroid.tool.Resources.MAX_TASKS_IN_SCHEDULER;
import it.slumdroid.tool.components.scheduler.TraceDispatcher.SchedulerAlgorithm;
import it.slumdroid.tool.model.DispatchListener;
import it.slumdroid.tool.model.TaskScheduler;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import it.slumdroid.droidmodels.model.Trace;

class TrivialScheduler implements TaskScheduler {

	private final TraceDispatcher traceDispatcher;
	private List<Trace> tasks;
	private SchedulerAlgorithm algorithm;

	public TrivialScheduler (TraceDispatcher traceDispatcher, SchedulerAlgorithm algorithm) {
		this.traceDispatcher = traceDispatcher;
		setSchedulerAlgorithm (algorithm);
	}

	public void setSchedulerAlgorithm (SchedulerAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	public Trace nextTask() {
		if (!hasMore()) return null;

		switch (algorithm) {
		case DEPTH_FIRST: return lastTask();
		case BREADTH_FIRST: 
		default: return firstTask();
		}
	}

	public void addTasks(Collection<Trace> newTasks) {
		for (Trace t: newTasks) {
			tasks.add(t);
			for (DispatchListener theListener: this.traceDispatcher.theListeners) {
				theListener.onNewTaskAdded(t);
			}
		}				
	}

	public void addPlannedTasks(List<Trace> newTasks) {
		switch (algorithm) {
		case DEPTH_FIRST:
			Collections.reverse(newTasks);
			addTasks(newTasks);
			break;
		case BREADTH_FIRST: 
		default: addTasks(newTasks);
		}
	}

	public void setTaskList(List<Trace> theList) {
		this.tasks = theList;
	}

	public List<Trace> getTaskList() {
		return this.tasks;
	}

	public boolean hasMore() {
		return (!tasks.isEmpty());
	}

	public void remove(Trace t) {
		tasks.remove(t);
	}

	public void addTasks(Trace t) {
		discardTasks();
		this.tasks.add(t);
	}

	private void discardTasks() {
		if (MAX_TASKS_IN_SCHEDULER==0) return;
		while (this.tasks.size()>=MAX_TASKS_IN_SCHEDULER) {
			switch (algorithm) {
			case DEPTH_FIRST: 
				remove (firstTask());
				break;
			case BREADTH_FIRST: 
			default: 
				remove (lastTask());
				break;
			}
		}
	}

	public Trace firstTask() {
		return this.tasks.get(0);
	}

	public Trace lastTask() {
		return this.tasks.get(this.tasks.size()-1);
	}

}