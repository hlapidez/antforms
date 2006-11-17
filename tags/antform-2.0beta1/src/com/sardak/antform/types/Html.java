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

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.apache.tools.ant.Task;

import com.sardak.antform.gui.ControlPanel;

/**
 * Text property.
 * 
 * @author René Ghosh
 */
public class Html extends BaseType implements HyperlinkListener {

	private int width = 600, height = 400;

	private String data = null;

	private String urlString = null;

	private File file = null;

	private JEditorPane htmlPane;

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getUrlString() {
		return urlString;
	}

	public void setUrlString(String urlString) {
		this.urlString = urlString;
	}

	public void addToControlPanel(ControlPanel panel) {
		htmlPane = new JEditorPane();
		htmlPane.setEditable(false);
		htmlPane.setPreferredSize(new Dimension(width, height));
		htmlPane.addHyperlinkListener(this);
		try {
			if (urlString != null) {
				htmlPane.setPage(urlString);
			} else if (file != null) {
				htmlPane.setPage(file.toURL());
			} else if (data != null) {
				htmlPane.setText(data);
			}
		} catch (IOException ioe) {
			//TODO log something...
		}
		JScrollPane scrollPane = new JScrollPane(htmlPane);
		scrollPane.setPreferredSize(new Dimension(width, height));
		JLabel labelComponent = panel.makeLabel("");
		labelComponent.setLabelFor(scrollPane);
		panel.addRight(scrollPane);
	}

	public void hyperlinkUpdate(HyperlinkEvent event) {
		if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			try {
				htmlPane.setPage(event.getURL());
			} catch (IOException ioe) {
				warnUser("Can't follow link to " + event.getURL().toExternalForm() + ": " + ioe);
			}
		}
	}

	private void warnUser(String message) {
		JOptionPane.showMessageDialog(htmlPane, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public boolean validate(Task task) {
		return true;
	}
}
