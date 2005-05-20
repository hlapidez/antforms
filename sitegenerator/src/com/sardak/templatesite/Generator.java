/*
 * Créé le 31 déc. 2004
 */
package com.sardak.templatesite;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * @author 1699016
 */
public class Generator {
	private static final boolean VERBOSE = true;
	private static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private static void log(String message) {
		if (VERBOSE) {
			Date date = new Date();
			System.out.println(formatter.format(date)+": "+message);
		}		
	}
	
	private static String titleOfHref(String reference, List menus) {
		String local = "";
		for (Iterator j=menus.listIterator();j.hasNext();) {
			Element menu = (Element) j.next();
			for (Iterator i=menu.getChildren("item").iterator();i.hasNext();) {
				Element item = (Element) i.next();			
				String href = item.getAttributeValue("href");
				String title = item.getAttributeValue("title");
				if (href.equals(reference)) {
					local = title;
				}
			}
		}
		return local;
	}
	
	private static String getMenu(String inputDir, Element menu, String title, String skinDir) throws IOException, JDOMException{		
		String menuTemplate = Personalizer.fileContent(new File(skinDir+File.separator+"transform"+File.separator+"menu.template.html"));
		String menuItemTemplate = Personalizer.fileContent(new File(skinDir+File.separator+"transform"+File.separator+"menu_items.template.html"));		
		SAXBuilder builder = new SAXBuilder();		
		StringBuffer menuItemsBuffer = new StringBuffer();
		for (Iterator i=menu.getChildren("item").iterator();i.hasNext();) {
			Element item = (Element) i.next();
			String label = item.getAttributeValue("label");
			String href = item.getAttributeValue("href");
			log("item: "+label+" points to "+href);
			String itemPersonalisation = Personalizer.personalize(menuItemTemplate, "item_name", label);			
			itemPersonalisation = Personalizer.personalize(itemPersonalisation, "item_file", href);			
			menuItemsBuffer.append(itemPersonalisation);
			if (i.hasNext()){
				menuItemsBuffer.append("\n");
			}
		}
		String menuString = Personalizer.personalize(menuTemplate, "menu_items", menuItemsBuffer.toString());
		menuString = Personalizer.personalize(menuString, "menu_title", title);
		return menuString;
	}
	
	private static final void copyResources(String skinDir, String outputDir) throws IOException{
		File transformDir = new File(skinDir+File.separator+"transform");
		File[] dirs = transformDir.listFiles();
		for (int i = 0; i < dirs.length; i++) {
			File dir = dirs[i];
			if (dir.isDirectory()) {
				File outputDirectory  = new File(outputDir+File.separator+dir.getName());
				outputDirectory.mkdirs();
				File[] files = dir.listFiles();
				for (int j = 0; j < files.length; j++) {
					File aFile = files[j];
					if (aFile.isFile()) {
						byte[] bytes = Personalizer.byteContent(aFile);
						FileOutputStream output = new FileOutputStream(outputDirectory+File.separator+aFile.getName());
						output.write(bytes);
						output.flush();
						output.close();
					}
				}
			}
		}
	}
	
	/**
	 * get the menu buffer for navigation
	 * @throws JDOMException
	 * @throws IOException
	 */
	private static final String menuBuffer(List menus, String inputDir, String skinDir) throws IOException, JDOMException
		{
		StringBuffer menuBuffer = new StringBuffer();
		for (Iterator it=menus.iterator();it.hasNext();){						
			Element menu = (Element) it.next();
			String menuTitle = menu.getAttributeValue("title");
			String menuString = getMenu(inputDir, menu,menuTitle,skinDir);
			menuBuffer.append(menuString);
			menuBuffer.append("\n");
		}
		return menuBuffer.toString();		
	}
	
	/**
	 * Generate website
	 */
	public static void generate(String inputDir, String outputDir, String skinDir) throws JDOMException, IOException {
		log("Output directory: "+outputDir);		
		File outputDirectory = new File(outputDir);
		outputDirectory.mkdirs();			
		File[] existing =  outputDirectory.listFiles();
		for (int i = 0; i < existing.length; i++) {
			File toErase = existing[i];
			if (toErase.isFile()) {
				toErase.delete();
			}
		}		
		File navigation = new File(inputDir+File.separator+"nav.xml");
		File propertiesFile = new File(inputDir+File.separator+"generation.properties");		
		Properties properties = new Properties();		
		properties.load(new FileInputStream(propertiesFile));
		formatter = new SimpleDateFormat(properties.getProperty("date.format", "dd/MMyyyy"));
		String author =  properties.getProperty("author", "author?");
		String email = properties.getProperty("author.email", "author.email?");
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(navigation);
		Element root = doc.getRootElement();
		List menus = root.getChildren("menu");
		List rightMenus = root.getChildren("menu-right");		
		String menusString = menuBuffer(menus, inputDir, skinDir);
		String menusRightString = menuBuffer(rightMenus, inputDir, skinDir);
		String documentTemplate = Personalizer.fileContent(new File(skinDir+File.separator+"transform"+File.separator+"document.template.html"));
		documentTemplate = Personalizer.personalize(documentTemplate, "menus", menusString);					
		documentTemplate = Personalizer.personalize(documentTemplate, "menus-right", menusRightString);
		File contentDir = new File(inputDir+File.separator+"content");
		File[] pages=contentDir.listFiles();
		String now = formatter.format(new Date());			
		for (int i = 0; i < pages.length; i++) {
			File page = pages[i];
			log("Page to generate: "+page.getName());
			String content = Personalizer.fileContent(page);
			String pageName = page.getName();
			String title = titleOfHref(pageName, menus);
			if (title==null) {
				title = titleOfHref(pageName, rightMenus);
			}
			log("page title: "+title);							
			content = Personalizer.personalize(documentTemplate, "content", content);
			content = Personalizer.personalize(content, "document_title", title);
			content = Personalizer.personalize(content, "date", now);
			content = Personalizer.personalize(content, "author", author);
			content = Personalizer.personalize(content, "author.email", email);
			FileOutputStream output = new FileOutputStream(outputDir+File.separator+pageName);
			output.write(content.getBytes());
			output.flush();
			output.close();
		}
		
		copyResources(skinDir, outputDir);
	}
	
	public static void main(String[] args) {
		try {
			generate("e:/web/data", "e:/web/data/site", "e:/web/data");
		} catch (Exception e) {
			e.printStackTrace();
		}
}
}
