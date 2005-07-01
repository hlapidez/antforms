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
import com.sardak.antform.gui.FileChooser;
import com.sardak.antform.gui.helpers.FileChooserGetter;
import com.sardak.antform.interfaces.Requirable;
import com.sardak.antform.interfaces.ValueHandle;

/**
 * File selection property
 * @author René Ghosh
 * 4 mars 2005
 */
public class FileSelectionProperty extends DefaultProperty implements Requirable{ 
	private int columns = 34;
	private boolean directoryChooser;
	private boolean required;
	
	

	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public int getColumns() {
		return columns;
	}
	public void setColumns(int columns) {
		this.columns = columns;
	}
	public boolean isDirectoryChooser() {
		return directoryChooser;
	}
	public void setDirectoryChooser(boolean directoryChooser) {
		this.directoryChooser = directoryChooser;
	}

	public ValueHandle addToControlPanel(ControlPanel panel) {
		FileChooser chooser = new FileChooser(columns, directoryChooser);
		panel.getStylesheetHandler().addFileChooser(chooser);
		initComponent(chooser, panel);
		FileChooserGetter valueHandle = new FileChooserGetter(chooser);
		panel.addControl(getProperty(), valueHandle, required);
		return valueHandle;
	}
}
