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

import javax.swing.JCheckBox;

import org.apache.tools.ant.Task;

import com.sardak.antform.gui.ControlPanel;
import com.sardak.antform.gui.helpers.CheckValueGetter;
import com.sardak.antform.interfaces.ValueHandle;

/**
 * Boolean, or true/false, property
 * @author René Ghosh
 */
public class BooleanProperty extends DefaultProperty {
	public ValueHandle addToControlPanel(ControlPanel panel) {
		JCheckBox checkBox = new JCheckBox();
		checkBox.setEnabled(isEditable());
		initComponent(checkBox, panel);
		panel.getStylesheetHandler().addCheckBox(checkBox);
		CheckValueGetter valueHandle = new CheckValueGetter(checkBox);
		panel.addControl(getProperty(), valueHandle);
		return valueHandle;
	}
	
	public boolean validate(Task task) {
		return super.validate(task, "BooleanProperty");
	}
}
