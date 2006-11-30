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

import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

/**
 * @author René Ghosh
 * 14 févr. 2005
 */
public class FontConverter {
	private static Map fontWeightMap = new HashMap();
	static {
		fontWeightMap.put("bold", new Integer(Font.BOLD));
		fontWeightMap.put("plain", new Integer(Font.PLAIN));
		fontWeightMap.put("italic", new Integer(Font.ITALIC));
		fontWeightMap.put("bold-italic", new Integer(Font.BOLD|Font.ITALIC));
	}
	
	/**
	 * Convert from string to Java Font int constant 
	 */
	public static int convert(String string) {		
		return ((Integer) fontWeightMap.get(string.toLowerCase())).intValue();
	}
}
