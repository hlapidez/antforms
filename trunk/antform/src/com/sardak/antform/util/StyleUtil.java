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
package com.sardak.antform.util;

import java.awt.Color;
import java.awt.Font;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.text.JTextComponent;

import com.sardak.antform.gui.CheckGroupBox;
import com.sardak.antform.gui.RadioGroupBox;
import com.sardak.antform.style.FontConverter;
import com.sardak.antform.style.HexConverter;

/**
 * utility for managing style behaviour
 * @author René Ghosh
 * 13 mars 2005
 */
public class StyleUtil {
	/**
	 * set style to components
	 */
	public static void styleComponents(String componentId, Properties props, Collection components) {
		String componentBCString = props.getProperty(componentId+".background.color", props.getProperty("*.background.color"));
		String componentFCString = props.getProperty(componentId+".color", props.getProperty("*.color"));		
		String componentFFamilyString = props.getProperty(componentId+".font.family", props.getProperty("*.font.family"));
		String componentFSizeString = props.getProperty(componentId+".font.size", props.getProperty("*.font.size"));
		String componentFWeightString = props.getProperty(componentId+".font.weight", props.getProperty("*.font.weight"));
		String componentBStyleString = props.getProperty(componentId+".border.style", props.getProperty("*.border.style"));
		String componentBWidthString = props.getProperty(componentId+".border.width", props.getProperty("*.border.width"));
		String componentBColorString = props.getProperty(componentId+".border.color", props.getProperty("*.border.color"));		
		int componentBWidth = 1;
		Color componentBC = null;
		Color componentFC = null;
		Color componentBorderC = null;
		Border componentBorder = null;
		if (componentBCString!=null) {
			componentBC = HexConverter.translate(componentBCString, null);
		}		
		if (componentFCString!=null) {
			componentFC = HexConverter.translate(componentFCString, null);
		}		
		if ((componentBWidthString!=null)&&(componentBColorString!=null)&&(componentBStyleString!=null)) {
			componentBWidth = Integer.parseInt(componentBWidthString);
			componentBorderC = HexConverter.translate(componentBColorString, null);
			componentBorder = BorderFactory.createLineBorder(componentBorderC, componentBWidth);			
		}		
		for (Iterator i=components.iterator();i.hasNext();) {
			JComponent comp = (JComponent) i.next();
			if (comp==null) {
				continue;
			}
			if (componentBC!=null) {
				comp.setBackground(componentBC);				
				if (comp instanceof JTextComponent) {
					JTextComponent textComp = (JTextComponent) comp;
					Color inverseColor = new Color(255-(int) (componentBC.getRed()*0.7),
						255-(int) (componentBC.getGreen()*0.7),255-(int) (componentBC.getBlue()*0.7));
					textComp.setSelectedTextColor(inverseColor);
				}
			}						
			if (componentFC!=null) {
				comp.setForeground(componentFC);
				if (comp instanceof JTextComponent) {
					JTextComponent textComp = (JTextComponent) comp;					
					Color inverseColor = new Color(255-(int) (componentFC.getRed()*0.7),
						255-(int) (componentFC.getGreen()*0.7),255-(int) (componentFC.getBlue()*0.7));
					textComp.setSelectionColor(inverseColor.darker());					
				}				
			}
			setFont(comp, componentFFamilyString, componentFSizeString, componentFWeightString);
			if ((componentBorder!=null)&&(componentBStyleString.trim().toLowerCase().equals("solid"))) {
				if (comp instanceof JButton) {
					comp.setBorder(new CompoundBorder(componentBorder,BorderFactory.createEmptyBorder(5,5,5,5)));
//				} else if (comp instanceof JCheckBox){									
//				} else if (comp instanceof JComboBox){
//					JComboBox combo = (JComboBox) comp;
				} else {
					comp.setBorder(componentBorder);
				}
			}			
		}
	}
	
	private static void setFont(JComponent component, String componentFFamilyString, String componentFSizeString, String componentFWeightString) {
		Font currentFont = component.getFont();
		String family = componentFFamilyString == null ? currentFont.getFamily() : componentFFamilyString;
		int size = componentFSizeString == null ? currentFont.getSize() : Integer.parseInt(componentFSizeString);
		int style = componentFWeightString == null ? currentFont.getStyle() : FontConverter.convert(componentFWeightString);
		component.setFont(new Font(family, style, size));
	}
}
