package com.sardak.antform.util;

import org.apache.tools.ant.types.EnumeratedAttribute;

public class ActionType extends EnumeratedAttribute {
	public static final int UNDEFINED = -1;
	public static final int OK = 0;
	public static final int CANCEL = 1;
	public static final int RESET = 2;
	private static final String[] values = {"ok", "cancel", "reset"};

	public String[] getValues() {
		return values;
	}
	
	public static int getType(String type) {
		int res = OK; // default (if type = null)
		if ("cancel".equals(type)) {
			res = CANCEL;
		} else if ("reset".equals(type)) {
			res = RESET;
		}
		return res;
	}
	
	public int getType() {
		return getType(getValue());
	}

	public static String getType(int type) {
		return values[type];
	}
}
