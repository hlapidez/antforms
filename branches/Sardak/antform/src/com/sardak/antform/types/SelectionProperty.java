package com.sardak.antform.types;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


/**
 * Property for selecting a value among a list of proposed values.
 * @author René Ghosh
 */
public class SelectionProperty extends DefaultProperty{
	private String values;
	private String separator = ",";
	private String[] splitValues;
	
	
	/**
	 * @return splitValues.
	 */
	public String[] getSplitValues() {
		return splitValues;
	}
	/**
	 * @param splitValues.
	 */
	public void setSplitValues(String[] splitValues) {
		this.splitValues = splitValues;
	}
	/**
	 * @return separator.
	 */
	public String getSeparator() {
		return separator;
	}
	/**
	 * @param separator.
	 */
	public void setSeparator(String separator) {
		this.separator = separator;
		split();
	}
	
	
	/**
	 * @return values.
	 */
	public String getValues() {
		return values;
	}
	/**
	 * split the values	 
	 */
	private void split(){
		StringTokenizer tokenizer = new StringTokenizer(values, separator);
		List valueList = new ArrayList();
		while (tokenizer.hasMoreTokens()){
			String token  = tokenizer.nextToken();
			valueList.add(token.trim());
		}
		splitValues = (String[]) valueList.toArray(new String[valueList.size()]);		
	}
	/**
	 * @param values
	 */
	public void setValues(String values) {
		this.values = values;
		split();
	}
}
