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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 * @author René Ghosh
 * 4 mars 2005
 */
public class FileChooser extends JPanel implements ActionListener{
	private JTextField textField;
	private JButton button;
	private boolean directoryChooser = false;
	
	/**
	 * Constructor
	 */
	public FileChooser(int columns, boolean directoryChooser){		
		textField = new JTextField(columns);
		button = new JButton("...");
		button.addActionListener(this);
		add(textField, BorderLayout.CENTER);
		add(button, BorderLayout.EAST);
		this.directoryChooser=directoryChooser;
	}

	public void requestFocus() {
		textField.requestFocus();
	}

	/**
	 * Process action events
	 */
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source==button) {
			JFileChooser chooser = new JFileChooser();
			if (directoryChooser) {
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			}
			chooser.setCurrentDirectory(new File(textField.getText()));
			int choseSomething =  chooser.showOpenDialog(this);
			if (choseSomething==JFileChooser.APPROVE_OPTION) {
				textField.setText(chooser.getSelectedFile().getAbsolutePath());
			}
			textField.requestFocus();
		}
	}
	
	/**
	 * set the background
	 */
	public void setBackground(Color bg) {
		if (textField!=null) {
			textField.setBackground(bg);
		}
	}
	
	/**
	 * set the border
	 */
	public void setBorder(Border border) {
		if (textField!=null) {
			textField.setBorder(border);
		}
	}
	
	/**
	 * set the foreground color
	 */
	public void setForeground(Color fg) {
		if (textField!=null) {
			textField.setForeground(fg);
		}
	}
	
	/**
	 * set the text field as enabled or not
	 */
	public void setEnabled(boolean enabled) {		
		textField.setEnabled(enabled);
		button.setEnabled(enabled);
	}

	
	/**
	 * set the font
	 */
	public void setFont(Font font) {
		if (textField!=null) {
			textField.setFont(font);
		}
	}
	
	
	/**
	 * test method
	 */
	public static void main(String[] args) {
		System.out.println(UIManager.getSystemLookAndFeelClassName());
		System.setProperty("swing.defaultlaf", UIManager.getSystemLookAndFeelClassName());
		JFrame frame = new JFrame();
		frame.getContentPane().add(new FileChooser(50, true), BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.show();		
	}

	/**
	 * set the textField value
	 */
	public void setText(String value) {
		textField.setText(value);
	}

	/**
	 * get the text from the textField
	 */
	public String getText() {
		return textField.getText();
	}

}
