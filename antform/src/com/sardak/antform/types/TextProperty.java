package com.sardak.antform.types;


/**
 * Text property.
 * @author René Ghosh
 */
public class TextProperty extends DefaultProperty{
	private int columns=34; 
	private boolean password = false;
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
	public boolean isPassword() {
		return password;
	}
	public void setPassword(boolean password) {
		this.password = password;
	}
}
