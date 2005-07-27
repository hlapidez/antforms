package com.sardak.antform.types;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Sequential;

import com.sardak.antform.gui.ControlPanel;
import com.sardak.antform.interfaces.ValueHandle;

public class Wait extends BaseType implements BuildListener {
	private String label = "Please wait ...";
	private boolean showProgress = true;
	private boolean closeWhenDone = false;
	private Sequential sequential;
	private JProgressBar progressBar;
	private JLabel labelComponent;
	private Project project;
	private Thread createdThread;
	private ControlPanel controlPanel;
	
	public Wait(Project project) {
		this.project = project;
	}

	public void setShowProgress(boolean showProgress) {
		this.showProgress = showProgress;
	}

	public void setCloseWhenDone(boolean closeWhenDone) {
		this.closeWhenDone = closeWhenDone;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void addConfiguredSequential(Sequential s) {
		sequential = s;
	}

	public ValueHandle addToControlPanel(ControlPanel panel) {
		this.controlPanel = panel;
		controlPanel.addCentered(buildPanel());
		if (sequential != null) {
			createdThread = new Thread() {
				public void run() {
					sequential.perform();
				}
			};
			createdThread.start();
			project.addBuildListener(this);
		}
		return null;
	}

	private void executionDone() {
		progressBar.setIndeterminate(false);
		progressBar.setValue(100);
		labelComponent.setText("Done");
	}
	
	private JPanel buildPanel() {
		JPanel widgetPanel = new JPanel(new GridLayout(0, 1));
		if (showProgress) {
			progressBar = new JProgressBar(0, 100);
			progressBar.setValue(0);
			progressBar.setStringPainted(false);
			progressBar.setIndeterminate(true);
			widgetPanel.add(progressBar);
		}
		labelComponent = new JLabel(label);
		widgetPanel.add(labelComponent);
		return widgetPanel;
	}

	public void buildStarted(BuildEvent evt) {
	}

	public void buildFinished(BuildEvent evt) {
		if (createdThread != null && sequential.getProject().equals(evt.getProject())) {
			if (createdThread.isAlive()) {
				project.log("Waiting for background threads completion...", Project.MSG_VERBOSE);
				try {
					createdThread.join();
				} catch (InterruptedException ie) {
					project.log("Thread " + createdThread.getName() + " got interrupted: " + ie.getMessage(), Project.MSG_DEBUG);
				}
			}
		}
	}

	public void targetStarted(BuildEvent evt) {
	}

	public void targetFinished(BuildEvent evt) {
	}

	public void taskStarted(BuildEvent evt) {
	}

	public void taskFinished(BuildEvent evt) {
		if (evt.getTask().equals(sequential)) {
			executionDone();
			if (closeWhenDone) {
				ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ControlPanel.CANCEL_MSG);
				controlPanel.actionPerformed(event);
			}
		}
	}

	public void messageLogged(BuildEvent evt) {
	}
}
