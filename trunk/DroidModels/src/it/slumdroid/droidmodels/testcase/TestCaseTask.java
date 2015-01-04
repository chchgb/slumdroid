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
 * Copyright (C) 2013-2015 Gennaro Imparato
 */

package it.slumdroid.droidmodels.testcase;

import it.slumdroid.droidmodels.guitree.GuiTree;
import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.model.Task;
import it.slumdroid.droidmodels.model.Transition;
import it.slumdroid.droidmodels.xml.ElementWrapper;
import it.slumdroid.droidmodels.xml.NodeListWrapper;

import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TestCaseTask extends ElementWrapper implements Task {

	public final static String TAG = "TASK";

	public TestCaseTask () {
		super();
	}

	public TestCaseTask (Element task) {
		super(task);
	}

	public TestCaseTask (GuiTree session) {
		this (session.getDom());
	}

	public TestCaseTask (Document dom) {
		super (dom, TAG);
	}

	public String getId () {
		return getAttribute("id");
	}

	public void setId (String id) {
		setAttribute("id", id);
	}

	public boolean isFailed() {
		if (!hasAttribute("fail")) return false;
		return (getAttribute("fail").equals("true"));
	}

	public void setFailed(boolean failure) {
		setAttribute("fail", (failure)?"true":"false");
	}

	public void setFailed(String failure) {
		setAttribute("fail", failure);
	}

	protected String getFailed() {
		return getAttribute("fail");
	}

	public TestCaseTask getWrapper(Element element) {
		return new TestCaseTask (element);
	}

	// Iterator Methods
	public Iterator<Transition> transitions () {
		Element transition = getElement();
		if (transition.getNodeName().equals(TAG)) {
			return new NodeListWrapper<Transition> (transition, new TestCaseTransition());
		}
		return null;		
	}

	public Iterator<Transition> iterator() {
		return transitions();
	}

	public void addTransition(Transition theTransition) {
		appendChild(theTransition.getElement());
	}

	@Override
	public TestCaseTask clone () {
		TestCaseTask task = new TestCaseTask (getElement().getOwnerDocument());
		for (Transition child: this) {
			TestCaseTransition newChild = ((TestCaseTransition)child).clone();
			task.addTransition(newChild);
		}
		task.setFailed(getFailed());
		return task;
	}

	public void setFinalActivity(ActivityState theState) {
		Transition lastTransition = getFinalTransition();
		if (lastTransition != null) {
			lastTransition.setFinalActivity(theState);
		}
	}

	public Transition getFinalTransition() {
		Transition lastTransition = null;
		for (Transition transition: this) {
			lastTransition = transition;
		}
		return lastTransition;
	}

}
