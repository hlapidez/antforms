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
package com.sardak.antform.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.sardak.antform.util.MnemonicsUtil;

/**
 * Panel that holds the user form and lays out labels and
 * input fields in two side-by-side columns
 * @author Ren� Ghosh
 */
public class ControlPanel extends JPanel {
	private GridBagLayout layout;	
	private JPanel buttonPanel;	
	private String title;
	private Control control;
	private JLabel topLabel;
	private JPanel southPanel;
	private JPanel topPanel;
	private JPanel overPanel;
	private JPanel currentPanel; 
	private Map mnemonicsMap;
	private HashSet usedLetters;
	private JTabbedPane tabbedPane;
	private StylesheetHandler stylesheetHandler;
	
	// empty space on left
	private final static int LEFT_MARGIN = 15;
	// empty space on right
	private final static int RIGHT_MARGIN = 15;
	// horizontal gap between components
	private final static int HGAP = 5;
	// vertical gap between components
	private final static int VGAP = 5;
	
	private final boolean DEBUG_BORDERS = false;
	
    public Control getControl() {
        return control;
    }

	/**
	 * Initiallize local collections
	 */
	public void init() {		
		usedLetters = new HashSet();
		mnemonicsMap = new HashMap();
		stylesheetHandler = new StylesheetHandler();
	}
	
	/**
	 * add a component to the left side of the form
	 * @param component
	 */
	private final void addLeft(Component component){
		GridBagConstraints gbc = new GridBagConstraints();			
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.gridwidth=1;
		gbc.insets = new Insets(VGAP, LEFT_MARGIN, VGAP, HGAP);
		layout.setConstraints(component, gbc);
		currentPanel.add(component);
		if (DEBUG_BORDERS) {
			debugBorders((JComponent) component, Color.GREEN);
		}
	}
	
	/**
	 * Construct a label and add it into the left column
	 */
	public JLabel makeLabel(String labelText){
		JLabel label = new JLabel(labelText);
		stylesheetHandler.addLabel(label);
		addLeft(label);				
		setMnemonics(label, labelText);						
		return label;
	}

	/**
	 * set mnemonics for component
	 */
	public void setMnemonics(JComponent component, String labelText) {
		if (mnemonicsMap.containsKey(component)){
			return;		
		}		
		String sToUse=null;
		boolean isSet = false;
		sToUse = MnemonicsUtil.newMnemonic(labelText, usedLetters);
		isSet = (sToUse!=null);
		if (isSet){
			char toUse=sToUse.charAt(0);
			if (component instanceof JLabel){
				((JLabel) component).setDisplayedMnemonic(toUse);
			} else if (component instanceof JButton){
				((JButton) component).setMnemonic(toUse);
			}
			mnemonicsMap.put(component, sToUse);
		}
	}

	/**
	 * add a component to the right side of the form
	 * @param component
	 */
	public final void addRight(Component component){
		GridBagConstraints gbc = new GridBagConstraints();		
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.gridwidth= GridBagConstraints.REMAINDER;
		gbc.insets = new Insets(VGAP, HGAP, VGAP, RIGHT_MARGIN);
		layout.setConstraints(component, gbc);
		currentPanel.add(component);
		if (DEBUG_BORDERS) {
			debugBorders((JComponent) component, Color.BLUE);
		}
	}	
	
	/**
	 * add a component to the right side of the form
	 * @param component
	 */
	public final void addCentered(Component component){
		GridBagConstraints gbc = new GridBagConstraints();		
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;		
		gbc.weightx = 0.0;		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets = new Insets(VGAP, LEFT_MARGIN, VGAP, RIGHT_MARGIN);
		layout.setConstraints(component, gbc);
		currentPanel.add(component);
		if (DEBUG_BORDERS) {
			debugBorders((JComponent) component, Color.RED);
		}
	}
	
	/**
	 * add a component to the right side of the form
	 * @param component
	 */
	public final void addCenteredNoFill(Component component){
		GridBagConstraints gbc = new GridBagConstraints();		
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.NONE;		
		gbc.weightx = 0.0;		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets = new Insets(15,LEFT_MARGIN,15,RIGHT_MARGIN);
		layout.setConstraints(component, gbc);
		currentPanel.add(component);
		if (DEBUG_BORDERS) {
			debugBorders((JComponent) component, Color.RED);
		}
	}	
	
	/**
	 * Etched border with insets 
	 */
	private static Border bigEtched() {
		Border eBorder = BorderFactory.createEmptyBorder(5,5,5,5);
		Border bBorder = BorderFactory.createEtchedBorder();
		return BorderFactory.createCompoundBorder(bBorder, eBorder);
	}

	
	/**
	 * Etched border with insets 
	 */
	public static Border linkBorder() {
		Border eBorder = BorderFactory.createEmptyBorder(0,100,0,100);
		Border bBorder = BorderFactory.createEmptyBorder();
		return BorderFactory.createCompoundBorder(bBorder, eBorder);		
	}
	
	/**
	 * Constructor	 
	 */
	public ControlPanel(Control control, boolean tabbed){
		this.control = control;
		topLabel = new JLabel(title, JLabel.CENTER);
		Font font = topLabel.getFont();
		font = font.deriveFont((float) 16.0);		
		font = font.deriveFont(Font.BOLD);		
		topLabel.setOpaque(false);		
		topLabel.setFont(font);
		setLayout(new BorderLayout());
		overPanel = new JPanel();
		topPanel = new JPanel();
		topPanel.setOpaque(false);
		overPanel.setLayout(new BorderLayout());
		overPanel.add(topPanel, BorderLayout.CENTER);				
		overPanel.setOpaque(true);
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		topPanel.add(Box.createHorizontalStrut(10));
		topPanel.add(topLabel);
		topPanel.add(Box.createHorizontalStrut(10));
		overPanel.setBorder(bigEtched());
		add(overPanel, BorderLayout.NORTH);		
		layout = new GridBagLayout();
		currentPanel = new JPanel();
		currentPanel.setBorder(BorderFactory.createEmptyBorder(15,5,15,5));
		currentPanel.setLayout(layout);
		if (tabbed){
			tabbedPane = new JTabbedPane();			
			add(tabbedPane, BorderLayout.CENTER);			
		} else {					
			add(currentPanel, BorderLayout.CENTER);					
		}
	}
	
	public void addButtonPanel(JPanel buttonPanel) {
		add(buttonPanel, BorderLayout.SOUTH);
		stylesheetHandler.addPanel(buttonPanel);
		this.buttonPanel = buttonPanel;
	}
	
	/**
	 * add a blank panel
	 */
	public void addBlankSouthPanel(){		
		southPanel = new JPanel();
		southPanel.setOpaque(true);
		southPanel.setBorder(linkBorder());
		add(southPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * set the form title
	 * @param title
	 */
	public void setTitle(String title){		
		topLabel.setText(title);					
	}
	
	public JPanel getOverPanel() {
		return overPanel;
	}
	
	public JPanel getSouthPanel() {
		return southPanel;
	}
	
	public JPanel getButtonPanel() {
		return buttonPanel;
	}
	
	public JPanel getCurrentPanel() {
		return currentPanel;
	}
	
	public StylesheetHandler getStylesheetHandler() {
		return stylesheetHandler;
	}

	/**
	 * set the form stylesheet
	 * @param title
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void setStyleSheet(File stylesheet) throws IOException{
		stylesheetHandler.apply(stylesheet, this);
	}

	/**
	 * set the form title
	 * @param title
	 */
	public void setImage(File image){		
		if (image!=null) {
			overPanel.add(new JLabel(new ImageIcon(image.getAbsolutePath())), BorderLayout.WEST);
		}
	}

	public HashSet getUsedLetters() {
		return usedLetters;
	}
	public void setUsedLetters(HashSet usedLetters) {
		this.usedLetters = usedLetters;
	}
	
	public void addMenu(JMenu menu) {
	    control.addMenu(menu);
	}
	
	public void addToTabbedPane(String label, JPanel tabPanel, GridBagLayout aLayout) {
	    tabbedPane.addTab(label, tabPanel);
	    stylesheetHandler.addPanel(tabPanel);
		currentPanel = tabPanel;
		layout = aLayout;
	}
	
	public void activateTab(int tabIndex) {
		if (tabbedPane != null && tabIndex > -1 && tabIndex < tabbedPane.getTabCount()) {
			tabbedPane.setSelectedIndex(tabIndex);
		}
	}
	
	private void debugBorders(JComponent jComponent, Color borderColor) {
		jComponent.setBorder(BorderFactory.createCompoundBorder(new LineBorder(borderColor, 1), jComponent.getBorder()));
	}
}
