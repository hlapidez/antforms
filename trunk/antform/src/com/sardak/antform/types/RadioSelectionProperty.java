/***************************************************************************\*
 *                                                                            *
 *    AntForm form-based interaction for Ant scripts                          *
 *    Copyright (C) 2005 Ren� Ghosh                                           *
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

import javax.swing.JComponent;

import org.apache.tools.ant.Task;

import com.sardak.antform.gui.ControlPanel;
import com.sardak.antform.gui.RadioGroupBox;
import com.sardak.antform.interfaces.ActionListenerComponent;

/**
 * Selection property using radio fields
 * 
 * @author Ren� Ghosh 13 mars 2005
 */
public class RadioSelectionProperty extends SelectionProperty implements
		ActionListenerComponent {
	private int columns = 1;
	private RadioGroupBox radioBox;

	public void addToControlPanel(ControlPanel panel) {
		radioBox = new RadioGroupBox(getSplitValues(), getColumns());
		radioBox.setEnabled(isEditable());
		panel.getStylesheetHandler().addRadioGroupBox(radioBox);
		initComponent(radioBox, panel);
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public boolean validate(Task task) {
		return super.validate(task, "RadioSelectionProperty");
	}
	
	public void ok() {
		getProject().setProperty(getProperty(), radioBox.getSelectedValue());
	}

	public void reset() {
		if (isValidValue(getCurrentProjectPropertyValue())) {
			radioBox.setSelectedValue(getCurrentProjectPropertyValue());
		} else {
			radioBox.setSelectedValue(getSplitValues()[0]);
		}
	}
	
	public JComponent getFocusableComponent() {
		return radioBox;
	}
}
