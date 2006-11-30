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
package com.sardak.antform.style;

import java.awt.Color;

/**
 * @author René Ghosh
 * 13 févr. 2005
 */
public class HexConverter {

	/**
	 * get an rgb Color from a hexadecimal string 
	 */
	public static Color translate(String hexString, Color defaultColor) {
		if (hexString==null) {
			return defaultColor;
		}
		if (hexString.startsWith("#")) {
			hexString=hexString.substring(1);
		}
		if (hexString.length()!=6) {
			return defaultColor;
		}
		hexString = hexString.toLowerCase();
		int r = Integer.parseInt(hexString.substring(0,2), 16), 
			g = Integer.parseInt(hexString.substring(2,4), 16), 
			b = Integer.parseInt(hexString.substring(4,6), 16);
		return new Color(r,g, b);
	}
	
	public static void main(String[] args) {
		System.out.println(""+translate("ffff00", null));
	}
}
