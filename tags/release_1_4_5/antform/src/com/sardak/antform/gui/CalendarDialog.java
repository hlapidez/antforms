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
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author René Ghosh
 * 1 mars 2005
 */
public class CalendarDialog extends JDialog implements ActionListener, MouseListener, ChangeListener{
	private static final SimpleDateFormat monthStringFormatter = new SimpleDateFormat("MMMM"),
		monthFormatter = new SimpleDateFormat("MM"),		
		weekDayStringFormatter = new SimpleDateFormat("EEEE");
	
	private static final String[] monthNames = new String[12];
	private static final String[] weekDayNames = new String[7];
	static {		
			try {
				for (int i = 1; i <= 12; i++) {
					monthNames[i-1] = monthStringFormatter.format(monthFormatter.parse(""+i));
				}
				GregorianCalendar testCalendar  = new GregorianCalendar();
				testCalendar.set(Calendar.DAY_OF_WEEK, 	testCalendar.getFirstDayOfWeek() );
				for (int i=1;i<=7;i++) {
					weekDayNames[i-1]=weekDayStringFormatter.format(testCalendar.getTime());
					testCalendar.roll(Calendar.DAY_OF_WEEK, 1);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		
	}
	private GregorianCalendar calendar;
	private JComboBox monthCombo;
	private JSpinner yearSpinner;
	private JButton okButton;
	private JPanel middlePanel;
	private JLabel[] numbers;
	
	/**
	 * Default constructor
	 * Constructor
	 */
	public CalendarDialog(Frame frame){
		super(frame, true);
		common();
	}
	
	/**
	 * Default constructor
	 * Constructor
	 */
	private CalendarDialog(){
		super();
		common();
	}
	
	/**
	 * common constructor instructions
	 */
	private void common(){
		calendar = new GregorianCalendar();
		getContentPane().setLayout(new BorderLayout());		
		okButton = new JButton("OK");
		okButton.addActionListener(this);
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.X_AXIS));
		southPanel.add(Box.createHorizontalGlue());
		southPanel.add(okButton);
		southPanel.setBorder(bigEtched());
		middlePanel = new JPanel();
		middlePanel.setLayout(new GridLayout(7,7));
		middlePanel.setBorder(bigEtched());
		numbers = new JLabel[42];
		for (int i=0;i<7;i++) {
			JLabel letterLabel = new JLabel((weekDayNames[i].charAt(0)+"").toUpperCase());
			letterLabel.setHorizontalAlignment(JLabel.CENTER);
			middlePanel.add(letterLabel);
		}
		for (int i = 0; i < 42; i++) {
			numbers[i] = new JLabel();
			numbers[i].setOpaque(true);
			unselect(i);
			numbers[i].setHorizontalAlignment(JLabel.CENTER);			
			numbers[i].addMouseListener(this);
			middlePanel.add(numbers[i]);			
		}
		getContentPane().add(middlePanel, BorderLayout.CENTER);
		getContentPane().add(southPanel, BorderLayout.SOUTH);
		JPanel northPanel = new JPanel();
//		northPanel.setBorder(bigEtched());		
		northPanel.setLayout(new BorderLayout());		
		monthCombo = new JComboBox(monthNames);
		monthCombo.addActionListener(this);
		northPanel.add(monthCombo, BorderLayout.CENTER);
		yearSpinner = new JSpinner(new SpinnerNumberModel());
		yearSpinner.addChangeListener(this);
		northPanel.add(yearSpinner, BorderLayout.EAST);					
		getContentPane().add(northPanel, BorderLayout.NORTH);		
		pack();
	}
	
	/**
	 * show the dialog
	 */
	public void show() {		
		super.show();		
	}
	
	/**
	 * Etched border with insets 
	 */
	private static Border bigEtched() {
		Border eBorder = BorderFactory.createEmptyBorder(2,2,2,2);
		Border bBorder = BorderFactory.createEtchedBorder();
		return BorderFactory.createCompoundBorder(bBorder, eBorder);		
	}	
	/**
	 * Process action events
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==monthCombo){
			calendar.set(Calendar.MONTH, monthCombo.getSelectedIndex());			
			setDate(calendar.getTime());		
		} else if (e.getSource()==okButton){					
			dispose();			
		}
	}
	/**
	 * Proces events on the year spinner
	 */
	public void stateChanged(ChangeEvent e) {
		if (e.getSource()==yearSpinner){
			calendar.set(Calendar.YEAR, Integer.parseInt(yearSpinner.getValue()+""));			
			setDate(calendar.getTime());
		}
	}
	
	/**
	 * get the calendar date
	 */
	public Date getDate(){
		return calendar.getTime();
	}
	
	/**
	 * Set the calendar date
	 */
	public void setDate(Date date){
		calendar.setTime(date);		
		monthCombo.setSelectedIndex(calendar.get(Calendar.MONTH));
		yearSpinner.setValue(new Integer(calendar.get(Calendar.YEAR)));
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		int firstDayInWeek = calendar.getFirstDayOfWeek();
		GregorianCalendar testCalendar = new GregorianCalendar();
		testCalendar.setTime(calendar.getTime());
		testCalendar.set(Calendar.DAY_OF_MONTH, 1);
		int dayStart = testCalendar.get(Calendar.DAY_OF_WEEK);
		for (int i=0;i<42;i++) {
			numbers[i].setText("");
			unselect(i);
		}	
		int offSet = 0;
		if (dayStart-firstDayInWeek<0) {
			offSet = 7;
		}
		for (int i=offSet+dayStart-firstDayInWeek;i<calendar.getActualMaximum(Calendar.DAY_OF_MONTH)+dayStart-firstDayInWeek+offSet;i++) {						
			numbers[i].setText(""+(i-dayStart+1+firstDayInWeek-offSet));
			unselect(i);
		}			
		select(dayOfMonth-(firstDayInWeek+1)+dayStart);		
	}

	/**
	 * unselect a label
	 */
	private void unselect(int i) {
		if ((i>=0)&&(i<42)) {
			numbers[i].setForeground(Color.BLACK);
			numbers[i].setBackground(Color.WHITE);
		}
	}
	
	/**
	 * setlect a label
	 */
	private void select(int i) {
		if ((i>=0)&&(i<42)) { 
			numbers[i].setBackground(Color.BLACK);
			numbers[i].setForeground(Color.WHITE);
		}
	}
	
	/**
	 * Process clicks on a day of the month
	 */
	public void mouseClicked(MouseEvent e) {
		Object source = e.getSource();		
		for (int i=0;i<42;i++) {
			if (source==numbers[i]){
				select(i);
				GregorianCalendar testCalendar = new GregorianCalendar();
				testCalendar.setTime(calendar.getTime());
				testCalendar.set(Calendar.DAY_OF_MONTH, 1);				
				int dayStart = testCalendar.get(Calendar.DAY_OF_WEEK);				
				int firstDayInWeek = testCalendar.getFirstDayOfWeek();
				int offSet = 0;
				if (dayStart-firstDayInWeek<0) {
					offSet = 7;
				}
				int newDay = i+1-(dayStart-firstDayInWeek)-offSet;
				calendar.set(Calendar.DAY_OF_MONTH, newDay);				
			} else {
				unselect(i);
			}
		}
	}
	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}
	public void mousePressed(MouseEvent e) {
		
	}
	public void mouseReleased(MouseEvent e) {
	}
	public static void main(String[] args) {
		new CalendarDialog(new JFrame());
	}
}
