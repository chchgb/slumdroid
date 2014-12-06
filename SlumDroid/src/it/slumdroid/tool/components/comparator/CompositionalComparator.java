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

package it.slumdroid.tool.components.comparator;

import static it.slumdroid.droidmodels.model.SimpleType.ACTION_HOME;
import static it.slumdroid.droidmodels.model.SimpleType.AUTOC_TEXT;
import static it.slumdroid.droidmodels.model.SimpleType.BUTTON;
import static it.slumdroid.droidmodels.model.SimpleType.CHECKBOX;
import static it.slumdroid.droidmodels.model.SimpleType.CHECKTEXT;
import static it.slumdroid.droidmodels.model.SimpleType.DATE_PICKER;
import static it.slumdroid.droidmodels.model.SimpleType.DIALOG_TITLE;
import static it.slumdroid.droidmodels.model.SimpleType.EDIT_TEXT;
import static it.slumdroid.droidmodels.model.SimpleType.EMPTY_LIST;
import static it.slumdroid.droidmodels.model.SimpleType.EMPTY_SPINNER;
import static it.slumdroid.droidmodels.model.SimpleType.EXPAND_MENU;
import static it.slumdroid.droidmodels.model.SimpleType.IMAGE_VIEW;
import static it.slumdroid.droidmodels.model.SimpleType.LINEAR_LAYOUT;
import static it.slumdroid.droidmodels.model.SimpleType.LIST_VIEW;
import static it.slumdroid.droidmodels.model.SimpleType.MENU_ITEM;
import static it.slumdroid.droidmodels.model.SimpleType.MENU_VIEW;
import static it.slumdroid.droidmodels.model.SimpleType.NOEDITABLE_TEXT;
import static it.slumdroid.droidmodels.model.SimpleType.NUMBER_PICKER;
import static it.slumdroid.droidmodels.model.SimpleType.NUMBER_PICKER_BUTTON;
import static it.slumdroid.droidmodels.model.SimpleType.POPUP_MENU;
import static it.slumdroid.droidmodels.model.SimpleType.POPUP_WINDOW;
import static it.slumdroid.droidmodels.model.SimpleType.PREFERENCE_LIST;
import static it.slumdroid.droidmodels.model.SimpleType.PROGRESS_BAR;
import static it.slumdroid.droidmodels.model.SimpleType.RADIO;
import static it.slumdroid.droidmodels.model.SimpleType.RADIO_GROUP;
import static it.slumdroid.droidmodels.model.SimpleType.RATING_BAR;
import static it.slumdroid.droidmodels.model.SimpleType.RELATIVE_LAYOUT;
import static it.slumdroid.droidmodels.model.SimpleType.SEARCH_BAR;
import static it.slumdroid.droidmodels.model.SimpleType.SEEK_BAR;
import static it.slumdroid.droidmodels.model.SimpleType.SLIDING_DRAWER;
import static it.slumdroid.droidmodels.model.SimpleType.SPINNER;
import static it.slumdroid.droidmodels.model.SimpleType.SPINNER_INPUT;
import static it.slumdroid.droidmodels.model.SimpleType.TAB_HOST;
import static it.slumdroid.droidmodels.model.SimpleType.TEXT_VIEW;
import static it.slumdroid.droidmodels.model.SimpleType.TIME_PICKER;
import static it.slumdroid.droidmodels.model.SimpleType.TOAST;
import static it.slumdroid.droidmodels.model.SimpleType.TOGGLE_BUTTON;
import static it.slumdroid.droidmodels.model.SimpleType.WEB_VIEW;
import static it.slumdroid.tool.Resources.COMPARE_LIST_COUNT;
import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.model.WidgetState;
import it.slumdroid.tool.model.Comparator;

public class CompositionalComparator implements Comparator {

	private static String[] WIDGET_TYPES  = {
		AUTOC_TEXT, CHECKTEXT, EDIT_TEXT, NOEDITABLE_TEXT, 					// TEXT 
		BUTTON, CHECKBOX, NUMBER_PICKER_BUTTON,	RADIO, TOGGLE_BUTTON, 		// BUTTON
		ACTION_HOME, DIALOG_TITLE, EXPAND_MENU, MENU_ITEM, TOAST, 			// BASIC
		DATE_PICKER, NUMBER_PICKER, TIME_PICKER, 							// PICKER
		EMPTY_LIST, LIST_VIEW, PREFERENCE_LIST,								// LIST
		LINEAR_LAYOUT, RELATIVE_LAYOUT,										// LAYOUT
		IMAGE_VIEW, MENU_VIEW, TEXT_VIEW, WEB_VIEW,							// VIEW
		PROGRESS_BAR, RATING_BAR, SEARCH_BAR, SEEK_BAR,						// BAR
		EMPTY_SPINNER, SPINNER, SPINNER_INPUT,								// SPINNER
		POPUP_MENU, POPUP_WINDOW,											// POPUP
		RADIO_GROUP, SLIDING_DRAWER, TAB_HOST								// OTHER
	};
	
	@Override
	public boolean compare(ActivityState currentActivity, ActivityState storedActivity) {
		if (!compareNameTitle(currentActivity, storedActivity)) return false; 
		for (WidgetState field: currentActivity) {
			if (matchClass(field.getSimpleType())) {
				if (!lookFor(field, storedActivity)) return false;
			}
		}
		return true; 
	}
	
	private boolean compareNameTitle(ActivityState currentActivity, ActivityState storedActivity){
		if (!currentActivity.getTitle().equals(storedActivity.getTitle())) return false;
		if (!currentActivity.getName().equals(storedActivity.getName())) return false;
		return true;
	}
	
	private boolean matchClass (String type) {
		for (String storedType: WIDGET_TYPES) {
			if (storedType.equals(type)) return true;
		}
		return false;
	}

	private boolean lookFor (WidgetState field, ActivityState activity) {
		for (WidgetState otherField: activity) {
			if (matchWidget (otherField, field)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean matchWidget (WidgetState field, WidgetState otherField) {	
		boolean compareId = otherField.getId().equals(field.getId());
		boolean compareType = otherField.getSimpleType().equals(field.getSimpleType());		
		boolean compareWidget = compareId && compareType;
		if (compareWidget){
			if (field.getSimpleType().equals(TEXT_VIEW)) return field.getValue().isEmpty() == otherField.getValue().isEmpty();
			if (field.getSimpleType().equals(DIALOG_TITLE)) return otherField.getName().equals(field.getName());
			boolean isList = field.getSimpleType().equals(LIST_VIEW) || field.getSimpleType().equals(PREFERENCE_LIST);	
			boolean isMenu = field.getSimpleType().equals(MENU_VIEW) || field.getSimpleType().equals(EXPAND_MENU);
			if (isMenu || (isList && COMPARE_LIST_COUNT)) return otherField.getCount() == field.getCount();
		}
		return compareWidget;
	}

}