 /***************************************************************************\*
 *                                                                            *
 *    AntForm form-based interaction for Ant scripts                          *
 *    Copyright (C) 2005 René Ghosh                                           *
 *                                                                            *
 *   This library is free software; you can redistribute it and/or modify it  *
 *   under the terms of the GNU Lesser General Public License as published by *
 *   the Free Software Foundation; either version 2.1 of the License, or (at  *
 *   your option) any later version.                                          *
 *                                                                            *
 *   This library is distributed in the hope that it will be useful, but      *
 *   WITHOUT ANY WARRANTY; without even the implied warranty of               *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser  *
 *   General Public License for more details.                                 *
 *                                                                            *
 *   You should have received a copy of the GNU Lesser General Public License *
 *   along with this library; if not, write to the Free Software Foundation,  *
 *   Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA              *
 \****************************************************************************/
package com.sardak.antform.types;

import com.sardak.antform.gui.ControlPanel;
import com.sardak.antform.gui.RadioGroupBox;
import com.sardak.antform.gui.helpers.RadioGetter;
import com.sardak.antform.interfaces.ValueHandle;


/**
 * Selection property using radio fields
 * @author René Ghosh
 * 13 mars 2005
 */
public class RadioSelectionProperty extends SelectionProperty{
	int columns = 1;

    public ValueHandle addToControlPanel(ControlPanel panel) {
		RadioGroupBox radioBox = new RadioGroupBox(getSplitValues(), getColumns());		
		radioBox.setEnabled(isEditable());
		panel.getStylesheetHandler().addRadioGroupBox(radioBox);
		initComponent(radioBox, panel);
		RadioGetter valueHandle =  new RadioGetter(radioBox);
		panel.addControl(getProperty(), valueHandle);
		return valueHandle;
	}

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }
}
