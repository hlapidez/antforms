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

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.sardak.antform.gui.ControlPanel;
import com.sardak.antform.gui.helpers.TextValueGetter;
import com.sardak.antform.interfaces.Requirable;
import com.sardak.antform.interfaces.ValueHandle;


/**
 * Text property edited over multiple lines.
 * @author Ren� Ghosh
 */
public class MultilineTextProperty extends DefaultProperty implements Requirable{
	private int columns=40, rows=5;
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
	/**
	 * @return rows.
	 */
	public int getRows() {
		return rows;
	}
	/**
	 * @param rows.
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}

	public ValueHandle addToControlPanel(ControlPanel panel) {
		JTextArea textArea = new JTextArea(rows,columns);		
		textArea.setEditable(isEditable());		
		JScrollPane scrollPane = new JScrollPane(textArea);
		panel.addMultiLineTextArea(textArea); 
		panel.addScrollPane(scrollPane);
		initComponent(scrollPane, panel);
		TextValueGetter valueHandle = new TextValueGetter(textArea);
		panel.addControl(getProperty(), valueHandle, required);		
		return valueHandle;
	}
}
