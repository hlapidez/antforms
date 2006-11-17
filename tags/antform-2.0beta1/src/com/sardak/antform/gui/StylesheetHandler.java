package com.sardak.antform.gui;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.sardak.antform.style.HexConverter;
import com.sardak.antform.util.StyleUtil;

public class StylesheetHandler {
	private List textProperties,
		selectionProperties, numberProperties, links,
		multiLineTextProperties, buttons, dateProperties,
		labels, checkBoxes, messages, scrollPanes,
		fileChoosers, radioButtons, panels;	

	public StylesheetHandler() {
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
	}
	
	public void addLabel(JLabel label) {
	    labels.add(label);
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
	
	public void addSpinner(JComponent spinner) {
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
	
	public void addPanel(JPanel panel) {
	    panels.add(panel);
	}
	
	public void apply(String stylesheetFileName, ControlPanel controlPanel) throws FileNotFoundException, IOException {
		Properties props = new Properties();
		props.load(new FileInputStream(new File(stylesheetFileName)));
		Color background = HexConverter.translate(props.getProperty("background.color"), controlPanel.getCurrentPanel().getBackground());
		if (controlPanel.getSouthPanel() != null) {
			controlPanel.getSouthPanel().setBackground(background);
		}
		for (Iterator iter = panels.iterator(); iter.hasNext();) {
			JPanel aPanel  = (JPanel) iter.next();
			aPanel.setBackground(background);
		}
		controlPanel.getCurrentPanel().setBackground(background);
		controlPanel.setBackground(background);
//		for (Iterator iter = labels.iterator(); iter.hasNext();) {
//			JLabel label  = (JLabel) iter.next();
//			Font currentFont = label.getFont();
//			int fontSize = props.getProperty("font.size") == null ? currentFont.getSize() : Integer.parseInt(props.getProperty("font.size"));
//			Font labelFont = new Font(props.getProperty("font.family", currentFont.getFamily()), Font.PLAIN, fontSize);
//			label.setFont(labelFont);
//		}
		Set buttonBars = new HashSet();
		buttonBars.add(controlPanel.getButtonPanel());
		StyleUtil.styleComponents("buttonBar", props, buttonBars);
		List banners = new ArrayList();
		banners.add(controlPanel.getOverPanel());		
		StyleUtil.styleComponents("banner", props, banners);
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
}
