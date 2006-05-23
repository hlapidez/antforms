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
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.sardak.antform.types.AntMenuItem;
import com.sardak.antform.util.MnemonicsUtil;
import com.sardak.antform.util.StyleUtil;

/**
 * Frame for holding the user form
 * @author René Ghosh
 */
public class Control {
	private CallBackDialog dialog;
	private Properties properties = new Properties();
	private CallBack callBack;
	private ControlPanel panel;
	private int width=-1, height= -1;
	private String title, image;
	private JScrollPane scrollPane;
	private boolean firstShow = true;
	private List menuItems;
	private JMenuBar menuBar;
		
	/**
	 * set window width
	 */
	public void setWidth(int width){
		this.width = width;
	}
	
	/**
	 * set window width
	 */
	public void setHeight(int height){
		this.height = height;
	}
	
	/**
	 * @return panel.
	 */
	public ControlPanel getPanel() {
		return panel;
	}
	
	/**
	 * Constructor	 
	 */
	public Control(final CallBack antCallBack, String title, File iconFile, String image, boolean tabbed){
		init(antCallBack, title, iconFile, image, tabbed);
	}
	
	/**
	 * Manually set the callback method, title and image
	 */
	public void init(final CallBack antCallBack, String title, File iconFile, String image,
			boolean tabbed){
		this.callBack=antCallBack;
		if (dialog == null) {
			dialog = new CallBackDialog();		
			dialog.setTitle(title);
			dialog.setIcon(iconFile);
			this.title=title;
			this.image=image;					
			newPanel(tabbed);
			init();
			getPanel().init();
		}		
	}
	
	/**
	 * Initialize the control
	 */
	public void init() {
		menuItems = new ArrayList();
	}
	
	/**
	 * Create and setup a new Panel
	 */
	public void newPanel(boolean tabbed) {
		panel = new ControlPanel(this,tabbed);			
		JPanel container = new JPanel();
		container.setBorder(null);
		container.setLayout(new BorderLayout());
		container.setBackground(Color.WHITE);
		dialog.setContentPane(container);		
		if (title!=null) {
			panel.setTitle(title);
		}
		if (image!=null) {
			panel.setImage(image);
		}		
		scrollPane = new JScrollPane(panel);
		dialog.getContentPane().add(scrollPane, BorderLayout.CENTER);		
	}

	/**
	 * set the frame look and feel
	 * @param lookAndFeel
	 */
	public void updateLookAndFeel(String lookAndFeel) {
		try {
			UIManager.setLookAndFeel(lookAndFeel);			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		SwingUtilities.updateComponentTreeUI(dialog);
	}
	
	/**
	 * set the frame title
	 * @param title
	 */
	public void setTitle(String title){
		dialog.setTitle(title);
	}
	
	/**
	 * pack the frame
	 *
	 */
	public void pack(){
		dialog.pack();
	}
	
	/**
	 * show the frame
	 */
	public void show(){
		if (firstShow) {						
			pack();			
			if (width!=-1) {
				dialog.setSize(width, dialog.getHeight());				
			}
			if (height!=-1) {
				dialog.setSize(dialog.getWidth(), height);				
			}	
			center();
			if ((height==-1)&&(width==-1)) {
				pack();
				pack();
			} else {
				
			}			
			firstShow = false;			
			dialog.show();			
		} else {
			dialog.show();
		}					
	}
	
	/**
	 * Center the dialog
	 */
	private void center() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension size = dialog.getSize();
		dialog.setLocation(d.width/2-size.width/2,d.height/2-size.height/2);
	}

	/**
	 * Close the control panel and store properties
	 * @param properties
	 */
	public void close(Properties properties, String message) {		
		this.properties = properties;
		close(message);
	}
	
	/**
	 * Close the control panel and store properties
	 */
	public void close(String message) {				
		dialog.dispose(message);
	}
	
	/**
	 * @return the properties.
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * @param properties properties to set.
	 */
	public void initProperties(Properties properties) {
		this.properties = properties;
		panel.initWidgets(properties);
	}

	/**
	 * @param propertyNames properties to set.
	 */
	public void initProperties(Hashtable propertyNames) {
		Properties props = new Properties();
		for (Iterator i = propertyNames.keySet().iterator(); i.hasNext();){
			String aProperty = (String) i.next();
			String value= ""+propertyNames.get(aProperty);			
			props.setProperty(aProperty, value);
		}
		initProperties(props);
	}
	
	public JMenuBar getMenuBar() {
		if (menuBar == null) {
			menuBar = new JMenuBar();
		    dialog.setJMenuBar(menuBar);
		}
		return menuBar;
	}
	
	public void addAntMenuItem(AntMenuItem antMenuItem, JMenuItem parentMenu) {
		//create button
		JMenuItem item;
		if (antMenuItem.getSubMenuItems().size() == 0) {
			item = new JMenuItem();
		} else {
			item = new JMenu();
		}
		//setup button
		item.setText(antMenuItem.getName());
		String sToUse = MnemonicsUtil.newMnemonic(antMenuItem.getName(), panel.getUsedLetters());
		if (sToUse != null) {
			item.setMnemonic(sToUse.charAt(0));
		}
		//add to parent
		if (parentMenu == null) {
			getMenuBar().add(item);
			if (antMenuItem.getSubMenuItems().size() != 0) {
				addMenu((JMenu) item);
			}
		} else {
			parentMenu.add(item);
		}
		// set behaviour or add children
		if (antMenuItem.getSubMenuItems().size() == 0) { // action menu
			antMenuItem.setComponent(item);
			antMenuItem.register(callBack.getActionRegistry());
		} else {
			for (int i = 0 ; i < antMenuItem.getSubMenuItems().size() ; i++) {
				AntMenuItem childItem = (AntMenuItem) antMenuItem.getSubMenuItems().get(i);
				addAntMenuItem(childItem, item);
			}
		}
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
		StyleUtil.styleComponents("menu", props, menuItems);		
	}

	/**
	 * set a property to "false" only if it's set
	 */
	public void setFalse(String propertyName) {
		callBack.setFalse(propertyName);
	}
	
	public void addMenu(JMenu menu) {
	    menuItems.add(menu);
	}
}
