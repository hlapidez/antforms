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

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.sardak.antform.util.CSVReader;
import com.sardak.antform.util.StringUtil;


 /**
  * @author René Ghosh
  * 13 mars 2005
  */
 public class CheckGroupBox extends JPanel{
 	private List buttons = new ArrayList();
 	private String separator, escapeSequence;
 	
 	/**
 	 * private constructor
 	 * Constructor
 	 */
 	private CheckGroupBox(){
 	}

 	/**
 	 * Constructor
 	 */
 	public CheckGroupBox(String[] values, String separator, String escapeSequence, int columns){			
 		super();
 		this.separator=separator;
 		this.escapeSequence=escapeSequence;
 		setLayout(new GridLayout(0, columns));
 		for (int i = 0; i < values.length; i++) {
 			JCheckBox button = new JCheckBox(values[i]);
 			add(button);			
 			buttons.add(button); 			
 		}
 	}
 	
 	/**
 	 * set the font
 	 */
 	public void setFont(Font font) {
 		super.setFont(font);
 		if (buttons!=null) {
 			for (Iterator iter = buttons.iterator(); iter.hasNext();) {
 				JCheckBox button = (JCheckBox) iter.next();
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
 				JCheckBox button = (JCheckBox) iter.next();
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
 				JCheckBox button = (JCheckBox) iter.next();
 				button.setForeground(fg);
 			}
 		}
 	}

 	/**
 	 * set selected value
 	 */
 	public void setValue(String value){
 		CSVReader reader = new CSVReader(separator, escapeSequence);
		List valueList = reader.digest(value, true); 		
 		for (Iterator iter = valueList.iterator(); iter.hasNext();) {
			String aValue = (String) iter.next();			
			for (Iterator iterator = buttons.iterator(); iterator.hasNext();) {
				JCheckBox checkBox = (JCheckBox) iterator.next();
				if (checkBox.getText().equals(aValue)){
					checkBox.setSelected(true);
				}
			} 			
 		} 		
 	}
 	
 	/**
 	 * get selected value
 	 */
 	public String getValue(){
 		String local = "";
 		if (buttons!=null) {
 			int counter = -1;
 			for (Iterator iter = buttons.iterator(); iter.hasNext();) {
 				counter++;
 				JCheckBox button = (JCheckBox) iter.next();
 				String text = button.getText();
 				if (button.isSelected()){
 					if (local.length()>0) {
 						local += separator;
 					}
 					text = StringUtil.searchReplace(text, escapeSequence, escapeSequence+escapeSequence);
 					text = StringUtil.searchReplace(text, separator, escapeSequence+separator);
 					local += text;
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
 				JCheckBox button = (JCheckBox) buttons.get(i);
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
 				JCheckBox button = (JCheckBox) iter.next();
 				button.setEnabled(enabled);
 			}
 		}
 	}
 }
