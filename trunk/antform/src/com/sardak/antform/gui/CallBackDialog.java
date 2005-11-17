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

import javax.swing.JDialog;
import javax.swing.JOptionPane;


/**
 * A frame for implementing custim behaviour once the 
 * dispose is called: calls a callback function
 * after disposing.
 * @author Ren� Ghosh
 * 24 f�vr. 2005
 */
public class CallBackDialog extends JDialog{
		
	private CallBack callBack;
	
	/**
	 * get callback method
	 */
	public CallBack getCallBack() {
		return callBack;
	}
	
	/**
	 * set callback method
	 */
	public void setCallBack(CallBack callBack) {
		this.callBack = callBack;
	}
	/**
	 * Constructor
	 */
	public CallBackDialog(){		
		super(JOptionPane.getRootFrame(), true);			    	    
	}
	
	/**
	 * dispose() overload
	 */
	public void dispose(boolean executeCallBack) {
		if (!executeCallBack) {
			if (callBack!=null) {
				callBack.callbackCommand(null);
			}
		}
		super.hide();
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
		if (callBack!=null) {
			callBack.callbackCommand(message);
		}
		super.hide();				
	}	
}