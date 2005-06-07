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
package com.sardak.antform.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.UIManager;

import com.sardak.antform.gui.Control;
import com.sardak.antform.gui.ControlPanel;
import com.sardak.antform.interfaces.ValueHandle;
import com.sardak.antform.types.Link;
import com.sardak.antform.types.LinkBar;

/**
 * @author René Ghosh
 * 20 mars 2005
 */
public class ControlsTest {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Control control = new Control(new CallbackTest(), "Configure FTP Servers", null, false);
		ControlPanel panel = control.getPanel();
		
		LinkBar bar = new LinkBar();
		List links = new  ArrayList();			
		bar.addConfiguredLink(new Link("Add an FTP server", "addserver"));
		bar.addConfiguredLink(new Link("Remove an FTP server", "removeServer"));
		panel.addLinkBar(bar);
		panel.addSeparator();
		
		ValueHandle g1 = panel.addBooleanProperty("Passive-mode connection:", "pasv", true, null);
		ValueHandle g2 = panel.addTextProperty("Server address:", "serverAddress", 30, true, false, false, null);
		ValueHandle g3 = panel.addTextProperty("Server login:", "login", 30, true, false, false, null);
		ValueHandle g4 = panel.addTextProperty("Server password:", "password", 30, true, true, false, null);
		
		control.getPanel().addButtonControls("Save properties", "Reset form");	
		
		Properties props = new Properties();
		props.setProperty("pasv", "true");
		control.setProperties(props);
		g2.setValue("login");
		control.show();
		
		
		panel.getProperties().list(System.out);
		System.out.println(g1.getValue());
		System.exit(0);
	}
}
