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

package it.slumdroid.tool;

import static it.slumdroid.droidmodels.model.InteractionType.*;
import static it.slumdroid.droidmodels.model.SimpleType.*;

import java.util.ArrayList;
import java.util.Arrays;

import it.slumdroid.tool.components.comparator.*;
import it.slumdroid.tool.model.Comparator;
import it.slumdroid.tool.utilities.UserFactory;
import it.slumdroid.tool.utilities.adapters.SimpleInteractorAdapter;
import it.slumdroid.tool.utilities.interactors.editor.AdditionalEnterEditor;
import it.slumdroid.tool.utilities.interactors.editor.AdditionalWriteEditor;

public class Resources {

	// Support Variables
	public final static String COMPOSITIONAL_COMPARATOR = "CompositionalComparator";
	public final static String NULL_COMPARATOR = "NullComparator";
	public final static String TAG = "slumdroid";

	// Main Parameters
	public static String PACKAGE_NAME = "app.package";
	public static String CLASS_NAME = "app.package.class";
	public static long RANDOM_SEED = 93874383493L; 
	public static boolean ENABLE_MODEL = true;
	public static boolean SCREENSHOT_ENABLED = true; // Performs an image capture of the screen after processing a task
	public static boolean TAB_EVENTS_START_ONLY = false; // true -> click on tabs only on the start activity
	public static boolean HASH_VALUES = true;
	public static int MAX_NUM_EVENTS = 0; // After performing this amount of traces, the tool exits (0 = no length limit)
	public static int MAX_NUM_EVENTS_PER_SELECTOR = 3; // For ListView, Spinner and RadioGroup (0 = try all items in the list)
	public static void setMaxEventsSelector(int maxSelector) {
		MAX_NUM_EVENTS_PER_SELECTOR = maxSelector;		
	}
	public static int PAUSE_AFTER_TASKS = 1; // After performing this amount of traces, the tool pauses (0 = no pause)
	public static void setPauseTasks(int pauseAfterTraces) {
		PAUSE_AFTER_TASKS = pauseAfterTraces;		
	}
	public static String SCHEDULER_ALGORITHM = "BREADTH_FIRST";

	// Automation Parameters
	public static int SLEEP_AFTER_EVENT = 1000;
	public static int SLEEP_AFTER_RESTART = 0;
	public static int SLEEP_AFTER_TASK = 0;
	public static int SLEEP_ON_THROBBER = 1000; // How long to wait on spinning wheels (in ms -- 0 = don't wait)

	// Comparator Parameters
	public static Comparator COMPARATOR = new CompositionalComparator();
	public static String COMPARATOR_TYPE = new String(COMPOSITIONAL_COMPARATOR);
	public static String[] WIDGET_TYPES  = {
		// TEXT
		AUTOC_TEXT,
		CHECKTEXT,
		EDIT_TEXT, 
		NOEDITABLE_TEXT, 
		
		// BUTTON
		BUTTON, 
		CHECKBOX, 
		NUMBER_PICKER_BUTTON, 
		RADIO, 
		TOGGLE_BUTTON,

		// BASIC
		ACTION_HOME,
		DIALOG_TITLE,
		EXPAND_MENU,
		MENU_ITEM, 
		TOAST, 

		// PICKER
		DATE_PICKER, 
		NUMBER_PICKER,
		TIME_PICKER, 

		// LIST
		EMPTY_LIST, 
		LIST_VIEW, 
		PREFERENCE_LIST,

		// LAYOUT
		LINEAR_LAYOUT, 
		RELATIVE_LAYOUT,

		// VIEW
		IMAGE_VIEW, 
		MENU_VIEW, 
		TEXT_VIEW, 
		WEB_VIEW,

		// BAR
		PROGRESS_BAR, 
		RATING_BAR, 
		SEARCH_BAR, 
		SEEK_BAR,

		// SPINNER
		EMPTY_SPINNER, 
		SPINNER, 
		SPINNER_INPUT,

		// POPUP
		POPUP_MENU,
		POPUP_WINDOW,
		
		// OTHER
		RADIO_GROUP,
		SLIDING_DRAWER, 
		TAB_HOST
	};
	public static boolean COMPARE_LIST_COUNT = false;
	
	public static void getComparator() {
		if (COMPARATOR_TYPE.equals(NULL_COMPARATOR)) {
			COMPARATOR = new NullComparator();
		}    
	}

	// Interactions Parameters
	public static String EVENTS[];
	public static String EXTRA_EVENTS[];
	public static ArrayList<SimpleInteractorAdapter> ADDITIONAL_EVENTS = new ArrayList<SimpleInteractorAdapter>();

	public static void checkEvents(){
		if (EVENTS != null) {
			boolean isClick = false;
			for (String s: EVENTS) {
				String[] widgets = s.split("( )?,( )?");
				if (widgets[0].equals(CLICK)) isClick = true;
				UserFactory.addEvent(widgets[0], Arrays.copyOfRange(widgets, 1, widgets.length));
			}
			if (!isClick){
				UserFactory.addEvent(CLICK, BUTTON, MENU_ITEM, IMAGE_VIEW, LINEAR_LAYOUT);
			}
		} else {
			UserFactory.addEvent(CLICK, BUTTON, MENU_ITEM, IMAGE_VIEW, LINEAR_LAYOUT);
		}
		UserFactory.addEvent(LONG_CLICK, IMAGE_VIEW);
		UserFactory.addEvent(ENTER_TEXT, SEARCH_BAR);
		UserFactory.addEvent(LIST_SELECT, LIST_VIEW, PREFERENCE_LIST, EXPAND_MENU);
		UserFactory.addEvent(LIST_LONG_SELECT, LIST_VIEW);
		UserFactory.addEvent(RADIO_SELECT, RADIO_GROUP); 
		UserFactory.addEvent(SPINNER_SELECT, SPINNER);
		UserFactory.addEvent(SWAP_TAB, TAB_HOST);
		UserFactory.addEvent(DRAG, SLIDING_DRAWER);

		if (EXTRA_EVENTS != null) {
			ADDITIONAL_EVENTS.clear();
			for (String s: EXTRA_EVENTS) {
				String[] widgets = s.split(",");
				if (widgets[0].equals(WRITE_TEXT)){
					SimpleInteractorAdapter interactor = new AdditionalWriteEditor().addIdValuePair(widgets[1], Arrays.copyOfRange(widgets, 2, widgets.length));
					ADDITIONAL_EVENTS.add(interactor);
				} else {
					if (widgets[0].equals(ENTER_TEXT)){
						SimpleInteractorAdapter interactor = new AdditionalEnterEditor().addIdValuePair(widgets[1], Arrays.copyOfRange(widgets, 2, widgets.length));
						ADDITIONAL_EVENTS.add(interactor);
					}
				}	
			}
		}
	}

	public static String INPUTS[];
	public static String EXTRA_INPUTS[];
	public static ArrayList<SimpleInteractorAdapter> ADDITIONAL_INPUTS = new ArrayList<SimpleInteractorAdapter>();

	public static void checkInputs(){
		if (INPUTS != null) {
			boolean isClick = false;
			for (String s: INPUTS) {		
				String[] widgets = s.split("( )?,( )?");
				if (widgets[0].equals(CLICK)) isClick = true;
				UserFactory.addInput(widgets[0], Arrays.copyOfRange(widgets, 1, widgets.length));		
			}
			if (!isClick){
				UserFactory.addInput(CLICK, RADIO, CHECKBOX, CHECKTEXT, TOGGLE_BUTTON, NUMBER_PICKER_BUTTON);
			}
		} else {
			UserFactory.addInput(CLICK, RADIO, CHECKBOX, CHECKTEXT, TOGGLE_BUTTON, NUMBER_PICKER_BUTTON);
		}
		UserFactory.addInput(SPINNER_SELECT, SPINNER_INPUT);
		UserFactory.addInput(SET_BAR, SEEK_BAR, RATING_BAR);

		if (EXTRA_INPUTS != null) {
			ADDITIONAL_INPUTS.clear();
			for (String s: EXTRA_INPUTS) {
				String[] widgets = s.split(",");
				if (widgets[0].equals(WRITE_TEXT)){
					SimpleInteractorAdapter interactor = new AdditionalWriteEditor().addIdValuePair(widgets[1], Arrays.copyOfRange(widgets, 2, widgets.length));
					ADDITIONAL_INPUTS.add(interactor);
				}
			}
		}
	}

	// Persistence Parameters
	public static boolean ONLY_FINAL_TRANSITION = false;
	public static void setOnlyFinalTransition(boolean value){
		ONLY_FINAL_TRANSITION = value;
	}

	// Scheduler Parameters
	public static int MAX_TASKS_IN_SCHEDULER = 0;
	public static void setMaxTasksInScheduler(int value){
		MAX_TASKS_IN_SCHEDULER = value;
	}

	public static Class<?> theClass;

	static {
		Prefs.updateMainNode();
		Prefs.updateNode("automation", Resources.class);
		Prefs.updateNode("comparator", Resources.class);
		Prefs.updateNode("interactions", Resources.class);
		try {
			theClass = Class.forName(CLASS_NAME);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		getComparator();
		checkEvents();		
		checkInputs();			
	}
	
}