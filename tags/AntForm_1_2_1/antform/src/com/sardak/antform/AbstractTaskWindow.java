package com.sardak.antform;

import javax.swing.UIManager;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.sardak.antform.gui.Control;

/**
 * @author René Ghosh
 * 12 janv. 2005
 */
public abstract class AbstractTaskWindow extends Task implements AntCallBack{
	private String title = "Ant Controls";
	private int height=-1, width=-1; 
	protected Control control;
	protected boolean needFail = false;
	protected String lookAndFeel=null;
	protected volatile boolean quit = false;	
	
	
	/**
	 * get window height
	 */
	public int getHeight() {
		return height;
	}
	/**
	 * set window height
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	/**
	 * get window width
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * set window width
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	/**
	 * check that the task can continue and set the look and feel
	 */
	public void execute() throws BuildException {
		control.setWidth(width);
		control.setHeight(height);
		if (needFail) {
			throw new BuildException("certain properties where not correctly set.");
		}
		if (lookAndFeel == null) {
			lookAndFeel = UIManager.getSystemLookAndFeelClassName();			
		}
		control.updateLookAndFeel(lookAndFeel);						
		control.show();
		control.setTitle(getTitle());
		control.getPanel().setTitle(getTitle());
		quit = false;	
	}
	
	/**
	 * @return lookAndFeel.
	 */
	public String getLookAndFeel() {
		return lookAndFeel;
	}
	/**
	 * @param lookAndFeel.
	 */
	public void setLookAndFeel(String lookAndFeel) {
		this.lookAndFeel = lookAndFeel;
	}
	
	/**
	 * wait for user validation of the form
	 */
	public void waitForValidation(){
		while (!quit) {
			//do nothing...
		}		
	}
	/**
	 * @see org.apache.tools.ant.Task#init()
	 */
	public void init() throws BuildException {
		control = new Control(this, title);				
	}
	/**
	 * @return title.
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
}
