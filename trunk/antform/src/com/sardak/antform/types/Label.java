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

import javax.swing.JLabel;
import javax.swing.JTextArea;

import com.sardak.antform.gui.ControlPanel;
import com.sardak.antform.interfaces.ValueHandle;

/**
 * @author René Ghosh
 * 10 janv. 2005
 */
public class Label extends BaseType{
	private String text;
	private int columns=-1, rows=-1;

	public void addText(String text) {
		this.text = text;
	}
	/**
	 * get label text
	 */
	public String getText() {
		return text;
	}

	/**
	 * get te number of columns
	 */
	public int getColumns() {
		return columns;
	}
	
	/**
	 * set the number of colums
	 */
	public void setColumns(int columns) {
		this.columns = columns;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}

	public ValueHandle addToControlPanel(ControlPanel panel) {
		JTextArea textArea = new JTextArea(text);
		JLabel getFont = new JLabel("");		
		if ((columns>0)&&(rows>0)) {
			textArea = new JTextArea(rows, columns);
		} else {
			textArea = new JTextArea();
		}
		textArea.setFont(getFont.getFont());
		textArea.setText(text);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setOpaque(false);		
		textArea.setWrapStyleWord(true);
		if ((columns>0)&&(rows>0)) {
			panel.addCenteredNoFill(textArea);
		} else {
		    panel.addCentered(textArea);
		}		
		panel.addMessage(textArea);
		return null;
	}
}
