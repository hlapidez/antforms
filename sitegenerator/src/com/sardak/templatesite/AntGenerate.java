package com.sardak.templatesite;

import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.jdom.JDOMException;

/**
 * @author 1699016
 */
public class AntGenerate extends Task{
	private String inputDir, outputDir, skinDir;

	/**
	 * @see org.apache.tools.ant.Task#execute()
	 */
	public void execute() throws BuildException {
		try {
			System.out.println("generate from "+inputDir+" to "+outputDir+" with skin "+skinDir);
			Generator.generate(inputDir, outputDir, skinDir);
		} catch (JDOMException e) {
			throw new BuildException(e);
		} catch (IOException e) {
			throw new BuildException(e);
		}
	}
	/**
	 * @return Retourne inputDir.
	 */
	public String getInputDir() {
		return inputDir;
	}
	/**
	 * @param inputDir La valeur de inputDir à fixer.
	 */
	public void setInputDir(String inputDir) {
		this.inputDir = inputDir;
	}
	/**
	 * @return Retourne outputDir.
	 */
	public String getOutputDir() {
		return outputDir;
	}
	/**
	 * @param outputDir La valeur de outputDir à fixer.
	 */
	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}
	/**
	 * @return Retourne skinDir.
	 */
	public String getSkinDir() {
		return skinDir;
	}
	/**
	 * @param skinDir La valeur de skinDir à fixer.
	 */
	public void setSkinDir(String skinDir) {
		this.skinDir = skinDir;
	}
}
