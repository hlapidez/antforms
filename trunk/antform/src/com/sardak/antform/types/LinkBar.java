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
package com.sardak.antform.types;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.tools.ant.Task;

import com.sardak.antform.gui.ControlPanel;
import com.sardak.antform.interfaces.ValueHandle;

/**
 * @author Ren� Ghosh
 * 13 mars 2005
 */
public class LinkBar extends BaseType {
	private List links = new ArrayList(); 
	/**
	 * add a link to the link bar
	 */
	public void addConfiguredLink(Link link) {
		links.add(link);
	}
	/**
	 * get links
	 */
	public List getLinks() {
		return links;
	}
	
	public ValueHandle addToControlPanel(ControlPanel panel) {
		JPanel linkPanel = new JPanel();
		linkPanel.setOpaque(false);
		for (Iterator iter = links.iterator(); iter.hasNext();) {
			final Link link = (Link) iter.next();
			JButton button = new JButton(link.getLabel());			
			panel.getStylesheetHandler().addLink(button);
			linkPanel.add(button);
			linkPanel.add(Box.createHorizontalStrut(10));
			// this might not be needed...
			button.addActionListener(panel);
//			panel.setTeleport(button, link.getTarget(), link.isBackground());
			panel.listenToLink(button, link.getTarget(), link.isBackground());
			panel.setMnemonics(button, link.getLabel());
		}
		panel.addCentered(linkPanel);
		return null;
	}
	public boolean validate(Task task) {
		boolean attributesAreValid = true;
		if (links.isEmpty()) {
			task.log("LinkBar : no links configured.");
			attributesAreValid = false;
		}
		for (Iterator iter = links.iterator(); iter.hasNext();) {
			Link link = (Link) iter.next();
			if (!link.validate(task)) {
				attributesAreValid = false;
			}
		}
		return attributesAreValid;
	}
}
