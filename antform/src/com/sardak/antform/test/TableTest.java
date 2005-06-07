package com.sardak.antform.test;

import javax.swing.UIManager;

import com.sardak.antform.gui.Control;
import com.sardak.antform.gui.ControlPanel;

/**
 * @author René Ghosh
 * 1 avr. 2005
 */
public class TableTest {
public static void main(String[] args) {
	try {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	Control control = new Control(new CallbackTest(), "Table test", null, false);
	ControlPanel panel = control.getPanel();
	panel.addTableProperty("a table", "prop", true, new String[]{"col1", "col2", "col3"},
			new String[][]{{"d1", "d2", "d3"},{"d1", "d2", "d3"},{"d1", "d2", "d3"}},
			";", ",", "\\", -1, -1,-1,false, null);
	control.show();
	System.exit(0);
}
}
