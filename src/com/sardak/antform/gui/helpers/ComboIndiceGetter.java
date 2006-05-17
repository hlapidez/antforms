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
package com.sardak.antform.gui.helpers;

import java.awt.Component;

import javax.swing.JComboBox;

import com.sardak.antform.interfaces.ValueIndiceHandle;

/**
 * @author René Ghosh
 * 20 mars 2005
 */
public class ComboIndiceGetter implements ValueIndiceHandle{
	private JComboBox comboBox;
	
	/**
	 * Constructor
	 */
	public ComboIndiceGetter(JComboBox comboBox){
		this.comboBox=comboBox;
	}
	
	/**
	 * get the selected value
	 */
	public String getValue() {
		return comboBox.getSelectedItem().toString();
	}
	
	/**
	 * get the selected index
	 */
	public int getIndice() {
		return comboBox.getSelectedIndex();
	}
	
	/**
	 * set selected value
	 */
	public void setValue(String s) {
		comboBox.setSelectedItem(s);
	}
	
	/**
	 * Return the input component
	 */
	public Component getComponent() {
		return comboBox;
	}

}
