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
package com.sardak.antform.gui.helpers;

import java.awt.Component;

import com.sardak.antform.gui.RadioGroupBox;
import com.sardak.antform.interfaces.ValueIndiceHandle;

/**
 * @author Ren� Ghosh
 * 20 mars 2005
 */
public class RadioGetter implements ValueIndiceHandle{
	private RadioGroupBox radioGBox;
	
	/**
	 * Constructor
	 */
	public RadioGetter(RadioGroupBox radioGBox){
		this.radioGBox=radioGBox;
	}
	
	/**
	 * get the index of the selected item
	 */
	public int getIndice() {
		return radioGBox.getSelectedIndex();
	}
	
	/**
	 * get the value of the selected item
	 */
	public String getValue() {
		return radioGBox.getSelectedValue();
	}
	
	/**
	 * set selected value
	 */
	public void setValue(String s) {
		radioGBox.setSelectedValue(s);
	}
	
	/**
	 * Return the input component
	 */
	public Component getComponent() {
		return radioGBox;
	}

}