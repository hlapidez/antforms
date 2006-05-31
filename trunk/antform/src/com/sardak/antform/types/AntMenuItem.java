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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JMenuItem;

import org.apache.tools.ant.Task;

import com.sardak.antform.gui.ControlPanel;
import com.sardak.antform.interfaces.ActionComponent;
import com.sardak.antform.util.ActionRegistry;
import com.sardak.antform.util.ActionType;

/**
 * @author René Ghosh 11 mars 2005
 */
public class AntMenuItem extends BaseType implements ActionComponent {
	private List subMenuItems = new ArrayList();
	private HashSet usedLetters = new HashSet();
	private String target, name;
	private boolean background = false;
	private int type = ActionType.OK;
	private JMenuItem menuItem;
	private boolean loopExit = false;

	/**
	 * get the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * set the name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * get the target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * set the target
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	public void setType(ActionType type) {
		this.type = type.getType();
	}

	public int getActionType() {
		return type;
	}

	public void setBackground(boolean background) {
		this.background = background;
	}

	public boolean isBackground() {
		return background;
	}

	public boolean isLoopExit() {
		return loopExit;
	}

	public void setLoopExit(boolean loopExit) {
		this.loopExit = loopExit;
	}

	/**
	 * get the list of subProperties
	 */
	public List getSubMenuItems() {
		return subMenuItems;
	}

	/**
	 * Add another configured property to this one
	 */
	public void addConfiguredAntMenuItem(AntMenuItem menuProperty) {
		subMenuItems.add(menuProperty);
	}

	public HashSet getUsedLetters() {
		return usedLetters;
	}

	public void setUsedLetters(HashSet usedLetters) {
		this.usedLetters = usedLetters;
	}

	public void addToControlPanel(ControlPanel panel) {
		panel.getControl().addAntMenuItem(this, null);
	}

	public boolean validate(Task task) {
		boolean attributesAreValid = true;
		if (getName() == null) {
			task.log("AntMenuItem : attribute \"name\" missing.");
			attributesAreValid = false;
		}
		if (subMenuItems.size() > 0) {
			if (getTarget() != null) {
				task.log("AntMenuItem : cannot set \"target\" and sub-AntMenuItem at the same time.");
				attributesAreValid = false;
			}
			for (Iterator iter = subMenuItems.iterator(); iter.hasNext();) {
				AntMenuItem o = (AntMenuItem) iter.next();
				if (!o.validate(task)) {
					attributesAreValid = false;
				}
			}
		}
		return attributesAreValid;
	}

	public AbstractButton getComponent() {
		return menuItem;
	}
	
	public void setComponent(JMenuItem component) {
		menuItem = component;
	}

	public void register(ActionRegistry actionRegistry) {
		actionRegistry.register(this);
	}
}
