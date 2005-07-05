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
package com.sardak.antform.types;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import com.sardak.antform.gui.ControlPanel;
import com.sardak.antform.interfaces.ValueHandle;
import com.sardak.antform.util.MnemonicsUtil;

/**
 * @author René Ghosh
 * 11 mars 2005
 */
public class AntMenuItem extends BaseType{
	private List subProperties = new ArrayList();
	private HashSet usedLetters = new HashSet();
	private String target,name;
	private JMenuBar menuBar;
	private boolean background = false;
	
	
	public boolean isBackground() {
		return background;
	}
	
	public void setBackground(boolean background) {
		this.background = background;
	}
	
	/**
	 * get the list of subProperties
	 */
	public List getSubProperties(){
		return subProperties;
	}
	/**
	 * get the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * set the name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * get the target
	 */
	public String getTarget() {
		return target;
	}
	
	/**
	 * set the target
	 */
	public void setTarget(String target) {
		this.target = target;
	}
	/**
	 * Add another configured property to this one
	 */
	public void addConfiguredAntMenuItem(AntMenuItem menuProperty) {
		subProperties.add(menuProperty);
	}
	public HashSet getUsedLetters() {
		return usedLetters;
	}
	public void setUsedLetters(HashSet usedLetters) {
		this.usedLetters = usedLetters;
	}

	public ValueHandle addToControlPanel(ControlPanel panel) {
		if (menuBar==null) {
			menuBar = new JMenuBar();
			panel.getControl().setMenuBar(menuBar);
		}
		JMenu menu = new JMenu(name);
		panel.addMenu(menu);
		String sToUse = MnemonicsUtil.newMnemonic(name, panel.getUsedLetters());
		if (sToUse!=null) {
			menu.setMnemonic(sToUse.charAt(0));
		}
		menuBar.add(menu);
		panel.getControl().addMenuItems(this, menu);
		return null;
	}
}
