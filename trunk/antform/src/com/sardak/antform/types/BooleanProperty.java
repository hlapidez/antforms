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
import javax.swing.JComponent;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

import com.sardak.antform.gui.ControlPanel;
import com.sardak.antform.interfaces.ActionListenerComponent;

/**
 * Boolean, or true/false, property
 * 
 * @author René Ghosh
 */
public class BooleanProperty extends DefaultProperty implements ActionListenerComponent {
	private JCheckBox checkBox;

	public void addToControlPanel(ControlPanel panel) {
		checkBox = new JCheckBox();
		checkBox.setEnabled(isEditable());
		initComponent(checkBox, panel);
		panel.getStylesheetHandler().addCheckBox(checkBox);
	}

	public boolean validate(Task task) {
		return super.validate(task, "BooleanProperty");
	}

	public void ok() {
		if (getInitialPropertyValue() == null && !checkBox.isSelected()) {
			// do not set project property in this case
		} else if (checkBox.isSelected()) {
			getProject().setProperty(getProperty(), Boolean.TRUE.toString());
		} else { // !checkBox.isSelected() && getInitialPropertyValue() != null
			getProject().setProperty(getProperty(), Boolean.FALSE.toString());
		}
	}

	public void reset() {
		checkBox.setSelected(Project.toBoolean(getInitialPropertyValue()));
	}

	public JComponent getFocusableComponent() {
		return checkBox;
	}
}
