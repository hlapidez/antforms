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

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;

import org.apache.tools.ant.Task;

import com.sardak.antform.gui.ControlPanel;
import com.sardak.antform.interfaces.ActionComponent;
import com.sardak.antform.interfaces.Focusable;
import com.sardak.antform.util.ActionType;

public class Button extends BaseType implements ActionComponent, Focusable {
	private String target;
	private boolean background = false;
	private JButton button = new JButton();
	private int type = ActionType.OK;
	private boolean defaultButton = false;
	private boolean focus = false;
	private boolean loopExit = false;
	private boolean newProject = true;

	public boolean isNewProject() {
		return newProject;
	}

	public void setNewProject(boolean newProject) {
		this.newProject = newProject;
	}

	
	public Button() {
		super();
	}
	
	public Button(String text, String target, int actionType) {
		super();
		setLabel(text);
		setTarget(target);
		type = actionType;
	}
	
	public String getLabel() {
		return button.getText();
	}
	public void setLabel(String label) {
		button.setText(label);
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
    public String getTooltip() {
        return button.getToolTipText();
    }
    public void setTooltip(String tooltip) {
    	if (tooltip != null) {
    		button.setToolTipText(tooltip);
    	}
    }
	public boolean isBackground() {
		return background;
	}
	public void setBackground(boolean background) {
		this.background = background;
	}
	public int getActionType() {
		return type;
	}
	public void setType(ActionType type) {
		this.type = type.getType();
	}
	
	public boolean isLoopExit() {
		return loopExit;
	}

	public void setLoopExit(boolean loopExit) {
		this.loopExit = loopExit;
	}

	public AbstractButton getComponent() {
		return button;
	}
	
	public boolean validate(Task task) {
		boolean valid = true;
		if (getLabel() == null) {
			task.log("Button has no label");
			valid = false;
		}
		return valid;
	}
	
	public void addToControlPanel(ControlPanel panel) {
		// Always use a ButtonBar to hold a button
	}
	
	public String toString() {
		return super.toString() + " [label:" + getLabel() +
				", type:" + ActionType.getType(getActionType()) +
				", target:" + getTarget() + ", background:" + isBackground() +
				", defaultButton:" + isDefaultButton() + "]";
	}

	public void setFocus(boolean focus) {
		this.focus = focus;
	}

	public boolean isFocus() {
		return focus;
	}

	public JComponent getFocusableComponent() {
		return button;
	}

	public boolean isDefaultButton() {
		return defaultButton;
	}

	public void setDefaultButton(boolean defaultButton) {
		this.defaultButton = defaultButton;
	}
}
