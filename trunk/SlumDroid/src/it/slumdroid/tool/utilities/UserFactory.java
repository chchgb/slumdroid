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

package it.slumdroid.tool.utilities;

import it.slumdroid.tool.model.Abstractor;
import it.slumdroid.tool.model.UserAdapter;
import it.slumdroid.tool.utilities.adapters.SimpleInteractorAdapter;
import it.slumdroid.tool.utilities.adapters.SimpleUserAdapter;
import it.slumdroid.tool.utilities.interactors.*;
import it.slumdroid.tool.utilities.interactors.editor.*;
import it.slumdroid.tool.utilities.interactors.selector.*;

import java.util.*;

import static it.slumdroid.droidmodels.model.InteractionType.*;
import static it.slumdroid.tool.Resources.*;

public class UserFactory {

	public static HashMap<String,String[]> eventToTypeMap = new HashMap<String,String[]>();
	public static HashMap<String,String[]> inputToTypeMap = new HashMap<String,String[]>();

	public static Map<String,List<String>> vetoesMap = new Hashtable<String,List<String>>();
	public static Map<String,List<String>> overridesMap = new Hashtable<String,List<String>>();

	public static void addEvent(String eventType, String ... widgetTypes) {
		eventToTypeMap.put(eventType, widgetTypes);
	}

	public static void addInput(String inputType, String ... widgetTypes) {
		inputToTypeMap.put(inputType, widgetTypes);
	}

	public static String[] typesForEvent(String interaction) {
		return eventToTypeMap.get(interaction);
	}

	public static String[] typesForInput(String interaction) {
		return inputToTypeMap.get(interaction);
	}

	public static boolean isRequiredEvent(String interaction) {
		return UserFactory.eventToTypeMap.containsKey(interaction);
	}

	public static boolean isRequiredInput(String interaction) {
		return UserFactory.inputToTypeMap.containsKey(interaction);
	}

	public static UserAdapter getUser (Abstractor abstractor) {

		UserAdapter userAdapter = new SimpleUserAdapter(abstractor,new Random(RANDOM_SEED));

		userAdapter.addEvent(new Clicker(typesForEvent(CLICK)));
		userAdapter.addEvent(new LongClicker(typesForEvent(LONG_CLICK)));

		if (isRequiredEvent(WRITE_TEXT)) {
			if(HASH_VALUES) userAdapter.addEvent(new HashWriteEditor(typesForEvent(WRITE_TEXT)));
			else userAdapter.addEvent(new RandomWriteEditor(typesForEvent(WRITE_TEXT)));
		}

		if(HASH_VALUES){
			userAdapter.addEvent(new HashEnterEditor(typesForEvent(ENTER_TEXT)));
		} else userAdapter.addEvent(new RandomEnterEditor(typesForEvent(ENTER_TEXT)));
		
		userAdapter.addEvent(new ListSelector(MAX_NUM_EVENTS_PER_SELECTOR, typesForEvent(LIST_SELECT)));
		userAdapter.addEvent(new ListLongSelector(MAX_NUM_EVENTS_PER_SELECTOR, typesForEvent(LIST_LONG_SELECT)));
		userAdapter.addEvent(new SpinnerSelector(typesForEvent(SPINNER_SELECT)));
		userAdapter.addEvent(new RadioSelector(typesForEvent(RADIO_SELECT)));
		userAdapter.addEvent(new TabSwapper(typesForEvent(SWAP_TAB)));
		userAdapter.addEvent(new Drager(typesForEvent(DRAG)));

		for (SimpleInteractorAdapter i: ADDITIONAL_EVENTS) {
			userAdapter.addEvent(i);			
		}

		userAdapter.addInput(new Clicker (typesForInput(CLICK)));	

		if (isRequiredInput(WRITE_TEXT)) {
			if (HASH_VALUES) userAdapter.addInput(new HashWriteEditor(typesForInput(WRITE_TEXT)));
			else userAdapter.addInput(new RandomWriteEditor(typesForInput(WRITE_TEXT)));
		}

		userAdapter.addInput(new RandomSpinnerSelector(typesForInput(SPINNER_SELECT)));
		userAdapter.addInput(new BarSlider(typesForInput(SET_BAR)));

		for (SimpleInteractorAdapter i: ADDITIONAL_INPUTS) {
			userAdapter.addInput(i);
		}

		return userAdapter;
	}	
}