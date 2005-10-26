package com.sardak.antform.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 
 * The FileProperties class allows to store properties without changing the
 * formatting of the file. Already existing properties are updated others are
 * added to the end of the file.
 * 
 * @see java.util.Properties
 * 
 * @author k3rmitt
 * 
 */
public class FileProperties {

	private File propertyFile;

	private Properties originalProperties;

	private List lineList;

	public FileProperties(File pFile) throws IOException {
		if (pFile == null)
			throw new IOException("Invalid file");

		propertyFile = pFile;
		originalProperties = new Properties();
		lineList = new ArrayList();

		if (pFile.exists()) {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(pFile));
			originalProperties.load(bis);
			bis.close();

			BufferedReader br = new BufferedReader(new FileReader(pFile));
			String line;
			while ((line = br.readLine()) != null) {
				lineList.add(line);
			}
		}
	}

	/**
	 * 
	 * Save to the file the properties.
	 * 
	 * @param p
	 *            the properties to save
	 * 
	 * @throws IOException
	 *             if something goes wrong
	 * 
	 */
	public void store(Properties p) throws IOException {
		if (p == null)
			return;
		Set set = p.entrySet();
		Iterator iter = set.iterator();

		if (!iter.hasNext())
			return;

		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();

			if (originalProperties.containsKey(key)) {
				int equalIndex;
				for (int currentLineNumber = 0; currentLineNumber < lineList.size(); currentLineNumber++) {
					String line = (String) lineList.get(currentLineNumber);
					if ((equalIndex = line.indexOf('=')) != -1) {
						// Extract property key which may be preceeded or
						// followed by spaces or tabs
						String lineKey = line.substring(0, equalIndex).trim();
						if (lineKey.equals(convert(key, true))) {
							StringBuffer sb = new StringBuffer(line);
							sb.replace(equalIndex, line.length(), "=" + convert(value, false));
							lineList.set(currentLineNumber, sb.toString());
							break;
						}
					}
				}
			} else {
				lineList.add(convert(key, true) + "=" + convert(value, false));
			}
		}

		// file will be created if it does not already exist.
		BufferedWriter bw = new BufferedWriter(new FileWriter(propertyFile));
		for (int i = 0; i < lineList.size(); i++) {
			bw.write((String) lineList.get(i));
			bw.newLine();
		}
		bw.close();
	}

	private static final String specialSaveChars = "=: \t\r\n\f#!";

	/*
	 * Converts unicodes to encoded &#92;uxxxx and writes out any of the
	 * characters in specialSaveChars with a preceding slash
	 */
	private String convert(String str, boolean escapeSpace) {
		int len = str.length();
		StringBuffer outBuffer = new StringBuffer(len * 2);

		for (int x = 0; x < len; x++) {
			char aChar = str.charAt(x);
			switch (aChar) {
			case ' ':
				if (x == 0 || escapeSpace)
					outBuffer.append('\\');
				outBuffer.append(' ');
				break;
			case '\\':
				outBuffer.append('\\');
				outBuffer.append('\\');
				break;
			case '\t':
				outBuffer.append('\\');
				outBuffer.append('t');
				break;
			case '\n':
				outBuffer.append('\\');
				outBuffer.append('n');
				break;
			case '\r':
				outBuffer.append('\\');
				outBuffer.append('r');
				break;
			case '\f':
				outBuffer.append('\\');
				outBuffer.append('f');
				break;
			default:
				if ((aChar < 0x0020) || (aChar > 0x007e)) {
					outBuffer.append('\\');
					outBuffer.append('u');
					outBuffer.append(toHex((aChar >> 12) & 0xF));
					outBuffer.append(toHex((aChar >> 8) & 0xF));
					outBuffer.append(toHex((aChar >> 4) & 0xF));
					outBuffer.append(toHex(aChar & 0xF));
				} else {
					if (specialSaveChars.indexOf(aChar) != -1)
						outBuffer.append('\\');
					outBuffer.append(aChar);
				}
			}
		}
		return outBuffer.toString();
	}

	/**
	 * Convert a nibble to a hex character
	 * 
	 * @param nibble
	 *            the nibble to convert.
	 */
	private static char toHex(int nibble) {
		return hexDigit[(nibble & 0xF)];
	}

	/** A table of hex digits */
	private static final char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public static void main(String[] args) {
		try {
			FileProperties fp = new FileProperties(new File("test.properties"));

			Properties p = new Properties();
			p.put("gc.home.dir", "d:/java dev/src\\GenCommons");
			p.put("gc.debug", "false");
			p.put("gc.xxx", "   br");
			p.put("gc.zzz", " abcd  etytut");
			p.put("gc.AAA", "hello");

			fp.store(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
