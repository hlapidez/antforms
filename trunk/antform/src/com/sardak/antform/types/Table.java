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
package com.sardak.antform.types;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sardak.antform.util.CSVReader;

/**
 * @author Ren� Ghosh
 * 1 avr. 2005
 */
public class Table extends DefaultProperty {
	private String columns, data;
	private boolean editable=true;
	private String rowSeparator=",", columnSeparator=";";
	private String escapeSequence = "\\";
	private String[] cols;
	private String[][] values;
	private int width=-1, height=-1;
	private int columnWidth = -1;
	
	public int getColumnWidth() {
		return columnWidth;
	}
	public void setColumnWidth(int columnWidth) {
		this.columnWidth = columnWidth;
	}
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
	public String getColumnSeparator() {
		return columnSeparator;
	}
	public void setColumnSeparator(String columnSeparator) {
		this.columnSeparator = columnSeparator;
	}
	public String getEscapeSequence() {
		return escapeSequence;
	}
	public void setEscapeSequence(String escapeSequence) {
		this.escapeSequence = escapeSequence;
	}
	public String getRowSeparator() {
		return rowSeparator;
	}
	public void setRowSeparator(String rowSeparator) {
		this.rowSeparator = rowSeparator;
	}
	public String getColumns() {
		return columns;
	}
	public void setColumns(String columns) {
		this.columns = columns;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	/**
	 * split the values into a table
	 */
	public String[][] splitData(){		
		CSVReader rowReader = new CSVReader(rowSeparator, escapeSequence);
		CSVReader columnReader = new CSVReader(columnSeparator, escapeSequence);
		List rowsList = rowReader.digest(data, false);
		List dataList = new ArrayList();
		int maxCols = cols.length;
		for (Iterator iter = rowsList.iterator(); iter.hasNext();) {
			String row = (String) iter.next();
			if (row.trim().length()==0) {
				continue;
			}
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
		return array;  		
	}

	/**
	 * split the column headers
	 */
	public String[]  splitColumns(){
		CSVReader columnReader = new CSVReader(columnSeparator, escapeSequence);
		List values = columnReader.digest(columns, true);
		cols = (String[]) values.toArray(new String[values.size()]);
		return cols;
	}
	
	public static void main(String[] args) {
		Table t = new Table();
		t.setRowSeparator(";");
		t.setColumnSeparator(",");
		t.setColumns("col1,col2,col3");
		t.setData("d1,d2,d3;d4,d5,d6;d7,d8,d9");
		String[][] array = t.splitData();
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				System.out.println(array[i][j]);
			}
		}
		String[] cols = t.splitColumns();
		for (int i = 0; i < cols.length; i++) {
			System.out.println(cols[i]);
		}
	}
	public boolean isEditable() {
		return editable;
	}
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
}
