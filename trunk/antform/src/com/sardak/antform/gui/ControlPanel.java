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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

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

import com.sardak.antform.gui.helpers.CheckValueGetter;
import com.sardak.antform.interfaces.ValueHandle;
import com.sardak.antform.util.MnemonicsUtil;

/**
 * Panel that holds the user form and lays out labels and
 * input fields in two side-by-side columns
 * @author René Ghosh
 */
public class ControlPanel extends JPanel implements ActionListener{
	private boolean disposeOnReset = false; 
	private GridBagLayout layout;	
	private JPanel buttonPanel;	
	private String title, okMessage, resetMessage, cancelMessage;
	private Control control;
	private JLabel topLabel;
	private JPanel southPanel;
	private JPanel topPanel;
	private JPanel overPanel;
//	private JPanel buttonInnerPanel;
	private JPanel currentPanel; 
//	private Font font = null;	
//	private boolean tabbed;
	private Properties properties,defaultProperties;
	private Map controlsMap, requiredMap, mnemonicsMap;
	private HashSet usedLetters;
	private JTabbedPane tabbedPane;
	private StylesheetHandler stylesheetHandler;
	
	public static final String CANCEL_MSG = "CANCEL_EVENT";

    public Control getControl() {
        return control;
    }

	/**
	 * Initiallize local collections
	 */
	public void init() {		
		properties = new Properties();
		defaultProperties = new Properties();		
		controlsMap = new HashMap();
		requiredMap = new HashMap();
		usedLetters = new HashSet();
		mnemonicsMap = new HashMap();
		stylesheetHandler = new StylesheetHandler();
	}
	
	/**
	 * Process action events
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {		
		String command = e.getActionCommand();
		if (command.equals(cancelMessage) || command.equals(CANCEL_MSG)){
			control.close(properties, command);			
		} else if (command.equals(resetMessage)){
			setProperties(defaultProperties);	
			if (disposeOnReset) {
				control.close(properties, resetMessage);
			}
		} else if (command.equals(okMessage)){			
			for (Iterator iter = requiredMap.keySet().iterator(); iter.hasNext();) {
				String property = (String) iter.next();
				ValueHandle vh = (ValueHandle) requiredMap.get(property);
				if ((vh.getValue()==null)||(vh.getValue().trim().length()==0)) {
					vh.getComponent().requestFocus();
					return;
				}
			}
			properties = new Properties();
			for (Iterator i=controlsMap.keySet().iterator();i.hasNext();) {
				String propertyName = (String) i.next();
				ValueHandle vh = (ValueHandle) controlsMap.get(propertyName);
				properties.setProperty(propertyName, vh.getValue());
				if (vh instanceof CheckValueGetter) {
					CheckValueGetter checkBoxGetter = (CheckValueGetter) vh;					
					if (!checkBoxGetter.getValue().equals(Boolean.TRUE+"")){
						properties.remove(propertyName);
						control.setFalse(propertyName);
					}
				}				
			}					
			control.close(properties, okMessage);
		}
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
		gbc.insets = new Insets(5,15,5,5);
		layout.setConstraints(component, gbc);
		currentPanel.add(component);		
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
		gbc.insets = new Insets(5,5,5,15);
		layout.setConstraints(component, gbc);
		currentPanel.add(component);
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
		gbc.insets = new Insets(15,10,15,10);
		layout.setConstraints(component, gbc);
		currentPanel.add(component);			
	}
	
	/**
	 * add a component to the right side of the form
	 * @param component
	 */
	public final void addLinkToLayout(Component component){
		GridBagConstraints gbc = new GridBagConstraints();		
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;		
		gbc.weightx = 0.0;		
		gbc.weighty = 1.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets = new Insets(5,0,5,0);
		layout.setConstraints(component, gbc);
		currentPanel.add(component);
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
		gbc.insets = new Insets(15,10,15,10);
		layout.setConstraints(component, gbc);
		currentPanel.add(component);
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
//		this.tabbed=tabbed;
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
	
	/**
	  * add a blank line
	  */
	public void addBlank(){
		JLabel label = new JLabel();
		label.setOpaque(false);
		addCentered(label);
	}
	
	public void addButtonPanel(ButtonPanel buttons) {
		add(buttons, BorderLayout.SOUTH);
		stylesheetHandler.addPanel(buttons);
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
	public void setStyleSheet(String stylesheetFileName) throws FileNotFoundException, IOException{
		stylesheetHandler.apply(stylesheetFileName, this);
	}

	/**
	 * set the form title
	 * @param title
	 */
	public void setImage(String image){		
		if (image!=null) {
			overPanel.add(new JLabel(new ImageIcon(image)), BorderLayout.WEST);
		}
	}

	/**
	 * set the ok button message
	 * @param message
	 */
	public void setOkMessage(String message){		
		okMessage = message;
	}
	
	/**
	 * set the reset button message
	 */
	public void setResetMessage(String message){
		resetMessage = message;
	}
	
	/**
	 * Focus on a named field 
	 */
	public void focus(String focusEntity){
		ValueHandle vh= (ValueHandle) controlsMap.get(focusEntity);
		if (vh==null) {
			return;
		}
		JComponent comp = (JComponent) vh.getComponent();		
		if (comp!=null) {			
			comp.requestFocus();
		} 
	}
	
	
	/**
	 * set the flag governing wether the 
	 * frame must dispose whe, the reset button
	 * is hit 
	 */
	public void setDisposeOnReset(boolean disposeOnReset){
		this.disposeOnReset=disposeOnReset;
	}
	
	/**
	 * @return properties.
	 */
	public Properties getProperties() {
		return properties;
	}
	
	/**
	 * @param set the Properties.
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
		this.defaultProperties=(Properties) properties.clone();		
		for (Iterator iter = properties.keySet().iterator(); iter.hasNext();) {
			String property = (String) iter.next();
			if (controlsMap.containsKey(property)) {
				String value = properties.getProperty(property);
				ValueHandle vh = (ValueHandle) controlsMap.get(property);
				vh.setValue(value);
			}
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
	
	public void addControl(String propertyName, ValueHandle valueHandle) {
	    addControl(propertyName, valueHandle, false);
	}

	public void addControl(String propertyName, ValueHandle valueHandle, boolean required) {
	    controlsMap.put(propertyName, valueHandle);
		if (required){
			requiredMap.put(propertyName, valueHandle);
		}
	}

	public void addRequired(String propertyName, ValueHandle valueHandle) {
	    requiredMap.put(propertyName, valueHandle);
	}
	
	public void setCancelMessage(String cancelMessage) {
	    this.cancelMessage = cancelMessage;
	}
	
	public void addToTabbedPane(String label, JPanel tabPanel, GridBagLayout aLayout) {
	    tabbedPane.addTab(label, tabPanel);
	    stylesheetHandler.addPanel(tabPanel);
		currentPanel = tabPanel;
		layout = aLayout;
	}
	
	public void listenToLink(JButton link, final String target, final boolean background) {
		link.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
					control.executeLink(target, background);
			}
		});		
	}
}
