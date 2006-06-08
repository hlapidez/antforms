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
 * This class is used to instanciate NumberProperty. It can be parsed by a Java
 * 1.3.
 */
public class DummyNumberProperty extends DefaultProperty implements DummyComponent {
	private double min = 0, max = 100, step = 1;
	private DummyNumberProperty numberProperty;

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

	public void addToControlPanel(ControlPanel panel) {
	}

	public boolean validate(Task task) {
		return super.validate(task, "NumberProperty");
	}

	public JComponent getFocusableComponent() {
		return null;
	}

	public BaseType getRealType() {
		if (numberProperty == null) {
			numberProperty = (DummyNumberProperty) ClasspathUtils.newInstance(
					"com.sardak.antform.types.NumberProperty",
					DummyNumberProperty.class.getClassLoader());
			numberProperty.setEditable(isEditable());
			numberProperty.setFocus(isFocus());
			numberProperty.setIf(getIf());
			numberProperty.setLabel(getLabel());
			numberProperty.setMax(getMax());
			numberProperty.setMin(getMin());
			numberProperty.setProject(getProject());
			numberProperty.setProperty(getProperty());
			numberProperty.setStep(getStep());
			numberProperty.setTooltip(getTooltip());
			numberProperty.setUnless(getUnless());
		}
		return numberProperty;
	}
}
