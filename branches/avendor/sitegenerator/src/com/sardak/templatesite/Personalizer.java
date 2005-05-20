/*
 * Créé le 31 déc. 2004
 */
package com.sardak.templatesite;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

/**
 * @author 1699016
 */
public class Personalizer {
	
	public static final String fileContent(File file) throws IOException {
		int byteBite = 1024;
		byte[] word= new byte[byteBite];
		InputStream input = new FileInputStream(file);
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int numberRead = 0;
		while ((numberRead=input.read(word))!=-1){
			buffer.write(word, 0, numberRead);
		}
		return new String(buffer.toByteArray()).trim();
	}

	public static final byte[] byteContent(File file) throws IOException {
		int byteBite = 1024;
		byte[] word= new byte[byteBite];
		InputStream input = new FileInputStream(file);
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int numberRead = 0;
		while ((numberRead=input.read(word))!=-1){
			buffer.write(word, 0, numberRead);
		}
		return buffer.toByteArray();
	}
	
	public static String replace(String holder, String toReplace, String replaceString) {
		int start = holder.indexOf(toReplace);
		while (start!=-1) {
			holder = holder.substring(0, start)+replaceString+holder.substring(start+toReplace.length());
			start = start+replaceString.length();
			start = holder.indexOf(toReplace, start);
		}
		return holder;
	}
	
	public static String personalize(String content, String property, String value) throws IOException {
		String local = content;
		local = replace(local, "${"+property+"}",value);
		return local;
	}
	
	public static String personalize(String content, Properties props) throws IOException {
		String local = content;
		for (Iterator i=props.keySet().iterator();i.hasNext();) {
			String property = (String) i.next();
			String value = props.getProperty(property);
			local = replace(local, "${"+property+"}",value);
		}
		return local;
	}
}
