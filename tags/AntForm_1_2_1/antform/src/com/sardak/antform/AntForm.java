package com.sardak.antform;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Properties;

import org.apache.tools.ant.BuildException;

import com.sardak.antform.types.BooleanProperty;
import com.sardak.antform.types.DefaultProperty;
import com.sardak.antform.types.Label;
import com.sardak.antform.types.MultilineTextProperty;
import com.sardak.antform.types.SelectionProperty;
import com.sardak.antform.types.TextProperty;

/**
 * Ant task for empowering form-based user interaction
 * @author René Ghosh
 */
public class AntForm extends AbstractTaskWindow implements AntCallBack{
	private String okMessage = "OK";
	private String resetMessage = "Reset";	
	private String save = null;
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
		control.getPanel().addTextProperty(
				textProperty.getLabel(),
				textProperty.getProperty(),
				textProperty.getColumns(),
				textProperty.isEditable(),
				textProperty.isPassword());
		control.pack();
	}
	/**
	 * add a configured multiline text property
	 * @param multilineTextProperty
	 */
	public void addConfiguredMultilineTextProperty(MultilineTextProperty multilineTextProperty){
		checkBaseProperties(multilineTextProperty);
		control.getPanel().addMultiLineTextProperty(
					multilineTextProperty.getLabel(),
					multilineTextProperty.getProperty(),
					multilineTextProperty.getColumns(),
					multilineTextProperty.getRows(),
					multilineTextProperty.isEditable());
		control.pack();
	}
	/**
	 * add a configured selection property
	 * @param textProperty
	 */
	public void addConfiguredSelectionProperty(SelectionProperty selectionProperty){
		checkBaseProperties(selectionProperty);
		control.getPanel().addConstrainedProperty(selectionProperty.getLabel(),
				selectionProperty.getProperty(), 
				selectionProperty.getSplitValues(),
				selectionProperty.isEditable()
				);
		control.pack();
	}

	/**
	 * add a configured boolean property
	 * @param textProperty
	 */
	public void addConfiguredBooleanProperty(BooleanProperty booleanProperty){
		checkBaseProperties(booleanProperty);
		control.getPanel().addBooleanProperty(booleanProperty.getLabel(),
				booleanProperty.getProperty(), booleanProperty.isEditable());		
	}
	
	/**
	 * add a configured label
	 * @param textProperty
	 */
	public void addConfiguredLabel(Label label){
		control.getPanel().addLabel(label.getText());
		control.pack();
	}

	public void init() throws BuildException {
		super.init();
		control.getPanel().addButtonControls(okMessage, resetMessage);
	}
	/**
	 * callback method
	 * @see architecture.integration.tests.AntCallBack#callback()
	 */
	public void callback(String message){
		Properties props = control.getProperties();
		for (Iterator i=props.keySet().iterator();i.hasNext();){
			String property = (String) i.next();
			getProject().setProperty(property, props.getProperty(property));
		}		
		quit = true;
	}
	
	

	/**
	 * @see org.apache.tools.ant.Task#execute()
	 */
	public void execute() throws BuildException {
		super.execute();				
		control.setProperties(getProject().getProperties());				
		control.getPanel().setOkMessage(okMessage);
		control.getPanel().setResetMessage(resetMessage);				
		waitForValidation();
		if (save!=null) {
			try {
				control.getProperties().save(new FileOutputStream(save), "");
			} catch (FileNotFoundException e) {
				throw new BuildException(e);
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
