/***************************************************************************\*
 *                                                                            *
 *    AntForm form-based interaction for Ant scripts                          *
 *    Copyright (C) 2005 Ren� Ghosh                                           *
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
package com.sardak.antform;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;

import com.sardak.antform.gui.CallBack;
import com.sardak.antform.gui.Control;
import com.sardak.antform.interfaces.ActionComponent;
import com.sardak.antform.interfaces.ActionListenerComponent;
import com.sardak.antform.interfaces.DummyComponent;
import com.sardak.antform.interfaces.Focusable;
import com.sardak.antform.interfaces.Requirable;
import com.sardak.antform.types.AntMenuItem;
import com.sardak.antform.types.BaseType;
import com.sardak.antform.types.Button;
import com.sardak.antform.types.ButtonBar;
import com.sardak.antform.types.Label;
import com.sardak.antform.types.Tab;
import com.sardak.antform.util.ActionRegistry;
import com.sardak.antform.util.FocusedComponent;

/**
 * @author Ren� Ghosh 12 janv. 2005
 */
public abstract class AbstractTaskWindow extends Task implements CallBack {
	protected String title = null;
	private File stylesheet = null;
	protected File image = null;
	private File iconFile = null;
	private int height = -1, width = -1;
	protected Control control;
	protected boolean needFail = false;
	protected String lookAndFeel = null;
	protected List widgets;
	protected List displayedWidgets;
	protected boolean dynamic = false;
	protected boolean tabbed = false;
	private ActionRegistry actionRegistry;
	private boolean loop = false;
	private ActionComponent actionSource = null;
	private boolean showWhenEmpty = false;

	/**
	 * get image URL
	 */
	public File getImage() {
		return image;
	}

	/**
	 * set image URL
	 */
	public void setImage(File image) {
		this.image = image;
	}

	/**
	 * set a property to false if it has been set
	 */
	public void setFalse(String propertyName) {
		if (getProject().getProperties().containsKey(propertyName)) {
			getProject().setProperty(propertyName, Boolean.FALSE + "");
		}
	}

	/**
	 * construct the gui widgets
	 */
	protected void build() {
		displayedWidgets = new ArrayList();
		for (int i = 0; i < widgets.size() && !needFail; i++) {
			BaseType o = (BaseType) widgets.get(i);
			if (o.validate(this)) {
				if (o.shouldBeDisplayed(getProject())) {
					if (o instanceof DummyComponent) {
						o = ((DummyComponent) o).getRealType();
						widgets.set(i, o);
					}
					o.addToControlPanel(control.getPanel());
					displayedWidgets.add(o);
				}
				if (o.getIf() != null || o.getUnless() != null) {
					dynamic = true;
				}
			} else {
				needFail = true;
			}
		}
		// for (Iterator iter = widgets.iterator(); iter.hasNext();) {
		// BaseType o = (BaseType) iter.next();
		// if (o.validate(this)) {
		// if (o.shouldBeDisplayed(getProject())) {
		// o.addToControlPanel(control.getPanel());
		// displayedWidgets.add(o);
		// }
		// if (o.getIf() != null || o.getUnless() != null) {
		// dynamic = true;
		// }
		// } else {
		// needFail = true;
		// }
		// }
	}

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

 	public void close() {
 		// make copy and test;
		// otherwise need to synchronize access
 		Control controlCopy= control;
 		if(controlCopy!=null) {
 			controlCopy.close();
 		}
	}

	/**
	 * add a menu
	 */
	public void addConfiguredAntMenuItem(AntMenuItem menuItem) {
		if (menuItem.validate(this)) {
			widgets.add(menuItem);
		} else {
			needFail = true;
		}
	}

	/**
	 * add a button bar
	 */
	public void addConfiguredButtonBar(ButtonBar buttonBar) {
		widgets.add(buttonBar);
		buttonBar.register(getActionRegistry());
	}

	/**
	 * add a button
	 */
	public void addConfiguredButton(Button button) {
		ButtonBar buttonBar = new ButtonBar();
		buttonBar.setProject(getProject());
		buttonBar.addConfiguredButton(button);
		buttonBar.setMargins(0, 100, 0, 100);
		addConfiguredButtonBar(buttonBar);
	}

	/**
	 * add a label
	 */
	public void addConfiguredLabel(Label label) {
		widgets.add(label);
	}

	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	/**
	 * set preliminary gui operations, such as Look & Feel
	 */
	public void preliminaries() {
		if (control == null) {
			control = new Control(this, title, iconFile, image, tabbed);
			if (lookAndFeel != null) {
				control.updateLookAndFeel(lookAndFeel);
			}
		}
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
		control.setTitle(getTitle());
		if (stylesheet != null) {
			try {
				control.setStyleSheet(stylesheet);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		control.setFocusedComponent(getFocusedComponent());
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
	 * Find a target amid the projet targets
	 */
	public Target findTargetByName(String target) {
		Target targetToFind = null;
		Hashtable targets = getProject().getTargets();
		for (Iterator i = targets.keySet().iterator(); i.hasNext();) {
			String targetName = (String) i.next();
			Target aTarget = (Target) targets.get(targetName);
			if (aTarget.getName().equals(target)) {
				targetToFind = aTarget;
			}
		}
		return targetToFind;
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

	/**
	 * get the styelsheet reference
	 */
	public File getStylesheet() {
		return stylesheet;
	}

	/**
	 * set the styelsheet reference
	 */
	public void setStylesheet(File stylesheet) {
		this.stylesheet = stylesheet;
	}

	public File getIcon() {
		return iconFile;
	}

	public void setIcon(File icon) {
		this.iconFile = icon;
	}

	/**
	 * @return the showWhenEmpty
	 */
	public boolean showWhenEmpty() {
		return showWhenEmpty;
	}

	/**
	 * @param showWhenEmpty
	 *            the showWhenEmpty to set
	 */
	public void setShowWhenEmpty(boolean showWhenEmpty) {
		this.showWhenEmpty = showWhenEmpty;
	}

	/*
	 * Returns true if the form should be shown Must be called after build() so
	 * that displayedWidgets is set
	 */
	protected boolean shouldShow() {
		return showWhenEmpty() || !displayedWidgets.isEmpty();
	}

	public ActionComponent getActionSource() {
		return actionSource;
	}

	public void setActionSource(ActionComponent actionSource) {
		this.actionSource = actionSource;
	}

	public ActionRegistry getActionRegistry() {
		if (actionRegistry == null) {
			actionRegistry = new ActionRegistry(this);
		}
		return actionRegistry;
	}

	public boolean requiredStatusOk() {
		Iterator iter = displayedWidgets.iterator();
		while (iter.hasNext()) {
			Object o = iter.next();
			if (o instanceof Requirable && !((Requirable) o).requiredStatusOk()) {
				return false;
			}
		}
		return true;
	}

	public void ok() {
		Iterator iter = displayedWidgets.iterator();
		while (iter.hasNext()) {
			Object o = iter.next();
			if (o instanceof ActionListenerComponent) {
				((ActionListenerComponent) o).ok();
			}
		}
	}

	public void cancel() {
		Iterator iter = displayedWidgets.iterator();
		while (iter.hasNext()) {
			Object o = iter.next();
			if (o instanceof ActionListenerComponent) {
				((ActionListenerComponent) o).cancel();
			}
		}
	}

	public void reset() {
		Iterator iter = displayedWidgets.iterator();
		while (iter.hasNext()) {
			Object o = iter.next();
			if (o instanceof ActionListenerComponent) {
				((ActionListenerComponent) o).reset();
			}
		}
	}

	private FocusedComponent getFocusedComponent() {
		JComponent focusableComponent = null;
		JComponent firstFocusableComponent = null;
		int tabIndex = -1;
		Iterator iter = displayedWidgets.iterator();
		while (iter.hasNext()) {
			Object o = iter.next();
			if (o instanceof Tab) {
				tabIndex++;
			}
			if (o instanceof Focusable) {
				Focusable f = (Focusable) o;
				if (firstFocusableComponent == null) {
					firstFocusableComponent = f.getFocusableComponent();
				}
				if (f.isFocus()) {
					focusableComponent = f.getFocusableComponent();
					break;
				}
			}
		}
		if (focusableComponent == null && firstFocusableComponent != null) {
			focusableComponent = firstFocusableComponent;
			if (tabIndex >= 0) {
				tabIndex = 0;
			}
		}
		return new FocusedComponent(tabIndex, focusableComponent);
	}
}
