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
import javax.swing.JLabel;

import com.sardak.antform.gui.ControlPanel;

/**
 * Default property attributes: include the label and target property.
 * @author René Ghosh
 */
public class DefaultProperty extends BaseType{
	private String label, property, tooltip;
	private boolean editable = true;
	/**
	 * true if property is editable
	 */
	public boolean isEditable() {
		return editable;
	}
	/**
	 * set property editable
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	/**
	 * @return label.
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label.
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return property.
	 */
	public String getProperty() {
		return property;
	}
	/**
	 * @param property.
	 */
	public void setProperty(String property) {
		this.property = property;
	}
	/**
	 * @return tooltip.
	 */
	public String getTooltip() {
		return tooltip;
	}
	/**
	 * @param property.
	 */
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	/**
	 * Apply tooltip text to the specified JComponent (and optionnaly its label)
	 */
	private void applyTooltip(JComponent component, JLabel label, String tooltipText) {
		if (null != tooltipText && !tooltipText.equals("")) {
			component.setToolTipText(tooltipText);
			if (label != null) {
			    label.setToolTipText(tooltipText);
			}
			for (int i = 0 ; i < component.getComponentCount() ; i++) {
				if (component.getComponent(i) instanceof JComponent) {
					applyTooltip((JComponent) component.getComponent(i), null, tooltipText);
				}
			}
		}
	}
	
	/**
	 * Add the specified component to the control panel, creating a JLabel and adding tooltip info
	 */
	protected void initComponent(JComponent component, ControlPanel panel) {
		JLabel labelComponent = panel.makeLabel(label);
		labelComponent.setLabelFor(component);
		applyTooltip(component, labelComponent, tooltip);
		panel.addRight(component);
	}
}
