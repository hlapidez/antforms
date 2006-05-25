package com.sardak.antform.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

import com.sardak.antform.AbstractTaskWindow;
import com.sardak.antform.gui.ControlPanel;
import com.sardak.antform.interfaces.ActionComponent;

public class ActionRegistry implements ActionListener {
	private Map actionComponents;
	private AbstractTaskWindow task;
	private Project project;
	private ControlPanel panel;

	public ActionRegistry(AbstractTaskWindow task) {
		actionComponents = new HashMap();
		this.task = task;
		this.project = task.getProject();
	}

	public void register(ActionComponent component) {
		actionComponents.put(component.getComponent(), component);
		component.getComponent().addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		ActionComponent source = (ActionComponent) actionComponents.get(e.getSource());
		if (source == null) {
			throw new BuildException("Received event from unknown source.");
		}
		panel = task.getControl().getPanel();
		if (source.getActionType() == ActionType.OK) {
			// save properties
			setProjectProperties();
			// close form (unless background)
			// run target (if set)
			runTarget(source);
		} else if (source.getActionType() == ActionType.CANCEL) {
			// close form, unless background (do not save properties)
			// run target (if set)
			runTarget(source);
		} else if (source.getActionType() == ActionType.RESET) {
			// reset form properties
			panel.initWidgets(panel.getDefaultProperties());
		}
	}

	private void setProjectProperties() {
		Properties fromProperties = panel.getCurrentFormProperties();
		for (Iterator i = fromProperties.keySet().iterator(); i.hasNext();) {
			String property = (String) i.next();
			task.log("Setting project property " + property + " = "
					+ fromProperties.getProperty(property));
			project.setProperty(property, fromProperties.getProperty(property));
		}
	}

	private void runTarget(ActionComponent source) {
		if (source.isBackground() && source.getTarget() != null) { // even if CANCEL
			// invoke target
			if (task.findTargetByName(source.getTarget()) != null) {
				TargetInvoker invoker = new TargetInvoker(task, source.getTarget(), true);
				invoker.perform();
			}
		} else {
			// tell form/menu what kind of action occured
			task.setActionType(source.getActionType());
			// tell form/menu which target to run
			task.setTargetToInvoke(source.getTarget());
			// close the form/menu
			task.getControl().close(null);
		}
	}
}
