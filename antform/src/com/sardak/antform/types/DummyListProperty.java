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

import javax.swing.JComponent;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.ClasspathUtils;

import com.sardak.antform.gui.ControlPanel;
import com.sardak.antform.interfaces.DummyComponent;

/**
 * This class is used to instanciate ListProperty. It can be parsed by a Java
 * 1.3.
 */
public class DummyListProperty extends DefaultProperty implements DummyComponent{
	private String values;
	private String separator = ",";
	private String escapeSequence = "\\";
	private DummyListProperty listProperty;

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

	public void addToControlPanel(ControlPanel panel) {
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

	public JComponent getFocusableComponent() {
		return null;
	}
	
	public BaseType getRealType() {
		if (listProperty == null) {
			listProperty = (DummyListProperty) ClasspathUtils.newInstance(
					"com.sardak.antform.types.ListProperty",
					DummyListProperty.class.getClassLoader());
			listProperty.setEditable(isEditable());
			listProperty.setEscapeSequence(getEscapeSequence());
			listProperty.setFocus(isFocus());
			listProperty.setIf(getIf());
			listProperty.setLabel(getLabel());
			listProperty.setProject(getProject());
			listProperty.setProperty(getProperty());
			listProperty.setTooltip(getTooltip());
			listProperty.setUnless(getUnless());
			listProperty.setSeparator(getSeparator());
			listProperty.setValues(getValues());
		}
		return listProperty;
	}
}
