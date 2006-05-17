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

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.apache.tools.ant.Task;

import com.sardak.antform.gui.ControlPanel;
import com.sardak.antform.gui.helpers.SpinnerValueGetter;
import com.sardak.antform.interfaces.ValueHandle;

/**
 * @author René Ghosh
 * 2 mars 2005
 */
public class NumberProperty extends DefaultProperty{
	private double min=0,max=100,step=1;
	
	/**
	 * get Max
	 */
	public double getMax() {
		return max;
	}
	/**
	 * get Min
	 */
	public void setMax(double max) {
		this.max = max;
	}
	/**
	 * 
	 * get step
	 */
	public double getMin() {
		return min;
	}
	public void setMin(double min) {
		this.min = min;
	}
	public double getStep() {
		return step;
	}
	public void setStep(double step) {
		this.step = step;
	}

	public ValueHandle addToControlPanel(ControlPanel panel) {
		SpinnerNumberModel model = new SpinnerNumberModel(min,min, max, step); 
		JSpinner spinner = new JSpinner(model);
		panel.getStylesheetHandler().addSpinner(spinner);
		spinner.setEnabled(isEditable());
		initComponent(spinner, panel);
		SpinnerValueGetter valueHandle = new SpinnerValueGetter(spinner);
		panel.addControl(""+getProperty(), valueHandle);
		return valueHandle;
	}
	
	public boolean validate(Task task) {
		return super.validate(task, "NumberProperty");
	}
}
