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
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.sardak.antform.gui.helpers.CheckValueGetter;
import com.sardak.antform.interfaces.ValueHandle;
import com.sardak.antform.style.HexConverter;
import com.sardak.antform.util.MnemonicsUtil;
import com.sardak.antform.util.StyleUtil;

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
	private JPanel buttonInnerPanel;
	private JPanel currentPanel; 
//	private JButton resetButton, okButton, cancel;	
	private Font font = null;	
	private boolean tabbed;
	private Properties properties,defaultProperties;
	private Map controlsMap, requiredMap, mnemonicsMap;
	private HashSet usedLetters;
	private JTabbedPane tabbedPane;
	private List textProperties,
		selectionProperties, numberProperties,links,
		multiLineTextProperties, buttons, dateProperties,
		labels, checkBoxes, messages, scrollPanes,
		fileChoosers, radioButtons, panels;	

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
		textProperties = new ArrayList();
		selectionProperties = new ArrayList();
		numberProperties =  new ArrayList();
		links = new ArrayList();
		multiLineTextProperties = new ArrayList();
		buttons = new ArrayList();
		dateProperties = new ArrayList();
		labels = new ArrayList();
		checkBoxes = new ArrayList();
		messages  = new ArrayList();
		scrollPanes=new ArrayList();
		fileChoosers=new ArrayList();
		radioButtons = new ArrayList();
		panels = new ArrayList();
		mnemonicsMap = new HashMap();
	}
	
	/**
	 * Process action events
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {		
		String command = e.getActionCommand();
		if (command.equals(cancelMessage)){
			control.close(properties, "");			
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
		labels.add(label);
		if (font!=null) {
			label.setFont(font);
		} 
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
		this.tabbed=tabbed;
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
	/**
	 * add button controls to the panel
	 */
//	public void addButtonControls(String okMessage, String resetMessage){		
//		buttonPanel = new JPanel();		
//		buttonPanel.setBorder(BorderFactory.createEmptyBorder());
//		buttonInnerPanel = new JPanel();
//		buttonInnerPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
//		add(buttonPanel, BorderLayout.SOUTH);
//		buttonInnerPanel.setLayout(new BoxLayout(buttonInnerPanel, BoxLayout.X_AXIS));
//		buttonInnerPanel.setOpaque(false);
//		buttonPanel.setOpaque(true);
//		buttonPanel.setLayout(new BorderLayout());
//		buttonPanel.add(buttonInnerPanel, BorderLayout.CENTER);
//		resetButton=new JButton(resetMessage);
//		okButton=new JButton(okMessage);
//		resetButton.addActionListener(this);		
//		okButton.addActionListener(this);		
//		addToButtonInnerPanel(Box.createHorizontalGlue());
//		addToButtonInnerPanel(resetButton);
//		addToButtonInnerPanel(Box.createHorizontalStrut(10));
//		addToButtonInnerPanel(okButton);						
//		setOkMessage(okMessage);
//		setResetMessage(resetMessage);
//		addButton(okButton);
//		addButton(resetButton);
//		setMnemonics(okButton, okButton.getText());
//		setMnemonics(resetButton, resetButton.getText());
//		controlsMap.put(okButton.getText(), new ButtonValueGetter(okButton));
//		controlsMap.put(resetButton.getText(), new ButtonValueGetter(resetButton));
//	}
	
	public void addButtonPanel(ButtonPanel buttons) {
		add(buttons, BorderLayout.SOUTH);
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
	
	/**
	 * set the form stylesheet
	 * @param title
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void setStyleSheet(String styleSheet) throws FileNotFoundException, IOException{
		Properties props = new Properties();
		props.load(new FileInputStream(new File(styleSheet)));		
		Color background = HexConverter.translate(props.getProperty("background.color"), Color.GRAY);
		Color foreground = HexConverter.translate(props.getProperty("color"), Color.BLACK);
		List banners = new ArrayList();
		banners.add(overPanel);		
		if (southPanel != null) {
			southPanel.setBackground(background);
		}
		Set buttonBars = new HashSet();
		buttonBars.add(buttonPanel);
		StyleUtil.styleComponents("buttonBar", props, buttonBars);
		for (Iterator iter = panels.iterator(); iter.hasNext();) {
			JPanel aPanel  = (JPanel) iter.next();
			aPanel.setBackground(background);
		}
		currentPanel.setBackground(background);
		setBackground(background);
		StyleUtil.styleComponents("banner", props, banners);
		font = new Font(props.getProperty("font.family"), Font.PLAIN, Integer.parseInt(props.getProperty("font.size")));
		StyleUtil.styleComponents("label", props, labels);
		StyleUtil.styleComponents("textProperty", props, dateProperties);
		StyleUtil.styleComponents("textProperty", props, textProperties);
		StyleUtil.styleComponents("textProperty", props, numberProperties);
		StyleUtil.styleComponents("textProperty", props, fileChoosers);
		StyleUtil.styleComponents("multiLineTextProperty", props, multiLineTextProperties); 
		StyleUtil.styleComponents("link", props, links);
		StyleUtil.styleComponents("scrollPanes", props, scrollPanes);
		StyleUtil.styleComponents("selectionProperty", props, selectionProperties);
		StyleUtil.styleComponents("button", props, buttons);
		StyleUtil.styleComponents("checkbox", props, checkBoxes);
		StyleUtil.styleComponents("message", props, messages);
		StyleUtil.styleComponents("radio", props, radioButtons);
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
//		usedLetters.remove(mnemonicsMap.get(okButton));
//		mnemonicsMap.remove(okButton);
//		controlsMap.remove(okButton.getText());
//		okButton.setText(message);		
		okMessage = message;
//		setMnemonics(okButton, okButton.getText());
//		controlsMap.put(okButton.getText(), new ButtonValueGetter(okButton));
	}
	
	/**
	 * set the reset button message
	 */
	public void setResetMessage(String message){
//		usedLetters.remove(mnemonicsMap.get(resetButton));
//		mnemonicsMap.remove(resetButton);		
//		controlsMap.remove(resetButton.getText());
//		resetButton.setText(message);
		resetMessage = message;
//		setMnemonics(resetButton, resetButton.getText());
//		controlsMap.put(resetButton.getText(), new ButtonValueGetter(resetButton));				
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
	
	public void addCheckBox(JCheckBox checkBox) {
	    checkBoxes.add(checkBox);
	}

	public void addCheckGroupBox(CheckGroupBox checkGroupBox) {
	    checkBoxes.add(checkGroupBox);
	}

	public void addRadioGroupBox(RadioGroupBox radioBox) {
	    radioButtons.add(radioBox);
	}

	public void addDateChooser(DateChooser chooser) {
		dateProperties.add(chooser);
	}
	
	public void addFileChooser(FileChooser chooser) {
		fileChoosers.add(chooser);
	}
	
	public void addSpinner(JSpinner spinner) {
	    numberProperties.add(spinner);
	}
	
	public void addTextField(JTextField textField) {
	    textProperties.add(textField);
	}
	
	public void addMultiLineTextArea(JTextArea textArea) {
	    multiLineTextProperties.add(textArea);
	}
	
	public void addScrollPane(JScrollPane scrollPane) {
	    scrollPanes.add(scrollPane);
	}
	
	public void addComboBox(JComboBox comboBox) {
	    selectionProperties.add(comboBox);
	}
	
	public void addButton(JButton button) {
	    buttons.add(button);
	}
	
	public void addLink(JButton link) {
	    links.add(link);
	}
	
	public void addMessage(JTextArea textArea) {
	    messages.add(textArea);
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
	
//	public void addToButtonInnerPanel(Component component) {
//	    buttonInnerPanel.add(component);
//	}
	
	public void addToTabbedPane(String label, JPanel tabPanel, GridBagLayout aLayout) {
	    tabbedPane.addTab(label, tabPanel);
	    panels.add(tabPanel);
		currentPanel = tabPanel;
		layout = aLayout;
	}
	
	public void listenToLink(JButton link, final String target) {
		link.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
					control.executeLink(target);
			}
		});		
	}

	public void setTeleport(JButton link, final String target) {
		link.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
					control.executeTeleport(target);
			}
		});		
	}
}
