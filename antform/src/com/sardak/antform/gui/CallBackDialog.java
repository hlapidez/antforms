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
package com.sardak.antform.gui;

import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * A frame for implementing custom behaviour once the dispose is called: calls a
 * callback function after disposing.
 * 
 * @author Ren� Ghosh 24 f�vr. 2005
 */
public class CallBackDialog extends JDialog {

	private JFrame parentFrame = null;

	/**
	 * Constructor
	 */
	public CallBackDialog() {
		// BUG 2498317: the parentFrame.setLocation(-500, -500) does not work on non-windows systems.
		super(isWindowsSystem() ? new JFrame() : null, true);
		if (isWindowsSystem()) {
			parentFrame = (JFrame) getOwner();
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					parentFrame.setSize(0, 0);
					parentFrame.setLocation(-500, -500);
				}
			});
		}
	}
	
	private static boolean isWindowsSystem() {
		return System.getProperty("os.name").toLowerCase().indexOf("windows") != -1;
	}

	/**
	 * dispose() overload
	 */
	public void dispose() {
		super.hide();
		if (parentFrame != null)
			parentFrame.hide();
	}

	/**
	 * Hide the frame
	 */
	public void hide() {
		dispose();
	}
	
	public void display() {
		if (parentFrame != null)
			parentFrame.setVisible(true);
		show();
	}

	public void setTitle(String title) {
		super.setTitle(title);
		if (parentFrame != null)
			parentFrame.setTitle(title);
	}

	public void setIcon(File iconFile) {
		if (iconFile != null && parentFrame != null) {
			parentFrame.setIconImage(new ImageIcon(iconFile.getAbsolutePath()).getImage());
		}
	}
}
