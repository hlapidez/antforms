package com.sardak.antform;

import java.util.Hashtable;
import java.util.Iterator;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Target;

import com.sardak.antform.types.Link;

/**
 * @author René Ghosh
 * 12 janv. 2005
 */
public class AntMenu extends AbstractTaskWindow implements AntCallBack{
	private Target nextTarget = null;
	
	
	
	public void init() throws BuildException {
		super.init();			
	}
	/**
	 * execute method
	 */
	public void execute() throws BuildException {		
		super.execute();			
		waitForValidation();
		if (nextTarget!=null) {
			nextTarget.execute();
		}
	}
	/**
	 * add a configured link
	 */
	public void addConfiguredLink(Link link) {
		checkBaseAttributes(link);
		control.getPanel().addLink(link.getLabel(), link.getTarget());
	}
	
	public void callback(String message){
		Hashtable targets = getProject().getTargets();		
		for (Iterator i=targets.keySet().iterator();i.hasNext();) {
			String targetName = (String) i.next();
			Target target = (Target) targets.get(targetName);
			if (target.getName().equals(message)) {
				quit = true;
				nextTarget = target;
			}
		}
	}
	
	/**
	 * check that the base properties are correctly set.
	 */
	private void checkBaseAttributes(Link link) {
		if (link.getLabel()==null) {
			super.log("label attribute of the property "+link.getClass().getName()+" cannot be null.");
			needFail = true;
		}
		if (link.getTarget()==null) {
			super.log("property attribute of the property "+link.getClass().getName()+" cannot be null.");
			needFail = true;
		}
	}

}
