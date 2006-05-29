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
package com.sardak.antform;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.JComponent;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

import com.sardak.antform.gui.CallBack;
import com.sardak.antform.gui.ControlPanel;
import com.sardak.antform.interfaces.Focusable;
import com.sardak.antform.interfaces.PropertyComponent;
import com.sardak.antform.types.BaseType;
import com.sardak.antform.types.BooleanProperty;
import com.sardak.antform.types.Button;
import com.sardak.antform.types.ButtonBar;
import com.sardak.antform.types.Cancel;
import com.sardak.antform.types.CheckSelectionProperty;
import com.sardak.antform.types.DateProperty;
import com.sardak.antform.types.FileSelectionProperty;
import com.sardak.antform.types.ListProperty;
import com.sardak.antform.types.MultilineTextProperty;
import com.sardak.antform.types.NumberProperty;
import com.sardak.antform.types.RadioSelectionProperty;
import com.sardak.antform.types.SelectionProperty;
import com.sardak.antform.types.Separator;
import com.sardak.antform.types.Tab;
import com.sardak.antform.types.Table;
import com.sardak.antform.types.TextProperty;
import com.sardak.antform.util.ActionType;
import com.sardak.antform.util.FileProperties;
import com.sardak.antform.util.TargetInvoker;

/**
 * Ant task for empowering form-based user interaction
 * 
 * @author René Ghosh
 */
public class AntForm extends AbstractTaskWindow implements CallBack {
	private String okMessage = "OK";
	private boolean okMessageChanged = false;
	private String resetMessage = "Reset";
	private boolean resetMessageChanged = false;
	private String cancelMessage = null;
	private String save = null;
	private String nextTarget, previousTarget = null;
	private boolean built = false;
	private ButtonBar controlBar = null;
	private boolean controlBarIncompatibilityDetected = false;

	//
	// antform attributes
	//

	/**
	 * set the focus property
	 */
	public void setFocus(String focus) {
		log(
				"Attribute 'focus' of antform is not supported anymore. Use the widget 'focus' attribute instead.",
				Project.MSG_WARN);
//		let's not fail for that and avoid breaking existing build files
//		needFail = true;
	}

	/**
	 * get the next target
	 */
	public String getNextTarget() {
		return nextTarget;
	}

	/**
	 * set the next target
	 */
	public void setNextTarget(String nextTarget) {
		log("nexttarget attribute is deprecated. Use <controlbar> instead.",
				Project.MSG_WARN);
		if (controlBar != null) {
			controlBarIncompatibilityDetected();
		}
		this.nextTarget = nextTarget;
	}

	/**
	 * get the previous target
	 */
	public String getPreviousTarget() {
		return previousTarget;
	}

	/**
	 * set the previous target
	 */
	public void setPreviousTarget(String previousTarget) {
		log("previoustarget attribute is deprecated. Use <controlbar> instead.",
				Project.MSG_WARN);
		if (controlBar != null) {
			controlBarIncompatibilityDetected();
		}
		this.previousTarget = previousTarget;
	}

	/**
	 * @return okMessage.
	 */
	public String getOkMessage() {
		return okMessage;
	}

	/**
	 * @param okMessage.
	 */
	public void setOkMessage(String okMessage) {
		log("okmessage attribute is deprecated. Use <controlbar> instead.",
				Project.MSG_WARN);
		if (controlBar != null) {
			controlBarIncompatibilityDetected();
		}
		this.okMessage = okMessage;
		okMessageChanged = true;
	}

	/**
	 * @return resetMessage.
	 */
	public String getResetMessage() {
		return resetMessage;
	}

	/**
	 * @param resetMessage.
	 */
	public void setResetMessage(String resetMessage) {
		log("resetmessage attribute is deprecated. Use <controlbar> instead.",
				Project.MSG_WARN);
		if (controlBar != null) {
			controlBarIncompatibilityDetected();
		}
		this.resetMessage = resetMessage;
		resetMessageChanged = true;
	}

	/**
	 * @return cancelMessage.
	 */
	public String getCancelMessage() {
		return cancelMessage;
	}

	/**
	 * @param cancelMessage.
	 */
	public void setCancelMessage(String cancelMessage) {
		log("cancelmessage attribute is deprecated. Use <controlbar> instead.",
				Project.MSG_WARN);
		if (controlBar != null) {
			controlBarIncompatibilityDetected();
		}
		this.cancelMessage = cancelMessage;
	}

	/**
	 * @return save.
	 */
	public String getSave() {
		return save;
	}

	/**
	 * @param save.
	 */
	public void setSave(String save) {
		this.save = save;
	}

	//
	// antform widgets
	//

	/**
	 * add a configured text property
	 * 
	 * @param textProperty
	 */
	public void addConfiguredTextProperty(TextProperty textProperty) {
		widgets.add(textProperty);
	}

	/**
	 * Add a cancel button
	 */
	public void addConfiguredCancel(Cancel cancel) {
		log("<cancel> element is deprecated. Use <controlbar> instead.", Project.MSG_WARN);
		if (controlBar != null) {
			controlBarIncompatibilityDetected();
		}
		cancelMessage = cancel.getLabel();
	}

	/**
	 * add a configured separator
	 * 
	 * @param textProperty
	 */
	public void addConfiguredSeparator(Separator separator) {
		widgets.add(separator);
	}

	/**
	 * add a configured date property
	 * 
	 * @param dateProperty
	 */
	public void addConfiguredDateProperty(DateProperty dateProperty) {
		widgets.add(dateProperty);
	}

	/**
	 * add a configured number property
	 */
	public void addConfiguredNumberProperty(NumberProperty numberProperty) {
		widgets.add(numberProperty);
	}

	/**
	 * add a configured list property
	 */
	public void addConfiguredListProperty(ListProperty listProperty) {
		widgets.add(listProperty);
	}

	/**
	 * add a configured multiline text property
	 * 
	 * @param multilineTextProperty
	 */
	public void addConfiguredMultilineTextProperty(
			MultilineTextProperty multilineTextProperty) {
		widgets.add(multilineTextProperty);
	}

	/**
	 * add a configured selection property
	 * 
	 * @param textProperty
	 */
	public void addConfiguredSelectionProperty(SelectionProperty selectionProperty) {
		widgets.add(selectionProperty);
	}

	/**
	 * add a configured tab
	 * 
	 * @param textProperty
	 */
	public void addConfiguredTab(Tab tab) {
		widgets.add(tab);
		tabbed = true;
	}

	/**
	 * add a configured radio selection property
	 * 
	 * @param textProperty
	 */
	public void addConfiguredRadioSelectionProperty(
			RadioSelectionProperty radioSelectionProperty) {
		widgets.add(radioSelectionProperty);
	}

	/**
	 * add a configured check selection property
	 * 
	 * @param textProperty
	 */
	public void addConfiguredCheckSelectionProperty(
			CheckSelectionProperty checkSelectionProperty) {
		widgets.add(checkSelectionProperty);
	}

	/**
	 * add a configured boolean property
	 * 
	 * @param textProperty
	 */
	public void addConfiguredBooleanProperty(BooleanProperty booleanProperty) {
		widgets.add(booleanProperty);
	}

	/**
	 * add a configured custom widget
	 * 
	 * @param textProperty
	 */
	public void addConfigured(BaseType widget) {
		widgets.add(widget);
	}

	/**
	 * add a configured link bar
	 */
	public void addConfiguredLinkBar(ButtonBar buttonBar) {
		log("<linkbar> nested elements are deprecated. Use <buttonbar> instead.");
		addConfiguredButtonBar(buttonBar);
	}

	/**
	 * add a configured table
	 */
	public void addConfiguredTable(Table table) {
		widgets.add(table);
	}

	/**
	 * add a configured file selection property
	 * 
	 * @param textProperty
	 */
	public void addConfiguredFileSelectionProperty(
			FileSelectionProperty fileSelectionProperty) {
		widgets.add(fileSelectionProperty);
	}

	public void addConfiguredControlBar(ButtonBar buttonBar) {
		if (okMessageChanged || resetMessageChanged || nextTarget != null
				|| previousTarget != null || cancelMessage != null) {
			controlBarIncompatibilityDetected();
		}
		controlBar = buttonBar;
		buttonBar.register(getActionRegistry());
	}

	//
	// execution
	//

	/**
	 * initialize the properties
	 */
	public void init() throws BuildException {
		widgets = new ArrayList();
	}

	/**
	 * Construct the gui
	 */
	public void build() {
		if (controlBar == null) {
			createControlButton(ActionType.CANCEL, cancelMessage, null);
			if (previousTarget == null) {
				createControlButton(ActionType.RESET, resetMessage, null);
			} else {
				createControlButton(ActionType.OK, resetMessage, previousTarget);
			}
			createControlButton(ActionType.OK, okMessage, nextTarget);
		}
		if (controlBar != null && !controlBar.isEmpty()) {
			// do not create if no buttons
			controlBar.setAlign(BorderLayout.EAST);
			controlBar.setMargins(3, 3, 3, 3);
			ControlPanel controlPanel = control.getPanel();
			controlBar.applyStylesheet(controlPanel);
			controlPanel.addButtonPanel(controlBar.getPanel());
			controlBar.register(getActionRegistry());
		}
		super.build();
		built = true;
	}

	/**
	 * @see org.apache.tools.ant.Task#execute()
	 */
	public void execute() throws BuildException {
		preliminaries();
		if (!built) {
			build();
		}
		super.execute();
		// widgets are there. Initialize them.
		reset();

		control.show();
		if (getActionType() == ActionType.OK && save != null) {
			save();
		}
		if (dynamic) {
			built = false;
			control = null;
		}
		if (getTargetToInvoke() != null && findTargetByName(getTargetToInvoke()) != null) {
			TargetInvoker invoker = new TargetInvoker(this, getTargetToInvoke());
			invoker.perform();
		}
	}

	private void controlBarIncompatibilityDetected() {
		if (!controlBarIncompatibilityDetected) {
			log(
					"<controlbar> is incompatible with attributes okmessage, resetmessage, nexttarget, previoustarget and with <cancel> element. Use <controlbar> only.",
					Project.MSG_ERR);
			needFail = true;
			controlBarIncompatibilityDetected = true;
		}
	}

	private void createControlButton(int type, String label, String target) {
		if (label != null && !label.equals("")) {
			Button button = new Button();
			ActionType actionType = new ActionType();
			actionType.setValue(ActionType.getType(type));
			button.setType(actionType);
			button.setLabel(label);
			button.setTarget(target);
			if (controlBar == null) {
				controlBar = new ButtonBar();
			}
			controlBar.addConfiguredButton(button);
		}
	}

	private void save() {
		try {
			File file = new File(save);
			FileProperties props = new FileProperties(file);
			props.store(getFormProperties());
		} catch (FileNotFoundException e) {
			throw new BuildException(e);
		} catch (IOException e) {
			throw new BuildException(e);
		}
	}

	private Properties getFormProperties() {
		Properties formProperties = null;
		Iterator iter = displayedWidgets.iterator();
		while (iter.hasNext()) {
			Object o = iter.next();
			if (o instanceof PropertyComponent) {
				if (formProperties == null) {
					formProperties = new Properties();
				}
				String property = ((PropertyComponent) o).getProperty();
				formProperties.setProperty(property, getProject().getProperty(property));
			}
		}
		return formProperties;
	}
}