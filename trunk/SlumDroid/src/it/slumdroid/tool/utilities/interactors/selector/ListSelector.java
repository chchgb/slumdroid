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

package it.slumdroid.tool.utilities.interactors.selector;

import static it.slumdroid.droidmodels.model.InteractionType.LIST_SELECT;
import static it.slumdroid.droidmodels.model.SimpleType.EXPAND_MENU;
import static it.slumdroid.droidmodels.model.SimpleType.PREFERENCE_LIST;
import it.slumdroid.droidmodels.model.WidgetState;
import it.slumdroid.tool.utilities.adapters.IterativeInteractorAdapter;

public class ListSelector extends IterativeInteractorAdapter {

	public ListSelector (int maxItems, String ... simpleTypes) {
		super (maxItems, simpleTypes);
	}

	@Override
	public int getToItem (WidgetState widget, int fromItem, int toItem) {
		if (widget.getSimpleType().equals(PREFERENCE_LIST) 
				|| widget.getSimpleType().equals(EXPAND_MENU)) {
			return toItem;
		}		
		return super.getToItem (widget,fromItem,toItem);
	}

	public boolean canUseWidget (WidgetState widget) {
		return widget.isClickable() && super.canUseWidget(widget);
	}

	public String getInteractionType () {
		return LIST_SELECT;
	}

}