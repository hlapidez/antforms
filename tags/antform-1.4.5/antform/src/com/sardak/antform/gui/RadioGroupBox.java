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
package com.sardak.antform.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * @author René Ghosh
 * 13 mars 2005
 */
public class RadioGroupBox extends JPanel{
	private List buttons = new ArrayList();
	private ButtonGroup group = new ButtonGroup();
	
	/**
	 * private constructor
	 * Constructor
	 */
	private RadioGroupBox(){
	}

	/**
	 * Constructor
	 */
	public RadioGroupBox(String[] values){			
		super();
		setLayout(new GridLayout(0,1));
		for (int i = 0; i < values.length; i++) {
			JRadioButton button = new JRadioButton(values[i]);
			add(button);			
			buttons.add(button);
			group.add(button);
		}
	}
	
	/**
	 * set the font
	 */
	public void setFont(Font font) {
		super.setFont(font);
		if (buttons!=null) {
			for (Iterator iter = buttons.iterator(); iter.hasNext();) {
				JRadioButton button = (JRadioButton) iter.next();
				button.setFont(font);
			}
		}
	}

	
	/**
	 * set background
	 */
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if (buttons!=null) {
			for (Iterator iter = buttons.iterator(); iter.hasNext();) {
				JRadioButton button = (JRadioButton) iter.next();
				button.setBackground(bg);
			}
		}
	}
	
	
	/**
	 * set foreground
	 */
	public void setForeground(Color fg) {
		super.setForeground(fg);
		if (buttons!=null) {
			for (Iterator iter = buttons.iterator(); iter.hasNext();) {
				JRadioButton button = (JRadioButton) iter.next();
				button.setForeground(fg);
			}
		}
	}

	/**
	 * set selected value
	 */
	public void setSelectedValue(String value){
		if (buttons!=null) {
			for (Iterator iter = buttons.iterator(); iter.hasNext();) {
				JRadioButton button = (JRadioButton) iter.next();
				if (button.getText().equals(value)){
					button.setSelected(true);
				}
			}
		}
	}
	
	/**
	 * get selected value
	 */
	public String getSelectedValue(){
		String local = "";
		if (buttons!=null) {
			for (Iterator iter = buttons.iterator(); iter.hasNext();) {
				JRadioButton button = (JRadioButton) iter.next();
				if (button.isSelected()){
					local = button.getText();
				}
			}
		}
		return local;
	}
	
	/**
	 * get selected value
	 */
	public int getSelectedIndex(){
		int local = -1;		
		if (buttons!=null) {			
			for (int i = 0;i<buttons.size(); i++) {
				JRadioButton button = (JRadioButton) buttons.get(i);
				if (button.isSelected()){
					local = i;
				}
			}
		}
		return local;
	}
	
	/**
	 * Enable the component
	 */
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (buttons!=null) {
			for (Iterator iter = buttons.iterator(); iter.hasNext();) {
				JRadioButton button = (JRadioButton) iter.next();
				button.setEnabled(enabled);
			}
		}
	}
}
