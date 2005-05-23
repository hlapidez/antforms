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
import java.awt.Dimension;
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
import java.util.Enumeration;
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
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.table.TableColumn;

import com.sardak.antform.gui.helpers.ButtonValueGetter;
import com.sardak.antform.gui.helpers.CheckValueGetter;
import com.sardak.antform.gui.helpers.ComboIndiceGetter;
import com.sardak.antform.gui.helpers.DateChooserGetter;
import com.sardak.antform.gui.helpers.FileChooserGetter;
import com.sardak.antform.gui.helpers.MultiCheckGetter;
import com.sardak.antform.gui.helpers.RadioGetter;
import com.sardak.antform.gui.helpers.SpinnerValueGetter;
import com.sardak.antform.gui.helpers.TableGetter;
import com.sardak.antform.gui.helpers.TextValueGetter;
import com.sardak.antform.interfaces.ValueHandle;
import com.sardak.antform.interfaces.ValueIndiceHandle;
import com.sardak.antform.style.HexConverter;
import com.sardak.antform.types.Link;
import com.sardak.antform.types.LinkBar;
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
	private static final boolean VERBOSE = false;	
	private String title, okMessage, resetMessage, cancelMessage;
	private Control control;
	private JLabel topLabel;
	private JPanel southPanel;
	private JPanel topPanel;
	private JPanel overPanel;
	private JPanel buttonInnerPanel;
	private JPanel currentPanel; 
	private JButton resetButton, okButton, cancel;	
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
	 * Log a message if in verbose mode
	 * @param message
	 */
	private static final void log(String message) {
		if (VERBOSE) {
			System.out.println(message);
		}
	}
	
	
	/**
	 * Check wether a String is null or not
	 */
	private static boolean isNullText(String text){
		return ((text==null)||(text.trim().length()==0));
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
	private JLabel makeLabel(String labelText){
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
	private void setMnemonics(JComponent component, String labelText) {
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
	private final void addRight(Component component){
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
	private final void addCentered(Component component){
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
	private final void addLinkToLayout(Component component){
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
	private final void addCenteredNoFill(Component component){
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
	private static Border linkBorder() {
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
	public void addButtonControls(String okMessage, String resetMessage){		
		buttonPanel = new JPanel();		
		buttonPanel.setBorder(BorderFactory.createEmptyBorder());
		buttonInnerPanel = new JPanel();
		buttonInnerPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		add(buttonPanel, BorderLayout.SOUTH);
		buttonInnerPanel.setLayout(new BoxLayout(buttonInnerPanel, BoxLayout.X_AXIS));
		buttonInnerPanel.setOpaque(false);
		buttonPanel.setOpaque(true);
		buttonPanel.setLayout(new BorderLayout());
		buttonPanel.add(buttonInnerPanel, BorderLayout.CENTER);
		resetButton=new JButton(resetMessage);
		okButton=new JButton(okMessage);
		resetButton.addActionListener(this);		
		okButton.addActionListener(this);		
		buttonInnerPanel.add(Box.createHorizontalGlue());
		buttonInnerPanel.add(resetButton);
		buttonInnerPanel.add(Box.createHorizontalStrut(10));
		buttonInnerPanel.add(okButton);						
		setOkMessage(okMessage);
		setResetMessage(resetMessage);
		buttons.add(okButton);
		buttons.add(resetButton);
		setMnemonics(okButton, okButton.getText());
		setMnemonics(resetButton, resetButton.getText());
		controlsMap.put(okButton.getText(), new ButtonValueGetter(okButton));
		controlsMap.put(resetButton.getText(), new ButtonValueGetter(resetButton));
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
		usedLetters.remove(mnemonicsMap.get(okButton));
		mnemonicsMap.remove(okButton);
		controlsMap.remove(okButton.getText());
		okButton.setText(message);		
		okMessage = message;
		setMnemonics(okButton, okButton.getText());
		controlsMap.put(okButton.getText(), new ButtonValueGetter(okButton));
	}
	
	/**
	 * set the reset button message
	 */
	public void setResetMessage(String message){
		usedLetters.remove(mnemonicsMap.get(resetButton));
		mnemonicsMap.remove(resetButton);		
		controlsMap.remove(resetButton.getText());
		resetButton.setText(message);
		resetMessage = message;
		setMnemonics(resetButton, resetButton.getText());
		controlsMap.put(resetButton.getText(), new ButtonValueGetter(resetButton));				
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
	 * add a text property to the form
	 * @param label
	 * @param property
	 * @param required
	 */
	public ValueHandle addTextProperty(String label, String property, int numberColumns,
			boolean editable, boolean isPassword, boolean required){
		JLabel labelComponent = makeLabel(label);
		JTextField textField = null;		
		if (!isPassword) {
			textField = new JTextField(numberColumns);
		} else {
			textField = new JPasswordField(numberColumns);
		}		
		textProperties.add(textField);
		textField.setEditable(editable);
		labelComponent.setLabelFor(textField);
		addRight(textField);
		TextValueGetter valueGetter = new TextValueGetter(textField);
		controlsMap.put(property,valueGetter);
		if (required) {
			requiredMap.put(property, valueGetter);
		}
		return valueGetter;
	}
	
	/**
	 * add a table property
	 */
	public ValueHandle addTableProperty(String label, String property,
			boolean editable, String[] columns, String[][] data,
			String rowSeparator, String columnSeparator,
			String escapeSequence, int width, int height, int columnWidth, boolean bestFitColumns){
		JLabel labelComponent = makeLabel(label);			
		AntTable table = new AntTable(data, columns);		
		table.setEnabled(editable);
		if (columnWidth!=-1) {
			table.setAutoResizeMode(AntTable.AUTO_RESIZE_OFF);
			for (Enumeration e = table.getColumnModel().getColumns();e.hasMoreElements();) {
				TableColumn tc = (TableColumn) e.nextElement();
				tc.setPreferredWidth(columnWidth);
			}
		}
		if (bestFitColumns) {
		    table.bestFitColumns();
		}
		JScrollPane scrollPane = new JScrollPane(table);
		if ((width>0)&&(height>0)) {			
			scrollPane.setPreferredSize(new Dimension(width, height));
			table.setAutoResizeMode(AntTable.AUTO_RESIZE_OFF);
		}		
		addRight(scrollPane);
		ValueHandle valueGetter = new TableGetter(rowSeparator, columnSeparator,table, escapeSequence);
		controlsMap.put(property,valueGetter);
		return valueGetter;
	}
	
	/**
	 * add a number property to the form
	 * @param label
	 * @param property
	 */
	public ValueHandle addNumberProperty(String label, String property,
			boolean editable, double min, double max, double step){
		JLabel labelComponent = makeLabel(label);
		SpinnerNumberModel model = new SpinnerNumberModel(min,min, max, step); 
		JSpinner spinner = new JSpinner(model);					
		numberProperties.add(spinner);
		spinner.setEnabled(editable);
		labelComponent.setLabelFor(spinner);
		addRight(spinner);
		SpinnerValueGetter valueHandle = new SpinnerValueGetter(spinner);
		controlsMap.put(""+property, valueHandle);
		return valueHandle;
	}
	
	/**
	 * add a number property to the form
	 * @param label
	 * @param property
	 * @param required
	 */
	public ValueHandle addDateProperty(String label, String property,
			boolean editable, String dateFormat, boolean required){
		JLabel labelComponent = makeLabel(label);
		DateChooser chooser = new DateChooser(dateFormat);
		dateProperties.add(chooser);
		chooser.setEnabled(editable);
		labelComponent.setLabelFor(chooser);
		addRight(chooser);
		DateChooserGetter valueHandle =  new DateChooserGetter(chooser);
		controlsMap.put(property, valueHandle);
		if (required) {
			requiredMap.put(property, valueHandle);
		}
		return valueHandle;
	}
	
	/**
	 * add a text property to the form
	 * @param label
	 * @param property
	 */
	public ValueHandle addListProperty(String label, String property,
			boolean editable, List values){
		JLabel labelComponent = makeLabel(label);
		SpinnerListModel model = new SpinnerListModel(values); 
		JSpinner spinner = new JSpinner(model);					
		numberProperties.add(spinner);
		spinner.setEnabled(editable);
		labelComponent.setLabelFor(spinner);
		addRight(spinner);
		SpinnerValueGetter valueHandle = new SpinnerValueGetter(spinner);
		controlsMap.put(""+property, valueHandle);
		return valueHandle;
	}
	
	
	/**
	 * add a file section property
	 */
	public ValueHandle addFileSelectionProperty(String label, String property, boolean editable, int columns, boolean directoryChooser, boolean required) {
		JLabel labelComponent = makeLabel(label);
		FileChooser chooser = new FileChooser(columns, directoryChooser);
		addRight(chooser);
		fileChoosers.add(chooser);		
		labelComponent.setLabelFor(chooser);
		FileChooserGetter valueHandle = new FileChooserGetter(chooser);
		controlsMap.put(property, valueHandle);
		if (required){
			requiredMap.put(property, valueHandle);
		}
		return valueHandle;
	}
	
	/**
	 * add a text property to the form
	 * @param label
	 * @param property
	 * @param required
	 */
	public ValueHandle addMultiLineTextProperty(String label, String property, 
			int columns, int rows, boolean editable, boolean required){
		JLabel labelComponent = makeLabel(label);
		JTextArea textArea = new JTextArea(rows,columns);		
		textArea.setEditable(editable);		
		labelComponent.setLabelFor(textArea);
		JScrollPane scrollPane = new JScrollPane(textArea);
		multiLineTextProperties.add(textArea); 
		scrollPanes.add(scrollPane);
		addRight(scrollPane);
		TextValueGetter valueHandle = new TextValueGetter(textArea);
		controlsMap.put(property, valueHandle);		
		if (required){
			requiredMap.put(property, valueHandle);	
		}
		return valueHandle;
	}
	
	
	/**
	 * add a text property to the form
	 * @param label
	 * @param property
	 */
	public ValueHandle addBooleanProperty(String label, String property, boolean editable){
		JLabel labelComponent = makeLabel(label);
		JCheckBox checkBox = new JCheckBox();
		checkBox.setEnabled(editable);
		labelComponent.setLabelFor(checkBox);
		addRight(checkBox);
		checkBoxes.add(checkBox);
		CheckValueGetter valueHandle = new CheckValueGetter(checkBox);
		controlsMap.put(property, valueHandle);		
		return valueHandle;
	}
	
	/**
	 * add a link
	 */
	public void addLink(String label, String target){ 
		final JButton link = new JButton(label);
		final String linkLocation = target;
		JPanel linkPanel = new JPanel();
		linkPanel.setBorder(linkBorder());
		linkPanel.setLayout(new BorderLayout());
		linkPanel.add(link, BorderLayout.CENTER);
		linkPanel.setOpaque(false);
		links.add(link);		
		addLinkToLayout(linkPanel);
		link.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
					control.executeLink(linkLocation);
			}
		});		
		setMnemonics(link, label);
	}

	/**
	 * add a text property to the form
	 * @param label
	 * @param property
	 */
	public JTextArea makeAddLabel(String text, int columns, int rows){
		JTextArea label = new JTextArea(text);
		JLabel getFont = new JLabel("");		
		if ((columns>0)&&(rows>0)) {
			label = new JTextArea(rows, columns);
		} else {
			label = new JTextArea();
		}
		label.setFont(getFont.getFont());
		label.setText(text);
		label.setEditable(false);
		label.setLineWrap(true);
		label.setOpaque(false);		
		label.setWrapStyleWord(true);
		if ((columns>0)&&(rows>0)) {
			addCenteredNoFill(label);
		} else {
			addCentered(label);
		}		
		messages.add(label);
		return label;		
	}
	
	
	
	/**
	 * add a text property to the form
	 * @param label
	 * @param property
	 */
	public JTextArea addLabel(String text, int columns, int rows){
		JTextArea aLabel = makeAddLabel(text, columns, rows);		
		return aLabel;		
	}

	/**
	 * add a text property to the form
	 * @param label
	 * @param property
	 */
	public ValueIndiceHandle addConstrainedProperty(String label, String property, 
			String[] comboValues,
			boolean editable){
		JLabel labelComponent = makeLabel(label);
		JComboBox comboBox = new JComboBox(comboValues);		
		comboBox.setEnabled(editable);
		selectionProperties.add(comboBox);
		labelComponent.setLabelFor(comboBox);
		addRight(comboBox);
		ComboIndiceGetter valueHandle = new ComboIndiceGetter(comboBox);
		controlsMap.put(property, valueHandle);
		return valueHandle;
	}

	/**
	 * add a constrained radio property
	 */
	public ValueIndiceHandle addConstrainedRadioProperty(String label, 
			String property, 
			String[] comboValues, 
			boolean editable) {
		JLabel labelComponent = makeLabel(label);
		RadioGroupBox radioBox = new RadioGroupBox(comboValues);		
		radioBox.setEnabled(editable);		
		radioButtons.add(radioBox);
		labelComponent.setLabelFor(radioBox);
		addRight(radioBox);
		RadioGetter valueHandle =  new RadioGetter(radioBox);
		controlsMap.put(property, valueHandle);
		return valueHandle;
	}	
	
	/**
	 * add a constrained radio property
	 */
	public ValueHandle addMultiCheckProperty(String label, String property, 
			String[] comboValues, String separator,String escapeSequence, boolean editable) {
		JLabel labelComponent = makeLabel(label);
		CheckGroupBox checkGroupBox = new CheckGroupBox(comboValues, separator,escapeSequence);		
		checkGroupBox.setEnabled(editable);
		checkBoxes.add(checkGroupBox);
		labelComponent.setLabelFor(checkGroupBox);
		addRight(checkGroupBox);
		ValueHandle valueHandle =  new MultiCheckGetter(checkGroupBox);
		controlsMap.put(property, valueHandle);
		return valueHandle;
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
	
	/**
	 * add a separator
	 */
	public void addSeparator() {
		addCentered(new JSeparator(JSeparator.HORIZONTAL));
	}
	/**
	 * add a link bar
	 */
	public void addLinkBar(LinkBar linkBar) {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		BoxLayout layout = new BoxLayout(panel, BoxLayout.X_AXIS);		
		List linkBarLinks = linkBar.getLinks();
		for (Iterator iter = linkBarLinks.iterator(); iter.hasNext();) {
			final Link link = (Link) iter.next();			
			JButton button = new JButton(link.getLabel());			
			links.add(button);
			panel.add(button);
			panel.add(Box.createHorizontalStrut(10));			
			button.addActionListener(this);
			button.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
						control.executeTeleport(link.getTarget());
				}
			});
			setMnemonics(button, link.getLabel());
		}
		addCentered(panel);
	}

	/**
	 * add a cancel button
	 */
	public void addCancel(String label) {
		if (cancel==null) {
			cancel = new JButton(label);
			cancel.setActionCommand(label);
			cancelMessage = label;
			cancel.addActionListener(this);
			setMnemonics(cancel, cancel.getText());
			buttonInnerPanel.add(Box.createHorizontalStrut(20));
			buttonInnerPanel.add(cancel);
			buttons.add(cancel);
		}
	}

	/**
	 * Add a tab to the tabbedpane
	 */
	public void addTab(String label) {		
		GridBagLayout aLayout = new GridBagLayout();
		JPanel panel  = new JPanel();	
		panel.setBorder(BorderFactory.createEmptyBorder(15,5,15,5));
		panel.setLayout(aLayout);
		tabbedPane.addTab(label, panel);
		panels.add(panel);
		currentPanel = panel;
		layout = aLayout;
	}
}
