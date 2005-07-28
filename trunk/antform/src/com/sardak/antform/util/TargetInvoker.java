package com.sardak.antform.util;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.SubBuildListener;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.CallTarget;

/**
 * @author patmartin
 * 
 * This class allows to call an ant target, in the same thread or in a new one.
 * It is an ant task itself. 
 */
public class TargetInvoker extends CallTarget implements Runnable, SubBuildListener {

	private boolean background = false;
	private Thread createdThread;
	
	public TargetInvoker() {
	}

	public TargetInvoker(Task parentTask, String target) {
		configure(parentTask, target);
	}

	public TargetInvoker(Task parentTask, String target, boolean parallel) {
		configure(parentTask, target, parallel);
	}

	public boolean isBackground() {
		return background;
	}
	
	public void setBackground(boolean background) {
		this.background = background;
	}
	
	public void run() {
		super.execute();
	}

	public void execute() throws BuildException {
		if (background) {
			getProject().addBuildListener(this);
			createdThread = new Thread(this);
			createdThread.start();
		} else {
			run();
		}
	}
	
	public void buildStarted(BuildEvent evt) {
	}

	public void buildFinished(BuildEvent evt) {
		log("subBuildFinished event received by TargetInvoker", Project.MSG_DEBUG);
		waitForThread(evt);
	}

	public void targetStarted(BuildEvent evt) {
	}

	public void targetFinished(BuildEvent evt) {
	}

	public void taskStarted(BuildEvent evt) {
	}

	public void taskFinished(BuildEvent evt) {
	}

	public void messageLogged(BuildEvent evt) {
	}

	public void subBuildStarted(BuildEvent evt) {
	}

	public void subBuildFinished(BuildEvent evt) {
		log("subBuildFinished event received by TargetInvoker", Project.MSG_DEBUG);
		waitForThread(evt);
	}
	
	private void waitForThread(BuildEvent evt) {
		if (createdThread != null && getProject().equals(evt.getProject())) {
			if (createdThread.isAlive()) {
				log("Waiting for background threads completion...", Project.MSG_VERBOSE);
				try {
					createdThread.join();
				} catch (InterruptedException ie) {
					log("Thread " + createdThread.getName() + " got interrupted: " + ie.getMessage(), Project.MSG_DEBUG);
				}
			}
		}
	}

	private void configure(Task parentTask, String target) {
		setProject(parentTask.getProject());
		setOwningTarget(parentTask.getOwningTarget());
		setTaskName(parentTask.getTaskName());
		setLocation(parentTask.getLocation());									
		setTarget(target);
	}

	private void configure(Task parentTask, String target, boolean parallel) {
		configure(parentTask, target);
		this.background = parallel; 
	}
}
