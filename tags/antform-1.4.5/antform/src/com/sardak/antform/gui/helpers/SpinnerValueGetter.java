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

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import com.sardak.antform.interfaces.ValueHandle;

/**
 * @author René Ghosh
 * 20 mars 2005
 */
public class SpinnerValueGetter implements ValueHandle{
	private JSpinner spinner;
	
	/**
	 * Constructor
	 */
	public SpinnerValueGetter(JSpinner spinner){
		this.spinner=spinner;
	}
	
	/**
	 * get the value from the component
	 */
	public String getValue() {
		return spinner.getValue().toString();
	}
	
	/**
	 * set selected value
	 */
	public void setValue(String s) {		
		SpinnerModel model = spinner.getModel();
		if (model instanceof SpinnerNumberModel) {
			spinner.setValue(new Double(s));
		} else {
			spinner.setValue(s);	
		}		
	}
	
	/**
	 * Return the input component
	 */
	public Component getComponent() {
		return spinner;
	}

}
