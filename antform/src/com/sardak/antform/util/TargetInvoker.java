package com.sardak.antform.util;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.CallTarget;

/**
 * @author patmartin
 * 
 * This class allows to call an ant target, in the same thread or in a new one.
 * It is an ant task itself. 
 */
public class TargetInvoker extends CallTarget implements Runnable {

	private boolean background = false;
	
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
			Thread t = new Thread(this);
	        t.start();
		} else {
			run();
		}
	}
	
	public void configure(Task parentTask, String target) {
		setProject(parentTask.getProject());
		setOwningTarget(parentTask.getOwningTarget());
		setTaskName(parentTask.getTaskName());
		setLocation(parentTask.getLocation());									
		setTarget(target);
	}

	public void configure(Task parentTask, String target, boolean parallel) {
		configure(parentTask, target);
		this.background = parallel; 
	}
}
