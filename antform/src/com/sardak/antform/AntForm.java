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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.taskdefs.CallTarget;

import com.sardak.antform.gui.ButtonPanel;
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
import com.sardak.antform.util.FileProperties;

/**
 * Ant task for empowering form-based user interaction
 * 
 * @author René Ghosh
 */
public class AntForm extends AbstractTaskWindow implements CallBack {
    private String okMessage = "OK";

    private String resetMessage = "Reset";

    private String cancelMessage = null;

    private String save = null;

    private String nextTarget, previousTarget;

    private String message = null;

    private String focus = null;

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
        if (property.getLabel() == null) {
            super.log("label attribute of the property "
                    + property.getClass().getName() + " cannot be null.");
            needFail = true;
        }
        if (property.getProperty() == null) {
            super.log("property attribute of the property "
                    + property.getClass().getName() + " cannot be null.");
            needFail = true;
        }
    }

    /**
     * add a configured text property
     * 
     * @param textProperty
     */
    public void addConfiguredTextProperty(TextProperty textProperty) {
        checkBaseProperties(textProperty);
        widgets.add(textProperty);
    }

    /**
     * Add a cancel button
     */
    public void addConfiguredCancel(Cancel cancel) {
        if (cancel.getLabel() == null) {
            log("No label on cancel button");
            needFail = true;
        }
        cancelMessage = cancel.getLabel();
    }

    /**
     * add a configured separator
     * 
     * @param textProperty
     */
    public void addConfiguredSeparator(Separator separator) {
        widgets.add(separator);
    }

    /**
     * add a configured html pane
     * 
     * @param html
     */
    // public void addConfiguredHtml(Html html){
    // properties.add(html);
    // }
    /**
     * add a configured text property
     * 
     * @param dateProperty
     */
    public void addConfiguredDateProperty(DateProperty dateProperty) {
        checkBaseProperties(dateProperty);
        widgets.add(dateProperty);
    }

    /**
     * add a configured number property
     */
    public void addConfiguredNumberProperty(NumberProperty numberProperty) {
        widgets.add(numberProperty);
    }

    /**
     * add a configured list property
     */
    public void addConfiguredListProperty(ListProperty listProperty) {
        checkBaseProperties(listProperty);
        if (listProperty.getValues() == null) {
            super.log("values attribute of the property "
                    + listProperty.getClass().getName() + " cannot be null.");
        }
        widgets.add(listProperty);
    }

    /**
     * add a configured multiline text property
     * 
     * @param multilineTextProperty
     */
    public void addConfiguredMultilineTextProperty(
            MultilineTextProperty multilineTextProperty) {
        checkBaseProperties(multilineTextProperty);
        widgets.add(multilineTextProperty);
    }

    /**
     * add a configured selection property
     * 
     * @param textProperty
     */
    public void addConfiguredSelectionProperty(
            SelectionProperty selectionProperty) {
        checkBaseProperties(selectionProperty);
        widgets.add(selectionProperty);
    }

    /**
     * add a configured selection property
     * 
     * @param textProperty
     */
    public void addConfiguredTab(Tab tab) {
        if (tab.getLabel() == null) {
            log("tab must have a label");
            needFail = true;
        }
        widgets.add(tab);
        tabbed = true;
    }

    /**
     * add a configured selection property
     * 
     * @param textProperty
     */
    public void addConfiguredRadioSelectionProperty(
            RadioSelectionProperty radioSelectionProperty) {
        checkBaseProperties(radioSelectionProperty);
        widgets.add(radioSelectionProperty);
    }

    /**
     * add a configured checkGroup property
     * 
     * @param textProperty
     */
    public void addConfiguredCheckSelectionProperty(
            CheckSelectionProperty checkSelectionProperty) {
        checkBaseProperties(checkSelectionProperty);
        widgets.add(checkSelectionProperty);
    }

    /**
     * add a configured boolean property
     * 
     * @param textProperty
     */
    public void addConfiguredBooleanProperty(BooleanProperty booleanProperty) {
        checkBaseProperties(booleanProperty);
        widgets.add(booleanProperty);
    }

    /**
     * add a custom widget
     * 
     * @param textProperty
     */
    public void addConfigured(BaseType widget) {
        widgets.add(widget);
    }

    /**
     * add a configured link bar
     */
    public void addConfiguredLinkBar(LinkBar linkBar) {
        widgets.add(linkBar);
    }

    /**
     * Construct the gui
     */
    public void build() {
        control.getPanel().addButtonPanel(
                new ButtonPanel(okMessage, resetMessage, cancelMessage, control
                        .getPanel()));
        super.build();
        built = true;
    }

    /**
     * add a configured table
     */
    public void addConfiguredTable(Table table) {
        checkBaseProperties(table);
        if (table.getData() == null) {
            super.log("data attribute of the property "
                    + table.getClass().getName() + " cannot be null.");
            needFail = true;
        }
        if (table.getColumns() == null) {
            super.log("columns attribute of the property "
                    + table.getClass().getName() + " cannot be null.");
            needFail = true;
        }
        widgets.add(table);
    }

    /**
     * add a configured boolean property
     * 
     * @param textProperty
     */
    public void addConfiguredFileSelectionProperty(
            FileSelectionProperty fileSelectionProperty) {
        checkBaseProperties(fileSelectionProperty);
        widgets.add(fileSelectionProperty);
    }

    /**
     * add a configured label
     * 
     * @param textProperty
     */
    public void addConfiguredLabel(Label label) {
        label.addText(getProject().replaceProperties(label.getText()));
        widgets.add(label);
    }

    /**
     * initialize the properties
     */
    public void init() throws BuildException {
        widgets = new ArrayList();
    }

    /**
     * Map properties from the control back to the project
     */
    private void getProperties() {
        Properties props = control.getProperties();
        Project p = getProject();
        for (Iterator i = props.keySet().iterator(); i.hasNext();) {
            String property = (String) i.next();
            p.setProperty(property, props.getProperty(property));
        }
    }

    /**
     * 
     * @return a Properties object containing the form properties.
     */
    private Properties getPropertiesToSave() {
        if (widgets == null)
            return null;
        Properties p = control.getProperties();
        ListIterator iter = widgets.listIterator();
        Properties pList = new Properties();
        while (iter.hasNext()) {
            Object o = iter.next();
            if (o instanceof DefaultProperty) {
                DefaultProperty dp = (DefaultProperty) o;
                String value = p.getProperty(dp.getProperty());
                if (value != null)
                    pList.put(dp.getProperty(), value);
            }
        }
        return pList;
    }

    /**
     * callback method
     * 
     * @see architecture.integration.tests.CallBack#callback()
     */
    public void callbackCommand(String message) {
        this.message = message;
    }

    /**
     * implement a callback that ports to a target by automatically setting
     * okMessage and nextTarget values
     */
    public void callbackLink(String target) {
        // quit = true;
        this.message = okMessage;
        nextTarget = target;
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

        if (previousTarget != null) {
            control.getPanel().setDisposeOnReset(true);
        }
        if (focus != null) {
            control.getPanel().focus(focus);
        }
        control.show();
        getProperties();
        if (save != null) {
            try {
                File file = new File(save);
                FileProperties props = new FileProperties(file);
                props.store(getPropertiesToSave());
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
        if (message != null) {
            CallTarget callee = (CallTarget) getProject().createTask("antcall");
            callee.setOwningTarget(getOwningTarget());
            callee.setTaskName(getTaskName());
            callee.setLocation(getLocation());
            if ((message.equals(okMessage)) && (theNextTarget != null)) {
                callee.setTarget(nextTarget);
                callee.execute();
            } else if ((message.equals(resetMessage))
                    && (thePreviousTarget != null)) {
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
     * @return cancelMessage.
     */
    public String getCancelMessage() {
        return cancelMessage;
    }

    /**
     * @param cancelMessage.
     */
    public void setCancelMessage(String cancelMessage) {
        this.cancelMessage = cancelMessage;
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
