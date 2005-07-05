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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
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
	private HashSet usedLetters;
	private int width=-1, height= -1;
	private String title, image;
	private JScrollPane scrollPane;
	private boolean firstShow = true;
	private List menuItems;
		
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
	public Control(final CallBack antCallBack, String title, String image, boolean tabbed){
		init(antCallBack, title, image, tabbed);
	}
	
	/**
	 * Manually set the callback method, title and image
	 */
	public void init(final CallBack antCallBack, String title, String image,
			boolean tabbed){
		this.callBack=antCallBack;
		if (dialog == null) {
			dialog = new CallBackDialog();		
			dialog.setCallBack(antCallBack);
			dialog.setTitle(title);
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
		usedLetters = new HashSet();
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
	 * @param properties
	 */
	public void close(String message) {				
		dialog.dispose(message);
	}
	
	/**
	 * execute a target link
	 */
	public void executeLink(String target, boolean background){		
		if (background) {
			callBack.invokeTarget(target, background);
		} else {
			callBack.callbackCommand(target, background);
			dialog.dispose(true);
		}
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
	public void setProperties(Properties properties) {
		this.properties = properties;
		panel.setProperties(properties);
	}

	/**
	 * @param properties properties to set.
	 */
	public void setProperties(Hashtable properties) {
		Properties props = new Properties();
		for (Iterator i=properties.keySet().iterator();i.hasNext();){
			String aProperty = (String) i.next();
			String value= ""+properties.get(aProperty);			
			props.setProperty(aProperty, value);
		}
		setProperties(props);
	}
	
	/**
	 * add menu item to menu bar
	 */
	public void addMenuItems(AntMenuItem parentItem, JMenuItem parentMenuItem) {
		HashSet usedLetters = new HashSet(); 
		for (Iterator iter = parentItem.getSubProperties().iterator(); iter.hasNext();) {
			final AntMenuItem newItem = (AntMenuItem) iter.next();
			String name = newItem.getName();
			String sToUse = MnemonicsUtil.newMnemonic(name, usedLetters);					
			if (newItem.getSubProperties().size()>0) {
				JMenu newMenu = new JMenu(name);
				if (sToUse!=null) {
					newMenu.setMnemonic(sToUse.charAt(0));
				}				
				parentMenuItem.add(newMenu);
				addMenuItems(newItem, newMenu);
				menuItems.add(newMenu);
			} else {
				JMenuItem newMenuItem = new JMenuItem(name);			
				if (sToUse!=null) {
					newMenuItem.setMnemonic(sToUse.charAt(0));
				}				
				parentMenuItem.add(newMenuItem);
				menuItems.add(newMenuItem);
//				newMenuItem.addActionListener(this);
//				newMenuItem.setActionCommand(newItem.getTarget());
				newMenuItem.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
							executeLink(newItem.getTarget(), newItem.isBackground());
					}
				});		
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
	
	public void setMenuBar(JMenuBar menuBar) {
	    dialog.setJMenuBar(menuBar);
	}
}
