package com.sardak.antform.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import org.apache.tools.ant.BuildException;

import com.sardak.antform.AbstractTaskWindow;
import com.sardak.antform.interfaces.ActionComponent;

public class ActionRegistry implements ActionListener {
	private Map actionComponents;
	private AbstractTaskWindow task;

	public ActionRegistry(AbstractTaskWindow task) {
		actionComponents = new HashMap();
		this.task = task;
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
		if (source.getActionType() == ActionType.OK) {
			// check required components
			if (!task.requiredStatusOk()) {
				return;
			}
			// notify components
			task.ok();
			// close form (unless background) and run target (if set and valid)
			runTarget(source);
		} else if (source.getActionType() == ActionType.CANCEL) {
			// notify components
			task.cancel();
			// close form (unless background) and run target (if set and valid)
			runTarget(source);
		} else if (source.getActionType() == ActionType.RESET) {
			// reset form properties
			task.reset();
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
			// tell form/menu if it should exit its internal loop
			task.setLoop(!source.isLoopExit());
			// close the form/menu
			task.getControl().close(null);
		}
	}
}
