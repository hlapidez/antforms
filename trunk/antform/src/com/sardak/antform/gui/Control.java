package com.sardak.antform.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.sardak.antform.AntCallBack;

/**
 * Frame for holding the user form
 * @author René Ghosh
 */
public class Control {
	private static final boolean TESTMODE = false;
	private static final boolean VERBOSE = false;
	private JFrame frame;
	private JTabbedPane tabbedPane;	
	private Properties properties = new Properties();
	private AntCallBack antCallBack;
	private ControlPanel panel = new ControlPanel(this);
	private int width=-1, height= -1;
	
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
	public Control(final AntCallBack antCallBack, String title){
		this.antCallBack=antCallBack;
		frame = new JFrame(title){
			public void dispose() {
				if (VERBOSE) {
					System.out.println("closing...");
				}
				super.dispose();
				if (antCallBack!=null) {
					antCallBack.callback(null);
				}
			}
		};		
		panel.setTitle(title);		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		if (TESTMODE) {
			test(panel);
		}
		frame.getContentPane().add(panel);				
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
		SwingUtilities.updateComponentTreeUI(frame);
		
	}
	/**
	 * set the frame title
	 * @param title
	 */
	public void setTitle(String title){
		frame.setTitle(title);
	}
	
	/**
	 * pack the frame
	 *
	 */
	public void pack(){
		frame.pack();
	}
	/**
	 * show the frame
	 *
	 */
	public void show(){
		//center the frame
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();		
		pack();
		if (width!=-1) {
			frame.setSize(width, frame.getHeight());
		}
		if (height!=-1) {
			frame.setSize(frame.getWidth(), height);
		}
		frame.setLocation(
				(int) (d.getWidth()/2.0-frame.getWidth()/2.0),
				(int) (d.getHeight()/2.0-frame.getHeight()/2.0)
				);
		frame.show();
	}
	/**
	 * test the control panel interface
	 * @param panel
	 */
	private static void test(ControlPanel panel) {
		panel.addTextProperty("Login: ", "login",40, false, true);
		panel.addConstrainedProperty("Login Type: ", "type", new String[] {"remote", "telnet", "ssh"}, false);
		panel.addMultiLineTextProperty("Description: ", "description", 40,5, false);
		panel.addBooleanProperty("Remember me: ", "remember", false);
		Properties props = new Properties();
		props.setProperty("login", "<LOGIN>");
		props.setProperty("type", "ssh");
		props.setProperty("description", "<DESCRIPTION>");
		props.setProperty("remember", "true");
		panel.setProperties(props);			
		panel.show();
	}
	
	/**
	 * Close the control panel and store properties
	 * @param properties
	 */
	public void close(Properties properties) {
		if (TESTMODE) {
			properties.list(System.out);
		}
		this.properties = properties;
		frame.dispose();
	}
	
	/**
	 * execute a target link
	 */
	public void executeLink(String target){
		antCallBack.callback(target);		
		frame.dispose();
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
	public static void main(String[] args) {
		try {
			System.out.println(UIManager.getSystemLookAndFeelClassName());
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			Control c = new Control(null, "title");			
			ControlPanel panel = new ControlPanel(c);
			c.getPanel().addLabel("this is a label of text");
			test(panel);
			c.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
