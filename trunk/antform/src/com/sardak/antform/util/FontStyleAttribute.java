package com.sardak.antform.util;

import java.awt.Font;
import java.util.StringTokenizer;

import org.apache.tools.ant.BuildException;

public class FontStyleAttribute {
	private int fontStyle = Font.PLAIN;

	public FontStyleAttribute(String attribute) {
		StringTokenizer st = new StringTokenizer(attribute, ", \t\n\r\f");
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (token.equalsIgnoreCase("plain")) {
				fontStyle = fontStyle | Font.PLAIN;
			} else if (token.equalsIgnoreCase("bold")) {
				fontStyle = fontStyle | Font.BOLD;
			} else if (token.equalsIgnoreCase("italic")) {
				fontStyle = fontStyle | Font.ITALIC;
			} else {
				throw new BuildException("Unknown font style: " + token);
			}
		}
	}
	
	public int getFontStyle() {
		return fontStyle;
	}
}
