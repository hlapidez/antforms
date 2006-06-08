package com.sardak.antform.util;

import java.util.Hashtable;
import java.util.Iterator;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.SubBuildListener;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.CallTarget;

import com.sardak.antform.interfaces.ActionComponent;

/**
 * @author patmartin
 * 
 * This class allows to call an ant target, in the same thread or in a new one.
 * It is an ant task itself. 
 */
public class TargetInvoker extends CallTarget implements Runnable, SubBuildListener {

	private boolean background = false;
	private boolean newProject = true;
	private Thread createdThread;
	private ActionComponent actionSource = null;
	
	public TargetInvoker(Task parentTask, ActionComponent actionSource) {
		this.actionSource = actionSource;
		setProject(parentTask.getProject());
		setOwningTarget(parentTask.getOwningTarget());
		setTaskName(parentTask.getTaskName());
		setLocation(parentTask.getLocation());									
		setTarget(actionSource.getTarget());
		setBackground(actionSource.isBackground());
		setNewProject(actionSource.isNewProject());
	}

	public boolean isBackground() {
		return background;
	}
	
	public void setBackground(boolean background) {
		this.background = background;
	}
	
	public boolean isNewProject() {
		return newProject;
	}

	public void setNewProject(boolean newProject) {
		this.newProject = newProject;
	}

	public void run() {
		super.execute();
	}

	public void execute() throws BuildException {
		if (background) { // antcall in separate thread
			getProject().addBuildListener(this);
			createdThread = new Thread(this);
			createdThread.start();
		} else if (isNewProject()) { // regular antcall
			run();
		} else { // executing the designed target in the current project
			if (findTargetByName(actionSource.getTarget()) != null) {
				getProject().executeTarget(actionSource.getTarget());
			} else {
				// do not fail here: allow unexisting target.
				log("Target " + actionSource.getTarget() + " not found.");
			}
		}
	}
	
	public void buildStarted(BuildEvent evt) {
	}

	public void buildFinished(BuildEvent evt) {
		log("buildFinished event received by TargetInvoker", Project.MSG_DEBUG);
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
	/**
	 * Find a target amid the projet targets
	 */
	private Target findTargetByName(String target) {
		Target targetToFind = null;
		Hashtable targets = getProject().getTargets();
		for (Iterator i=targets.keySet().iterator();i.hasNext();) {
			String targetName = (String) i.next();
			Target aTarget = (Target) targets.get(targetName);
			if (aTarget.getName().equals(target)) {				
				targetToFind = aTarget;
			}
		}
		return targetToFind;
	}
}
