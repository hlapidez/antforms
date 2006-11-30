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
package com.sardak.antform.types;

import java.awt.Font;

import javax.swing.JComponent;

import org.apache.tools.ant.Task;

import com.sardak.antform.gui.ControlPanel;
import com.sardak.antform.gui.DateChooser;
import com.sardak.antform.interfaces.ActionListenerComponent;
import com.sardak.antform.interfaces.Requirable;
import com.sardak.antform.util.FontStyleAttribute;

/**
 * @author René Ghosh 2 mars 2005
 */
public class DateProperty extends DefaultProperty implements Requirable,
		ActionListenerComponent {
	private String dateFormat;
	private boolean required;
	private DateChooser chooser;
	private String fontName;
	private int fontSize = -1;
	private int fontStyle = Font.PLAIN;

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getFontName() {
		return fontName;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	public int getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(FontStyleAttribute fsa) {
		this.fontStyle = fsa.getFontStyle();
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public void addToControlPanel(ControlPanel panel) {
		chooser = new DateChooser(dateFormat);
		panel.getStylesheetHandler().addDateChooser(chooser);
		chooser.setEnabled(isEditable());
		if (fontName != null || fontSize == -1 ) {
			Font f = chooser.getFont();
			String name = fontName == null ? f.getFontName() : fontName;
			int size = fontSize > 0 ? fontSize : f.getSize();
			chooser.setFont(new Font(name, fontStyle, size));
		}
		initComponent(chooser, panel);
	}

	public boolean validate(Task task) {
		boolean isValid = super.validate(task, "Date");
		if (getDateFormat() == null) {
			task.log("DateProperty : attribute \"dateformat\" missing.");
			isValid = false;
		}
		return isValid;
	}

	public void ok() {
		getProject().setProperty(getProperty(), chooser.getText());
	}

	public void reset() {
		chooser.setText(getCurrentProjectPropertyValue());
	}

	public boolean requiredStatusOk() {
		boolean ok = true;
		if (isRequired() && "".equals(chooser.getText())) {
			ok = false;
			chooser.requestFocus();
		}
		return ok;
	}

	public JComponent getFocusableComponent() {
		return chooser;
	}
}
