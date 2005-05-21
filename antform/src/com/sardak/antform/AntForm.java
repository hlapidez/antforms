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
package com.sardak.antform;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.taskdefs.CallTarget;

import com.sardak.antform.gui.CallBack;
import com.sardak.antform.types.BaseType;
import com.sardak.antform.types.BooleanProperty;
import com.sardak.antform.types.Cancel;
import com.sardak.antform.types.CheckSelectionProperty;
import com.sardak.antform.types.DateProperty;
import com.sardak.antform.types.DefaultProperty;
import com.sardak.antform.types.FileSelectionProperty;
import com.sardak.antform.types.Label;
import com.sardak.antform.types.LinkBar;
import com.sardak.antform.types.ListProperty;
import com.sardak.antform.types.MultilineTextProperty;
import com.sardak.antform.types.NumberProperty;
import com.sardak.antform.types.RadioSelectionProperty;
import com.sardak.antform.types.SelectionProperty;
import com.sardak.antform.types.Separator;
import com.sardak.antform.types.Tab;
import com.sardak.antform.types.Table;
import com.sardak.antform.types.TextProperty;

/**
 * Ant task for empowering form-based user interaction
 * @author René Ghosh
 */
public class AntForm extends AbstractTaskWindow implements CallBack{
	private String okMessage = "OK";
	private String resetMessage = "Reset";	
	private String save = null;
	private String nextTarget,previousTarget;
	private String message = null;
	private String focus=null;
	private boolean built = false;
			
		
	/**
	 * get the focus property
	 */
	public String getFocus() {
		return focus;
	}
	
	/**
	 * set the focus property
	 */
	public void setFocus(String focus) {
		this.focus = focus;
	}
	
	/**
	 * get the next target
	 */
	public String getNextTarget() {
		return nextTarget;
	}
	
	/**
	 * set the next target
	 */
	public void setNextTarget(String nextTarget) {
		this.nextTarget = nextTarget;		
	}
	
	/**
	 * get the previous target
	 */
	public String getPreviousTarget() {
		return previousTarget;
	}
	
	/**
	 * set the previous target
	 */
	public void setPreviousTarget(String previousTarget) {
		this.previousTarget = previousTarget;
	}
	
	/**
	 * check that the base properties are correctly set.
	 */
	private void checkBaseProperties(DefaultProperty property) {
		if (property.getLabel()==null) {
			super.log("label attribute of the property "+property.getClass().getName()+" cannot be null.");
			needFail = true;
		}
		if (property.getProperty()==null) {
			super.log("property attribute of the property "+property.getClass().getName()+" cannot be null.");
			needFail = true;
		}
	}
	/**
	 * add a configured text property
	 * @param textProperty
	 */
	public void addConfiguredTextProperty(TextProperty textProperty){
		checkBaseProperties(textProperty);
		properties.add(textProperty);
	}
	
	/**
	 * Add a cancel button
	 */
	public void addConfiguredCancel(Cancel cancel){
		if (cancel.getLabel()==null) {
			log("No label on cancel button");
			needFail = true;
		}
		properties.add(cancel);
	}
	
	/**
	 * add a configured separator
	 * @param textProperty
	 */
	public void addConfiguredSeparator(Separator separator){		
		properties.add(separator);
	}
	
	/**
	 * add a configured text property
	 * @param dateProperty
	 */
	public void addConfiguredDateProperty(DateProperty dateProperty){
		checkBaseProperties(dateProperty);
		properties.add(dateProperty);
	}
	
	/**
	 * add a configured number property
	 */
	public void addConfiguredNumberProperty(NumberProperty numberProperty){		
		properties.add(numberProperty);
	}
	
	/**
	 * add a configured list property
	 */
	public void addConfiguredListProperty(ListProperty listProperty){
		checkBaseProperties(listProperty);
		if (listProperty.getValues()==null) {
			super.log("values attribute of the property "+listProperty.getClass().getName()+" cannot be null.");
		}
		properties.add(listProperty);
	}
	/**
	 * add a configured multiline text property
	 * @param multilineTextProperty
	 */
	public void addConfiguredMultilineTextProperty(MultilineTextProperty multilineTextProperty){
		checkBaseProperties(multilineTextProperty);
		properties.add(multilineTextProperty);
	}
	/**
	 * add a configured selection property
	 * @param textProperty
	 */
	public void addConfiguredSelectionProperty(SelectionProperty selectionProperty){
		checkBaseProperties(selectionProperty);
		properties.add(selectionProperty);
	}
	
	/**
	 * add a configured selection property
	 * @param textProperty
	 */
	public void addConfiguredTab(Tab tab){
		if (tab.getLabel()==null) {
			log("tab must have a label");
			needFail=true;
		}
		properties.add(tab);
		tabbed=true;
	}
	
	/**
	 * add a configured selection property
	 * @param textProperty
	 */
	public void addConfiguredRadioSelectionProperty(RadioSelectionProperty radioSelectionProperty){
		checkBaseProperties(radioSelectionProperty);
		properties.add(radioSelectionProperty);
	}
	
	/**
	 * add a configured checkGroup property
	 * @param textProperty
	 */
	public void addConfiguredCheckSelectionProperty(CheckSelectionProperty checkSelectionProperty){
		checkBaseProperties(checkSelectionProperty);
		properties.add(checkSelectionProperty);
	}

	/**
	 * add a configured boolean property
	 * @param textProperty
	 */
	public void addConfiguredBooleanProperty(BooleanProperty booleanProperty){
		checkBaseProperties(booleanProperty);
		properties.add(booleanProperty);		
	}
	
	
	/**
	 * add a configured link bar
	 */
	public void addConfiguredLinkBar(LinkBar linkBar) {
		properties.add(linkBar);
	}
	
	/**
	 * Construct the gui
	 */
	public void build(){		
		control.getPanel().addButtonControls(okMessage, resetMessage);
		control.getPanel().setOkMessage(okMessage);
		control.getPanel().setResetMessage(resetMessage);
		super.build();
		for (Iterator iter = properties.iterator(); iter.hasNext();) {
			BaseType o = (BaseType) iter.next();
			if (o.getIf()!=null){
				if (!getProject().getProperties().containsKey(o.getIf())){
					continue;
				}
			}
			if (o.getUnless()!=null) {
				if (getProject().getProperties().containsKey(o.getUnless())){
					continue;
				}			
			}
			if (o instanceof Tab) {
				Tab tab= (Tab) o;
				control.getPanel().addTab(tab.getLabel());
			} else if (o instanceof NumberProperty) {
				NumberProperty np = (NumberProperty) o;
				control.getPanel().addNumberProperty(
						np.getLabel(),
						np.getProperty(),
						np.isEditable(),
						np.getMin(),
						np.getMax(),
						np.getStep()
						
						);
			} else if (o instanceof Cancel) {
				Cancel cancel = (Cancel) o;
				control.getPanel().addCancel(cancel.getLabel());
				
			} else if (o instanceof FileSelectionProperty) {
				FileSelectionProperty fileSelectionProperty = (FileSelectionProperty) o;
				control.getPanel().addFileSelectionProperty(fileSelectionProperty.getLabel(),
						fileSelectionProperty.getProperty(), fileSelectionProperty.isEditable(),
						fileSelectionProperty.getColumns(), fileSelectionProperty.isDirectoryChooser(),
						fileSelectionProperty.isRequired());
			} else if (o instanceof LinkBar) {
				LinkBar linkBar = (LinkBar) o;
				control.getPanel().addLinkBar(linkBar);
			} else if (o instanceof Separator) {
				control.getPanel().addSeparator();
			} else if (o instanceof Label) {
				Label label = (Label) o;
				control.getPanel().makeAddLabel(label.getText(), label.getColumns(), label.getRows());
			} else if (o instanceof Table) {
				Table table = (Table) o;
				String[] cols = table.splitColumns();
				String[][] data = table.splitData();
				control.getPanel().addTableProperty(table.getLabel(),
						table.getProperty(), table.isEditable(),
						cols, data, table.getRowSeparator(), 
						table.getColumnSeparator(),
						table.getEscapeSequence(), table.getWidth(), 
						table.getHeight(), table.getColumnWidth());
			}else if (o instanceof RadioSelectionProperty) {
				RadioSelectionProperty radioSelectionProperty = (RadioSelectionProperty) o;
				control.getPanel().addConstrainedRadioProperty(radioSelectionProperty.getLabel(),
						radioSelectionProperty.getProperty(), 
						radioSelectionProperty.getSplitValues(),
						radioSelectionProperty.isEditable()
						);
			} else if (o instanceof CheckSelectionProperty) {
				CheckSelectionProperty checkSelectionProperty = (CheckSelectionProperty) o;
				control.getPanel().addMultiCheckProperty(checkSelectionProperty.getLabel(),
						checkSelectionProperty.getProperty(), 
						checkSelectionProperty.getSplitValues(),
						checkSelectionProperty.getSeparator(),
						checkSelectionProperty.getEscapeSequence(),
						checkSelectionProperty.isEditable()
						);
			} else if (o instanceof SelectionProperty) {
				SelectionProperty selectionProperty = (SelectionProperty) o;
				control.getPanel().addConstrainedProperty(selectionProperty.getLabel(),
						selectionProperty.getProperty(), 
						selectionProperty.getSplitValues(),
						selectionProperty.isEditable()
						);	
			} else if (o instanceof DateProperty) {
				DateProperty dateProperty = (DateProperty) o;
				control.getPanel().addDateProperty(
						dateProperty.getLabel(),
						dateProperty.getProperty(),
						dateProperty.isEditable(),
						dateProperty.getDateFormat(),
						dateProperty.isRequired()
						);		
			} else if (o instanceof ListProperty) {
				ListProperty listProperty = (ListProperty) o;
				control.getPanel().addListProperty(
						listProperty.getLabel(),
						listProperty.getProperty(),				
						listProperty.isEditable(),
						listProperty.asList()	
						);				
			} else if (o instanceof MultilineTextProperty) {
				MultilineTextProperty multilineTextProperty = (MultilineTextProperty) o;
				control.getPanel().addMultiLineTextProperty(
						multilineTextProperty.getLabel(),
						multilineTextProperty.getProperty(),
						multilineTextProperty.getColumns(),
						multilineTextProperty.getRows(),
						multilineTextProperty.isEditable(),
						multilineTextProperty.isRequired());	
			} else if (o instanceof BooleanProperty){
				BooleanProperty booleanProperty = (BooleanProperty) o;
				control.getPanel().addBooleanProperty(booleanProperty.getLabel(),
						booleanProperty.getProperty(), booleanProperty.isEditable());
			} else if (o instanceof TextProperty){
				TextProperty textProperty = (TextProperty) o;
				control.getPanel().addTextProperty(
						textProperty.getLabel(),
						textProperty.getProperty(),
						textProperty.getColumns(),
						textProperty.isEditable(),
						textProperty.isPassword(),
						textProperty.isRequired());
			}
		}				
		built = true;
	}
	
	/**
	 * add a configured table
	 */
	public void addConfiguredTable(Table table) {
		checkBaseProperties(table);
		if (table.getData()==null) {
			super.log("data attribute of the property "+table.getClass().getName()+" cannot be null.");
			needFail = true;
		}
		if (table.getColumns()==null) {
			super.log("columns attribute of the property "+table.getClass().getName()+" cannot be null.");
			needFail = true;
		}
		properties.add(table);
	}
	
	/**
	 * add a configured boolean property
	 * @param textProperty
	 */
	public void addConfiguredFileSelectionProperty(FileSelectionProperty fileSelectionProperty){
		checkBaseProperties(fileSelectionProperty);
		properties.add(fileSelectionProperty);		
	}	
	
	/**
	 * add a configured label
	 * @param textProperty
	 */
	public void addConfiguredLabel(Label label){
		label.addText(getProject().replaceProperties(label.getText()));
		properties.add(label);
	}

	/**
	 * initialize the properties
	 */
	public void init() throws BuildException {		
		properties = new ArrayList();
	}
	
	/**
	 * Map properties from the control back to the project
	 */
	private void getProperties(){
		Properties props = control.getProperties();
		Project p = getProject();
		for (Iterator i=props.keySet().iterator();i.hasNext();){
			String property = (String) i.next();
			p.setProperty(property, props.getProperty(property));
		}			
	}
	
	/**
	 * callback method
	 * @see architecture.integration.tests.CallBack#callback()
	 */
	public void callbackCommand(String message){			
		quit = true;
		this.message = message;
	}
	
	
	/**
	 * implement a callback that ports to a 
	 * target by autumatically setting okMessage and nextTarget
	 * values 
	 */
	public void callbackLink(String target) {		
		quit = true;
		this.message=okMessage;
		nextTarget=target;
	}
	
	

	/**
	 * @see org.apache.tools.ant.Task#execute()
	 */
	public void execute() throws BuildException {		
		preliminaries();
		if (!built) {
			build();
		}
		super.execute();		
		control.setProperties(getProject().getProperties());			
		if (previousTarget!=null) {
			control.getPanel().setDisposeOnReset(true);
		}
		if (focus!=null) {
			control.getPanel().focus(focus);
		}		
		control.show();
		getProperties();
		if (save!=null) {
			try {
				File file = new File(save);
				if (file.exists()) {
					Properties props = new Properties();
					props.load(new FileInputStream(file));
					for (Iterator iter = props.keySet().iterator(); iter.hasNext();) {
						String key = (String) iter.next();
						if (!control.getProperties().containsKey(key)) {
							control.getProperties().setProperty(key, props.getProperty(key));
						}
						
					}
				}
				control.getProperties().save(new FileOutputStream(save), "");
			} catch (FileNotFoundException e) {
				throw new BuildException(e);
			} catch (IOException e) {
				throw new BuildException(e);
			}
		}
		Target theNextTarget = findTargetByName(nextTarget);
		Target thePreviousTarget = findTargetByName(previousTarget);
		if (dynamic) {
			built = false;
			control = null;
		}		
		if (message!=null){		
			CallTarget callee = (CallTarget) getProject().createTask("antcall");
			callee.setOwningTarget(getOwningTarget());
			callee.setTaskName(getTaskName());
			callee.setLocation(getLocation());									
			if ((message.equals(okMessage))&&(theNextTarget!=null)) {
				callee.setTarget(nextTarget);
				callee.execute();
			} else if ((message.equals(resetMessage))&&(thePreviousTarget!=null)) {
				callee.setTarget(previousTarget);
				callee.execute();
			} 
		}				
	}

	/**
	 * @return okMessage.
	 */
	public String getOkMessage() {
		return okMessage;
	}
	/**
	 * @param okMessage.
	 */
	public void setOkMessage(String okMessage) {
		this.okMessage = okMessage;
	}
	/**
	 * @return resetMessage.
	 */
	public String getResetMessage() {
		return resetMessage;
	}
	/**
	 * @param resetMessage.
	 */
	public void setResetMessage(String resetMessage) {
		this.resetMessage = resetMessage;
	}
	
	/**
	 * @return save.
	 */
	public String getSave() {
		return save;
	}
	
	/**
	 * @param save.
	 */
	public void setSave(String save) {
		this.save = save;
	}
}
