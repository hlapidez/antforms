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
package com.sardak.antform.gui.helpers;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JTable;

import com.sardak.antform.interfaces.ValueHandle;
import com.sardak.antform.util.CSVReader;
import com.sardak.antform.util.StringUtil;

/**
 * @author René Ghosh
 * 1 avr. 2005
 */
public class TableGetter implements ValueHandle{
	private String rowSeparator, columnSeparator,escapeSequence;	
	private JTable table;
	/**
	 * Constructor
	 */
	public TableGetter(String rowSeparator, String columnSeparator, 
				JTable table, String escapeSequence){
		this.rowSeparator=rowSeparator;
		this.columnSeparator=columnSeparator;
		this.table=table;
		this.escapeSequence=escapeSequence;
		
	}
	/**
	 * get the rendering component
	 */
	public Component getComponent() {
		return table;
	}
	/**
	 * get the value
	 */
	public String getValue() {
		StringBuffer buffer = new StringBuffer();
		int noRows= table.getRowCount();
		int noCols = table.getColumnCount();
		for (int i = 0; i < noRows; i++) {
			for (int j = 0; j < noCols; j++) {
				String value = table.getValueAt(i,j)+"";
				value = StringUtil.searchReplace(value, escapeSequence, escapeSequence+escapeSequence);
				value = StringUtil.searchReplace(value, rowSeparator, escapeSequence+rowSeparator);
				value = StringUtil.searchReplace(value, columnSeparator, escapeSequence+columnSeparator);
				buffer.append(value);
				if (j!=noCols-1){
					buffer.append(columnSeparator);
				}
			}
			if (i!=noRows-1){
				buffer.append(rowSeparator);
			}
		}
		return buffer.toString();
	}
	
	/**
	 * set the value
	 */
	public void setValue(String s) {
		CSVReader rowReader = new CSVReader(rowSeparator, escapeSequence);
		CSVReader columnReader = new CSVReader(columnSeparator, escapeSequence);
		List rowsList = rowReader.digest(s, false);
		List dataList = new ArrayList();
		int maxCols = 0;
		for (Iterator iter = rowsList.iterator(); iter.hasNext();) {
			String row = (String) iter.next();
			List cells = columnReader.digest(row, true);
			String[] cellStrings = (String[]) cells.toArray(new String[cells.size()]);
			dataList.add(cellStrings);
			if (cellStrings.length>maxCols) {
				maxCols = cellStrings.length;
			}
		}
		String[][] array = new String[dataList.size()][maxCols];		
		for (int i=0;i<dataList.size(); i++) {
			String[] row = (String[]) dataList.get(i);
			array[i]=row;
		}		
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				try {
					table.getModel().setValueAt(array[i][j], i,j);	
				} catch (Exception e) {
//					e.printStackTrace();
				}				
			}
		}
	}
}
