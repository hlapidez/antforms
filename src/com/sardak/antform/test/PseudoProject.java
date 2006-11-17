package com.sardak.antform.test;

import java.util.Hashtable;
import java.util.Properties;

import org.apache.tools.ant.Project;

public class PseudoProject extends Project {
	private Properties properties;
	
	public PseudoProject() {
		properties = new Properties();
	}
	
	public void setProperty(String name, String value) {
		properties.setProperty(name, value);
	}

	public String getProperty(String name) {
		return properties.getProperty(name);
	}

    public Hashtable getProperties() {
    	return properties;
    }
}
