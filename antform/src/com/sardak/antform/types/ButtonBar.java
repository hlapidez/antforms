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
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.tools.ant.Task;

import com.sardak.antform.gui.ControlPanel;
import com.sardak.antform.interfaces.ActionComponent;
import com.sardak.antform.interfaces.Focusable;
import com.sardak.antform.util.ActionRegistry;

public class ButtonBar extends BaseType implements Focusable {
	private List buttons = new ArrayList();
	private Object align = BorderLayout.CENTER;
	private JPanel mainPanel;

	public void addConfiguredButton(Button button) {
		buttons.add(button);
	}

	public List getButtons() {
		return buttons;
	}
	
	public void addToControlPanel(ControlPanel panel) {
		buildMainPanel();
		applyStylesheet(panel);
		panel.addCentered(mainPanel);
	}
	
	public boolean validate(Task task) {
		boolean valid = true, hasDefault = false;
		if (buttons.isEmpty()) {
			task.log("ButtonBar : no button configured.");
			valid = false;
		}
		for (Iterator iter = buttons.iterator(); iter.hasNext() && valid;) {
			Button button = (Button) iter.next();
			if (!button.validate(task)) {
				valid = false;
			}
			if (button.isDefaultButton()) {
				if (hasDefault) {
					task.log("There is more than one default button on the bar.");
					valid = false;
				}
				hasDefault = true;
			}
		}
		return valid;
	}
	
	// deprecated methods
	public void addLink(Button button) {
		log("<link> nested elements are deprecated. Use <button> instead.");
		buttons.add(button);
	}

	public void register(ActionRegistry actionRegistry) {
		for (int i = 0 ;  i < buttons.size() ; i++) {
			actionRegistry.register((ActionComponent) buttons.get(i));
		}
	}
	
	public void setAlign(Object align) {
		this.align = align;
	}
	
	public JPanel getPanel() {
		buildMainPanel();
		return mainPanel;
	}
	
	public boolean isEmpty() {
		return getButtons().size() == 0;
	}
	
	private void buildMainPanel() {
		if (mainPanel == null) {
			mainPanel = new JPanel();
			BorderLayout mainLayout = new BorderLayout(0, 0);
			mainPanel.setLayout(mainLayout);
			JPanel innerPanel = new JPanel(new GridLayout(1, 0, 3, 0));
			for (Iterator iter = buttons.iterator(); iter.hasNext();) {
				final Button button = (Button) iter.next();
				JButton buttonComponent = (JButton) button.getComponent();
				innerPanel.add(buttonComponent);
			}
			mainPanel.add(innerPanel, align);
		}
	}
	
	public void applyStylesheet(ControlPanel panel) {
		for (Iterator iter = buttons.iterator(); iter.hasNext();) {
			final Button button = (Button) iter.next();
			JButton buttonComponent = (JButton) button.getComponent();
			panel.getStylesheetHandler().addLink(buttonComponent);
		}
	}
	
	public void setMargins(int top, int left, int bottom, int right) {
		getPanel().setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));		
	}

	public void setFocus(boolean focus) {
	}

	public boolean isFocus() {
		boolean focus = false;
		for (Iterator iter = buttons.iterator(); iter.hasNext();) {
			Button button = (Button) iter.next();
			if (button.isFocus()) {
				focus = true;
				break;
			}
		}
		return focus;
	}

	public JComponent getFocusableComponent() {
		JComponent c = null;
		for (Iterator iter = buttons.iterator(); iter.hasNext();) {
			Button button = (Button) iter.next();
			if (button.isFocus()) {
				c = button.getFocusableComponent();
				break;
			}
		}
		return c;
	}
}
