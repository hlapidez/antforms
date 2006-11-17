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
package com.sardak.antform.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author René Ghosh
 * @version 1.0
 */
public class CSVReader {
	private String separator,escape, doubleEscape;

	/**
	 * Generic search & replace utility
	 */
	private static final String searchReplace(String s, String search, String replace){
		int start = 0;
		while ((start=s.indexOf(search, start))!=-1){
			s = s.substring(0,start)+replace+s.substring(start+search.length());
			start = start+replace.length();
		}
		return s;
	}
	/**
	 * Constructor
	 */
	public CSVReader(String separator,String escape){
		this.separator = separator;
		this.escape = escape;	
		doubleEscape=escape+escape;
	}
	
	/**
	 * remove escape sequences
	 */
	private String cleanEscapes(String s) {
		int start = 0;
		while ((start=s.indexOf(escape, start))!=-1){
			String prefix = s.substring(0, start);
			String suffix = s.substring(start);
			if (suffix.startsWith(doubleEscape)) {
				suffix = escape+suffix.substring(doubleEscape.length());
				start = prefix.length()+escape.length();				
			} else {
				suffix = suffix.substring(escape.length());
				start = prefix.length();				
			}
			s = prefix+suffix;									
		}
		return s;
	}
	
	/**
	 * check weither a word ends with an escape sequence
	 */
	private boolean endsWithEscape(String s){
		int start = 0;
		int lastIndexOfDoubleEscape = 0;
		while ((start=s.indexOf(doubleEscape, start))!=-1){
			start = start+doubleEscape.length();
			lastIndexOfDoubleEscape = start;
		}
		
		return (s.substring(lastIndexOfDoubleEscape).endsWith(escape));
	}
	
	/**
	 * split a sentence into consitutive words 
	 */
	public List digest(String s, boolean lastLevel) {
		List list = new ArrayList();
		int start = 0;
		while ((start=s.indexOf(separator, start))!=-1) {
			String prefix = s.substring(0,start);
			String suffix = s.substring(start+separator.length());
			if (!endsWithEscape(prefix)){
				String atom = prefix;
				if (lastLevel) {
					atom = cleanEscapes(prefix);
				}
				list.add(atom);
				s = s.substring(start+separator.length());				
				start = 0;
			} else {
				start += separator.length();
			}
		}
		if (lastLevel) {
			s = cleanEscapes(s);
		}
		list.add(s);
		return list;
	}
	
	/**
	 * Digest a series of lines
	 */
	public List digest(List lines, boolean lastLevel){
		List list= new ArrayList();
		for (Iterator iter = lines.iterator(); iter.hasNext();) {
			String line = (String) iter.next();
			list.add(digest(line, lastLevel));
		}
		return list;
	}
	
	/**
	 * test method
	 * @param args
	 */
	public static void main(String[] args) {
		CSVReader reader =new CSVReader("|", "*");
		CSVReader reader2 =new CSVReader(";", "*");
		System.out.println(reader.digest("ha|ho**|hi", true));
		System.out.println(""+reader.digest(reader2.digest("ha|ho|hi;ha|ho|hi", false),true));
		System.out.println(""+reader.digest(reader2.digest("ha|ho|hi**;ha|ho|hi", false),true));
	}
}
