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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 * @author René Ghosh
 * 1 mars 2005
 */
public class DateChooser extends JPanel implements ActionListener{
	private SimpleDateFormat formatter;
	private Date date = new Date();	
	private JFormattedTextField textField;
	private JButton button;	
	
	/**
	 * set the text field as enabled or not
	 */
	public void setEnabled(boolean enabled) {		
		textField.setEnabled(enabled);
		button.setEnabled(enabled);
	}
	
	public void setTooltipText(String tooltip) {
		super.setToolTipText(tooltip);
		button.setToolTipText(tooltip);
		textField.setToolTipText(tooltip);
	}
	
	/**
	 * Constructor
	 */
	public DateChooser(String format){
		super();		
		textField = new JFormattedTextField(new SimpleDateFormat(format));		
		button = new JButton("...");
		button.addActionListener(this);
		button.setPreferredSize(new Dimension(button.getPreferredSize().width, textField.getPreferredSize().height));
		setLayout(new BorderLayout());
		add(textField, BorderLayout.CENTER);
		add(button, BorderLayout.EAST);
		formatter = new SimpleDateFormat(format);
		textField.setText(formatter.format(date));				
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
	 * set the background
	 */
	public void setBackground(Color bg) {
		if (textField!=null) {
			textField.setBackground(bg);
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
	 * set the font
	 */
	public void setFont(Font font) {
		if (textField!=null) {
			textField.setFont(font);
		}
	}
	/**
	 * process button clicks
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==button) {
			Date newDate = new Date();
			try {
				newDate = formatter.parse(textField.getText());
			} catch (ParseException pe) {
				
			}
			CalendarDialog dialog = new CalendarDialog(JOptionPane.getRootFrame());	
			dialog.setDate(newDate);
			dialog.setLocation(button.getLocationOnScreen());
			dialog.show();						
			textField.setText(formatter.format(dialog.getDate()));
		}				
	}
	
	/**
	 * set the text field
	 */
	public void setText(String text){
		try {
			if (text == null) {
				text = formatter.format(new Date());
			}
			Date date = formatter.parse(text);
			if (date!=null) {
				textField.setText(text);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * test function
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		frame.getContentPane().add(new DateChooser("dd/MM/yyyy"), BorderLayout.CENTER);
		SpinnerDateModel  model = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
		
		JSpinner spinner = new JSpinner(model);
		
		frame.getContentPane().add(spinner, BorderLayout.SOUTH);
		frame.show();
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


	/**
	 * return the text value
	 */
	public String getText() {
		return textField.getText();
	}

	public void requestFocus() {
		textField.requestFocus();
	}
}
