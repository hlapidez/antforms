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

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sardak.antform.gui.ControlPanel;
import com.sardak.antform.interfaces.ValueHandle;

/**
 * @author René Ghosh
 * 12 janv. 2005
 */
public class Link extends BaseType{
	private String label, target, tooltip;
	
	/**
	 * Constructor
	 */
	public Link(String label, String target){
		this.label=label;
		this.target=target;
	}
	
	/**
	 * Constructor
	 */
	public Link(){	
	}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
    public String getTooltip() {
        return tooltip;
    }
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
	
	public ValueHandle addToControlPanel(ControlPanel panel) {
		final JButton link = new JButton(label);
		final String linkLocation = target;
		JPanel linkPanel = new JPanel();
		linkPanel.setBorder(ControlPanel.linkBorder());
		linkPanel.setLayout(new BorderLayout());
		linkPanel.add(link, BorderLayout.CENTER);
		linkPanel.setOpaque(false);
		panel.addLink(link);		
		panel.addLinkToLayout(linkPanel);
		panel.listenToLink(link, linkLocation);
		panel.setMnemonics(link, label);
		applyTooltip(link, null, getTooltip());
		return null;
	}
}
