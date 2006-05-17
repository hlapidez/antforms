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
package com.sardak.antform.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.sardak.antform.gui.helpers.ButtonValueGetter;

/**
 * @author Patrick Martin
 *
 * This class manages the bottom buttons of an antform
 */
public class ButtonPanel extends JPanel  {
	private ControlPanel controlPanel;
	private JPanel rightPanel;

	public ButtonPanel(String okMessage, String resetMessage, String cancelMessage, ControlPanel controlPanel) {
		this.controlPanel = controlPanel;
		this.setLayout(new BorderLayout(3, 3));
		this.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
		rightPanel = new JPanel(new GridLayout(1, 0, 3, 3));
		addButton(cancelMessage, rightPanel);
		addButton(resetMessage, rightPanel);
		addButton(okMessage, rightPanel);
		controlPanel.setCancelMessage(cancelMessage);
		controlPanel.setResetMessage(resetMessage);
		controlPanel.setOkMessage(okMessage);
		this.add(rightPanel, BorderLayout.EAST);
	}
	
	public void setBackground(Color color) {
		super.setBackground(color);
		if (rightPanel != null) { // rightPanel will be null when invoking super constructor
			rightPanel.setBackground(color);
		}
	}
	
	private void addButton(String label, JPanel innerPanel) {
		if (label != null && !label.equals("")) {
			JButton button = new JButton(label);
			button.setActionCommand(label);
			button.addActionListener(controlPanel);
			controlPanel.setMnemonics(button, label);
			controlPanel.addControl(label, new ButtonValueGetter(button), false);
			controlPanel.getStylesheetHandler().addButton(button);
			innerPanel.add(button);
		}
	}
}
