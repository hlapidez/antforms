package com.sardak.antform.types;

/**
 * @author Ren� Ghosh
 * 10 janv. 2005
 */
public class Label {
	private String text;

	public void addText(String text) {
		this.text = text;
	}
	/**
	 * get label text
	 */
	public String getText() {
		return text;
	}
}
