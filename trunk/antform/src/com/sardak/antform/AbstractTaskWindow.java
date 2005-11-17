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

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;

import com.sardak.antform.gui.CallBack;
import com.sardak.antform.gui.Control;
import com.sardak.antform.types.AntMenuItem;
import com.sardak.antform.types.BaseType;
import com.sardak.antform.util.TargetInvoker;

/**
 * @author René Ghosh
 * 12 janv. 2005
 */
public abstract class AbstractTaskWindow extends Task implements CallBack{
	protected String title = null;
	private String stylesheet = null;
	protected String image = null;
	private int height=-1, width=-1; 
	protected Control control;
	protected boolean needFail = false;
	protected String lookAndFeel=null;
	protected List widgets;	
	protected boolean dynamic = false;
	protected boolean tabbed = false;
	
	/**
	 * get image URL
	 */
	public String getImage() {
		return image;
	}
	/**
	 * set image URL
	 */
	public void setImage(String image) {
		this.image = image;
	}
		
	/**
	 * set a property to false if it has been set
	 */
	public void setFalse(String propertyName){
		if (getProject().getProperties().containsKey(propertyName)) {
			getProject().setProperty(propertyName, Boolean.FALSE+"");
		}
	}
	
	/**
	 * construct the gui widgets
	 */
	protected void build(){
		for (Iterator iter = widgets.iterator(); iter.hasNext();) {
			BaseType o = (BaseType) iter.next();
			if (o.validate(this)) {
				if (o.shouldBeDisplayed(getProject())) {
				    o.addToControlPanel(control.getPanel());
				}
				if (o.getIf() != null || o.getUnless() != null) {
					dynamic = true;
				}
			} else {
				needFail = true;
			}
		}
	}
	
	/**
	 * get window height
	 */
	public int getHeight() {
		return height;
	}
	/**
	 * set window height
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	/**
	 * get window width
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * set window width
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	
	/**
	 * add a configured menuProperty
	 */
	public void addConfiguredAntMenuItem(AntMenuItem menuItem){
		if (menuItem.validate(this)) {
			widgets.add(menuItem);
		} else {
			needFail = true;
		}
	}
	/**
	 * set preliminary gui operations, such as 
	 * Look & Feel
	 */
	public void preliminaries(){
		if (control==null){
			control = new Control(this, title, image, tabbed);			
			if (lookAndFeel!=null) {
				control.updateLookAndFeel(lookAndFeel);
			}
		} 				
	}
	
	/**
	 * check that the task can continue and set the look and feel
	 */
	public void execute() throws BuildException {		
		control.setWidth(width);
		control.setHeight(height);
		if (needFail) {
			throw new BuildException("certain properties where not correctly set.");
		}				
		control.setTitle(getTitle());				
		if (stylesheet!=null) {
			try {				
				control.setStyleSheet(stylesheet);
				control.getPanel().setStyleSheet(stylesheet);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		} 				
	}
	
	/**
	 * @return lookAndFeel.
	 */
	public String getLookAndFeel() {
		return lookAndFeel;
	}
	/**
	 * @param lookAndFeel.
	 */
	public void setLookAndFeel(String lookAndFeel) {
		this.lookAndFeel = lookAndFeel;
	}
	
	
	/**
	 * Find a target amid the projet targets
	 */
	protected Target findTargetByName(String target) {
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
	
	/**
	 * @return title.
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * get the styelsheet reference
	 */
	public String getStylesheet() {
		return stylesheet;
	}
	/**
	 * set the styelsheet reference 
	 */
	public void setStylesheet(String stylesheet) {
		this.stylesheet = stylesheet;
	}
	
	public void invokeTarget(String target, boolean background) {
		TargetInvoker invoker = new TargetInvoker(this, target, background);
		invoker.perform();
	}
}
