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

import java.util.List;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;

import org.apache.tools.ant.Task;

import com.sardak.antform.gui.ControlPanel;
import com.sardak.antform.interfaces.ActionListenerComponent;
import com.sardak.antform.util.CSVReader;

/**
 * @author René Ghosh 2 mars 2005
 */
public class ListProperty extends DefaultProperty implements ActionListenerComponent {
	private String values;
	private String separator = ",";
	private String escapeSequence = "\\";
	private SpinnerListModel model;
	private JSpinner spinner;

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public String getValues() {
		return values;
	}

	public void setValues(String values) {
		this.values = values;
	}

	public String getEscapeSequence() {
		return escapeSequence;
	}

	public void setEscapeSequence(String escapeSequence) {
		this.escapeSequence = escapeSequence;
	}

	/**
	 * get the properry s a list of values
	 */
	public List asList() {
		return new CSVReader(separator, escapeSequence).digest(values, true);
	}

	public void addToControlPanel(ControlPanel panel) {
		model = new SpinnerListModel(asList());
		spinner = new JSpinner(model);
		panel.getStylesheetHandler().addSpinner(spinner);
		spinner.setEnabled(isEditable());
		initComponent(spinner, panel);
	}

	public boolean validate(Task task) {
		boolean isValid = super.validate(task, "ListProperty");
		if (getValues() == null) {
			task.log("ListProperty : attribute \"values\" missing.");
			isValid = false;
		} else if (getValues().length() == 0) {
			task.log("ListProperty : attribute \"values\" is empty.");
			isValid = false;
		}
		return isValid;
	}

	public void ok() {
		getProject().setProperty(getProperty(), spinner.getValue().toString());
	}

	public void reset() {
		if (model.getList().contains(getInitialPropertyValue())) {
			spinner.setValue(getInitialPropertyValue());
		} else {
			spinner.setValue(model.getList().get(0));
		}
	}

	public JComponent getFocusableComponent() {
		return spinner;
	}
}
