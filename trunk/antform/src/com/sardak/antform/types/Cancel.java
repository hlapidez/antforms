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

import javax.swing.Box;
import javax.swing.JButton;

import com.sardak.antform.gui.ControlPanel;
import com.sardak.antform.interfaces.ValueHandle;

/**
 * @author René Ghosh
 * 3 avr. 2005
 */
public class Cancel extends BaseType {
	private String label;
	//we want only one instance... let's keep a reference to it.
	private JButton cancelButton;
	/**
	 * get the cancel label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * set the cancel label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	public ValueHandle addToControlPanel(ControlPanel panel) {
		if (cancelButton==null) {
		    cancelButton = new JButton(label);
		    cancelButton.setActionCommand(label);
			panel.setCancelMessage(label);
			cancelButton.addActionListener(panel);
			panel.setMnemonics(cancelButton, cancelButton.getText());
			panel.addToButtonInnerPanel(Box.createHorizontalStrut(20));
			panel.addToButtonInnerPanel(cancelButton);
			panel.addButton(cancelButton);
		}
		return null;
	}
}
