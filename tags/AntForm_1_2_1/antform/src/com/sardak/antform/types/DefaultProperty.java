package com.sardak.antform.types;

/**
 * Default property attributes: include the label and target property.
 * @author René Ghosh
 */
public class DefaultProperty {
	private String label, property;
	private boolean editable = true;
	/**
	 * true if property is editable
	 */
	public boolean isEditable() {
		return editable;
	}
	/**
	 * set property editable
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	/**
	 * @return label.
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label.
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return property.
	 */
	public String getProperty() {
		return property;
	}
	/**
	 * @param property.
	 */
	public void setProperty(String property) {
		this.property = property;
	}
}
