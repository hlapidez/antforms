 /***************************************************************************\*
 *                                                                            *
 *    AntForm form-based interaction for Ant scripts                          *
 *    Copyright (C) 2005 Ren� Ghosh                                           *
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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.tools.ant.BuildException;

import com.sardak.antform.gui.CallBack;
import com.sardak.antform.types.Label;
import com.sardak.antform.types.Link;
import com.sardak.antform.types.Separator;
import com.sardak.antform.util.TargetInvoker;

/**
 * AntMenu task definition class.
 * @author Ren� Ghosh
 * 12 janv. 2005
 */
public class AntMenu extends AbstractTaskWindow implements CallBack{
	private String nextTarget = null;
	private boolean built = false;
	/**
	 * Initialisation
	 */
	public void init() throws BuildException {
		super.init();
		widgets = new ArrayList();		
	}
	/**
	 * build the visual components
	 */
	protected void build(){
		super.build();		
		control.getPanel().addBlankSouthPanel();
		built = true;
	}
	
	/**
	 * add a configured separator
	 * @param textProperty
	 */
	public void addConfiguredSeparator(Separator separator){		
		widgets.add(separator);
	}
	
	/**
	 * execute method
	 */
	public void execute() throws BuildException {
		preliminaries();
		if (!built) {
			build();
		}
		super.execute();		
		control.show();
		if (dynamic) {
			built = false;
			control = null;
		}
		if (nextTarget!=null) {
			TargetInvoker invoker = new TargetInvoker(this, nextTarget, false);
			invoker.perform();
		}
	}
	
	/**
	 * implement a callback that ports to a 
	 * target by autumatically setting okMessage and nextTarget
	 * values 
	 */
//	public void callbackLink(String target) {		
//		nextTarget = target;
//	}
	
	/**
	 * add a configured link
	 */
	public void addConfiguredLink(Link link) {
		widgets.add(link);
	}
	
	/**
	 * add a configured link
	 */
	public void addConfiguredLabel(Label label) {
		widgets.add(label);
	}
		
	
	/**
	 * callback for control panel
	 */
	public void callbackCommand(String message){
		if (message==null) {
			return;
		}
		Hashtable targets = getProject().getTargets();		
		for (Iterator i=targets.keySet().iterator();i.hasNext();) {
			String targetName = (String) i.next();			
			if (targetName.equals(message)) {
				nextTarget = targetName;
				break;
			}
		}
	}
}