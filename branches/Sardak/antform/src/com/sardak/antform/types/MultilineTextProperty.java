package com.sardak.antform.types;


/**
 * Text property edited over multiple lines.
 * @author René Ghosh
 */
public class MultilineTextProperty extends DefaultProperty{
	private int columns=40, rows=5;
	/**
	 * @return columns.
	 */
	public int getColumns() {
		return columns;
	}
	/**
	 * @param columns.
	 */
	public void setColumns(int columns) {
		this.columns = columns;
	}
	/**
	 * @return rows.
	 */
	public int getRows() {
		return rows;
	}
	/**
	 * @param rows.
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}
}
