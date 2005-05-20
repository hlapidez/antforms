package com.sardak.antform.test;

import javax.swing.UIManager;

import com.sardak.antform.gui.Control;
import com.sardak.antform.gui.ControlPanel;
import com.sardak.antform.interfaces.ValueHandle;

/**
 * @author René Ghosh
 * 29 mars 2005
 */
public class MultiSelectTest {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Control control = new Control(new CallbackTest(), "Tree component test", null, false);
		ControlPanel panel = control.getPanel();		
		ValueHandle handle = panel.addMultiCheckProperty(
				"a multiselect", "prop" , 
				new String[]{"element1","element2","element3","element4"},
				",", "\\",true);
		panel.addButtonControls("ok", "reset");
		handle.setValue("element1,element4");
		control.show();
		System.out.println(handle.getValue());
		System.exit(0);
	}
}
