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

import javax.swing.JComboBox;

import com.sardak.antform.gui.ControlPanel;
import com.sardak.antform.gui.helpers.ComboIndiceGetter;
import com.sardak.antform.interfaces.ValueHandle;
import com.sardak.antform.util.CSVReader;

/**
 * Property for selecting a value among a list of proposed values.
 * @author René Ghosh
 */
public class SelectionProperty extends DefaultProperty{
	private String values;
	private String separator = ",";
	private String escapeSequence="\\";
	private String[] splitValues;
	
	
	/**
	 * set the escape sequence
	 */
	public void setEscapeSequence(String escapeSequence) {
		this.escapeSequence = escapeSequence;
	}
	/**
	 * return the escape sequence
	 */
	public String getEscapeSequence() {
		return escapeSequence;
	}
	
	/**
	 * @return splitValues.
	 */
	public String[] getSplitValues() {
		return splitValues;
	}
	/**
	 * @param splitValues.
	 */
	public void setSplitValues(String[] splitValues) {
		this.splitValues = splitValues;
	}
	/**
	 * @return separator.
	 */
	public String getSeparator() {
		return separator;
	}
	/**
	 * @param separator.
	 */
	public void setSeparator(String separator) {
		this.separator = separator;
		split();
	}
	
	
	/**
	 * @return values.
	 */
	public String getValues() {
		return values;
	}
	/**
	 * split the values	 
	 */
	private void split(){
		CSVReader reader = new CSVReader(separator, escapeSequence);
		List valueList = reader.digest(values, true);
		splitValues = (String[]) valueList.toArray(new String[valueList.size()]);		
	}
	/**
	 * @param values
	 */
	public void setValues(String values) {
		this.values = values;
		split();
	}

	public ValueHandle addToControlPanel(ControlPanel panel) {
		JComboBox comboBox = new JComboBox(getSplitValues());		
		comboBox.setEnabled(isEditable());
		panel.addComboBox(comboBox);
		initComponent(comboBox, panel);
		ComboIndiceGetter valueHandle = new ComboIndiceGetter(comboBox);
		panel.addControl(getProperty(), valueHandle);
		return valueHandle;
	}
}
