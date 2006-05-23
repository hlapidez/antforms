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
package com.sardak.antform.gui;

import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * A frame for implementing custim behaviour once the dispose is called: calls a
 * callback function after disposing.
 * 
 * @author René Ghosh 24 févr. 2005
 */
public class CallBackDialog extends JDialog {

	private JFrame parentFrame;

	/**
	 * Constructor
	 */
	public CallBackDialog() {
		super(new JFrame(), true);
		parentFrame = (JFrame) getOwner();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				parentFrame.setSize(0, 0);
				parentFrame.setLocation(-500, -500);
				parentFrame.setVisible(true);
			}
		});
	}

	/**
	 * dispose() overload
	 */
	public void dispose(boolean executeCallBack) {
		super.hide();
		parentFrame.hide();
	}

	/**
	 * Hide the frame
	 */
	public void hide() {
		dispose(true);
	}

	/**
	 * Dispose the frame and call the callback
	 */
	public void dispose(String message) {
		super.hide();
		parentFrame.hide();
	}

	public void setTitle(String title) {
		super.setTitle(title);
		parentFrame.setTitle(title);
	}
	

	public void setIcon(File iconFile) {
		if (iconFile != null) {
			parentFrame.setIconImage(new ImageIcon(iconFile.getAbsolutePath()).getImage());
		}
	}
}
