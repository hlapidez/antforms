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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Panel that holds the user form and lays out labels and
 * input fields in two side-by-side columns
 * @author René Ghosh
 */
public class ControlPanel extends JPanel implements ActionListener{
	private GridBagLayout layout;
	private JPanel middlePanel;
	private Map controlsMap = new HashMap();
	private static final boolean VERBOSE = false;
	private Properties properties = new Properties();
	private Properties defaultProperties = new Properties();
	private String title, okMessage, resetMessage;
	private Control control;
	private JLabel topLabel;
	private JButton resetButton, okButton;
	private List usedLetters = new ArrayList();

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
	 * Process action events
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();	
		if (command.equals(resetMessage)){
			setProperties(defaultProperties);			
		} else if (command.equals(okMessage)){
			properties = new Properties();
			for (Iterator i=controlsMap.keySet().iterator();i.hasNext();) {
				String propertyName = (String) i.next();
				Component c = (Component) controlsMap.get(propertyName);			
				if (c instanceof JTextField) {
					JTextField textField = (JTextField) c;
					log(propertyName+": "+textField.getText());
					properties.setProperty(propertyName, textField.getText());
				} else if (c instanceof JComboBox) {
					JComboBox comboBox = (JComboBox) c;
					log(propertyName+": "+comboBox.getSelectedItem());
					properties.setProperty(propertyName, comboBox.getSelectedItem()+"");
				}  else if (c instanceof JTextArea) {
					JTextArea textArea = (JTextArea) c;
					log(propertyName+": "+textArea.getText());
					properties.setProperty(propertyName, textArea.getText());
				} else if (c instanceof JCheckBox) {
					JCheckBox checkBox = (JCheckBox) c;
					log(propertyName+": "+checkBox.isSelected());
					if (checkBox.isSelected()){
						properties.setProperty(propertyName, checkBox.isSelected()+"");
					} else {
						properties.remove(propertyName);
					}
				}				
			}
			control.close(properties);
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
		gbc.insets = new Insets(5,5,5,5);
		layout.setConstraints(component, gbc);
		middlePanel.add(component);
	}
	
	/**
	 * Construct a label and add it into the left column
	 */
	private JLabel makeLabel(String labelText){
		JLabel label = new JLabel(labelText);
		addLeft(label);		
		for (int i=0;i<labelText.length();i++){
			char toUse = labelText.charAt(i);
			if (usedLetters.contains((toUse+"").toUpperCase())){
				continue;
			}
			label.setDisplayedMnemonic(toUse);			
			usedLetters.add(""+toUse);
			break;
		}						
		return label;
	}

	/**
	 * add a component to the right side of the form
	 * @param component
	 */
	private final void addRight(Component component){
		GridBagConstraints gbc = new GridBagConstraints();		
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.gridwidth= GridBagConstraints.REMAINDER;
		gbc.insets = new Insets(5,5,5,5);
		layout.setConstraints(component, gbc);
		middlePanel.add(component);
	}	
	
	/**
	 * add a component to the right side of the form
	 * @param component
	 */
	private final void addCentered(Component component){
		GridBagConstraints gbc = new GridBagConstraints();		
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.HORIZONTAL;		
		gbc.weightx = 0.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets = new Insets(5,5,5,5);
		layout.setConstraints(component, gbc);
		middlePanel.add(component);
	}		
	/**
	 * Constructor	 
	 */
	public ControlPanel(Control control){
		this.control = control;	
		topLabel = new JLabel(title, JLabel.CENTER);
		Font font = topLabel.getFont();
		font = font.deriveFont((float) 16.0);		
		font = font.deriveFont(Font.BOLD);
		topLabel.setFont(font);
		topLabel.setBackground(Color.WHITE);
		topLabel.setOpaque(true);
		setLayout(new BorderLayout());
		add(topLabel, BorderLayout.NORTH);
		layout = new GridBagLayout();
		middlePanel = new JPanel();		
		add(middlePanel, BorderLayout.CENTER);
		middlePanel.setLayout(layout);		
	}
	
	/**
	 * add button controls to the panel
	 */
	public void addButtonControls(String okMessage, String resetMessage){		
		JPanel buttonPanel = new JPanel();
		add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		resetButton=new JButton(resetMessage);
		okButton=new JButton(okMessage);
		resetButton.addActionListener(this);
		okButton.addActionListener(this);		
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(resetButton);
		buttonPanel.add(Box.createHorizontalStrut(10));
		buttonPanel.add(okButton);		
		setOkMessage(okMessage);
		setResetMessage(resetMessage);
	}
	/**
	 * set the form title
	 * @param title
	 */
	public void setTitle(String title){
		topLabel.setText(title);
	}
	
	/**
	 * set the ok button message
	 * @param message
	 */
	public void setOkMessage(String message){
		okButton.setText(message);
		okMessage = message;
	}
	
	/**
	 * set the reset button message
	 */
	public void setResetMessage(String message){
		resetButton.setText(message);
		resetMessage = message;
	}
	
	/**
	 * add a text property to the form
	 * @param label
	 * @param property
	 */
	public void addTextProperty(String label, String property, int numberColumns,
			boolean editable, boolean isPassword){
		JLabel labelComponent = makeLabel(label);
		JTextField textField = null;
		if (!isPassword) {
			textField = new JTextField(numberColumns);
		} else {
			textField = new JPasswordField(numberColumns);
		}
		textField.setEnabled(editable);
		labelComponent.setLabelFor(textField);
		addRight(textField);
		controlsMap.put(property, textField);
	}
	
	/**
	 * add a text property to the form
	 * @param label
	 * @param property
	 */
	public void addMultiLineTextProperty(String label, String property, 
			int columns, int rows, boolean editable){
		JLabel labelComponent = makeLabel(label);
		JTextArea textArea = new JTextArea(rows,columns);
		textArea.setEnabled(editable);
		labelComponent.setLabelFor(textArea);
		addRight(new JScrollPane(textArea));
		controlsMap.put(property, textArea);		
	}
	
	
	/**
	 * add a text property to the form
	 * @param label
	 * @param property
	 */
	public void addBooleanProperty(String label, String property, boolean editable){
		JLabel labelComponent = makeLabel(label);
		JCheckBox checkBox = new JCheckBox();
		checkBox.setEnabled(editable);
		labelComponent.setLabelFor(checkBox);
		addRight(checkBox);
		controlsMap.put(property, checkBox);
	}
	
	/**
	 * add a link
	 */
	public void addLink(String label, String target){ 
		JButton button = new JButton(label);
		addCentered(button);
		final String linkLocation = target;
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				control.executeLink(linkLocation);
			}
		});
	}

	/**
	 * add a text property to the form
	 * @param label
	 * @param property
	 */
	public void addLabel(String text){
		JTextArea textArea = new JTextArea(text);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);		
		addCentered(textArea);		
	}
	/**
	 * add a text property to the form
	 * @param label
	 * @param property
	 */
	public void addConstrainedProperty(String label, String property, 
			String[] comboValues,
			boolean editable){
		JLabel labelComponent = makeLabel(label);
		JComboBox comboBox = new JComboBox(comboValues);
		comboBox.setEnabled(editable);
		labelComponent.setLabelFor(comboBox);
		addRight(comboBox);
		controlsMap.put(property, comboBox);
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
				Component c = (Component) controlsMap.get(property);
				if (c instanceof JTextField) {
					JTextField textField = (JTextField) c;		
					textField.setText(value);
				} else if (c instanceof JComboBox) {
					JComboBox comboBox = (JComboBox) c;
					comboBox.setSelectedItem(value);
				}  else if (c instanceof JTextArea) {
					JTextArea textArea = (JTextArea) c;
					textArea.setText(value);
				} else if (c instanceof JCheckBox) {
					JCheckBox checkBox = (JCheckBox) c;
					checkBox.setSelected(Boolean.valueOf(value).booleanValue());
				}
			}
			
		}
	}
}
