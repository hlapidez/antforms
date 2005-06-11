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

import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.sardak.antform.gui.ControlPanel;
import com.sardak.antform.gui.helpers.TextValueGetter;
import com.sardak.antform.interfaces.Requirable;
import com.sardak.antform.interfaces.ValueHandle;


/**
 * Text property.
 * @author René Ghosh
 */
public class TextProperty extends DefaultProperty implements Requirable{ 
	private int columns=34; 
	private boolean password = false;
	private boolean required;
	
	
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	/**
	 * @return columns.
	 */
	public int getColumns() {
		return columns;
	}
	/**
	 * @param columns.
	 */
	public void setColumns(int columns) {
		this.columns = columns;
	}
	public boolean isPassword() {
		return password;
	}
	public void setPassword(boolean password) {
		this.password = password;
	}

	public ValueHandle addToControlPanel(ControlPanel panel) {
		JTextField textField = null;		
		if (!isPassword()) {
			textField = new JTextField(getColumns());
		} else {
			textField = new JPasswordField(getColumns());
		}
		panel.addTextField(textField);
		textField.setEditable(isEditable());
		initComponent(textField, panel);
		TextValueGetter valueGetter = new TextValueGetter(textField);
		panel.addControl(getProperty(), valueGetter, required);
		return valueGetter;
	}
}
