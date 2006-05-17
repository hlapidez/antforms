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

import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.apache.tools.ant.Task;

import com.sardak.antform.gui.ControlPanel;
import com.sardak.antform.interfaces.ValueHandle;

/**
 * @author René Ghosh
 * 4 avr. 2005
 */
public class Tab extends BaseType{
	private String label;

	/**
	 * get the tab label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * set the tab label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	public ValueHandle addToControlPanel(ControlPanel panel) {
		GridBagLayout aLayout = new GridBagLayout();
		JPanel tabPanel  = new JPanel();	
		tabPanel.setBorder(BorderFactory.createEmptyBorder(15,5,15,5));
		tabPanel.setLayout(aLayout);
		panel.addToTabbedPane(label, tabPanel, aLayout);
		return null;
	}
	
	public boolean validate(Task task) {
		boolean attributesAreValid = true;
		if (getLabel() == null) {
			task.log("Tab : attribute \"label\" missing.");
			attributesAreValid = false;
		}		
		return attributesAreValid;
	}
}
