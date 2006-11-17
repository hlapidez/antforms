package com.sardak.antform.test;

import javax.swing.UIManager;

import com.sardak.antform.AntForm;
import com.sardak.antform.types.BooleanProperty;
import com.sardak.antform.types.Button;
import com.sardak.antform.types.ButtonBar;
import com.sardak.antform.types.Label;
import com.sardak.antform.types.Separator;
import com.sardak.antform.types.TextProperty;
import com.sardak.antform.util.ActionType;

public class SimpleTest extends AntForm {
	public static void main(String[] args) {
		new SimpleTest();
	}
	
	public SimpleTest() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PseudoProject project = new PseudoProject();
		
		init();
		setProject(project);
		setTitle("Simple test");

		Label label = new Label();
		label.setProject(project);
		label.addText("This is a simple label");
		addConfiguredLabel(label);
		
		ButtonBar bar = new ButtonBar();
		bar.setProject(project);
		bar.addConfiguredButton(new Button("Ok button", null, ActionType.OK));
		bar.addConfiguredButton(new Button("Cancel button", null, ActionType.CANCEL));
		bar.addConfiguredButton(new Button("Reset button", null, ActionType.RESET));
		addConfiguredButtonBar(bar);
		
		addConfiguredSeparator(new Separator());
		
		BooleanProperty bp = new BooleanProperty();
		bp.setProject(project);
		bp.setLabel("booleanproperty");
		bp.setProperty("booleanproperty");
		bp.setEditable(true);
		addConfiguredBooleanProperty(bp);
		
		TextProperty tp = new TextProperty();
		tp.setProject(project);
		tp.setLabel("textproperty");
		tp.setProperty("textproperty");
		tp.setColumns(30);
		tp.setEditable(true);
		tp.setPassword(false);
		tp.setRequired(false);
		addConfiguredTextProperty(tp);

		ButtonBar controlBar = new ButtonBar();
		controlBar.setProject(project);
		controlBar.addConfiguredButton(new Button("Save properties", null, ActionType.OK));
		controlBar.addConfiguredButton(new Button("Reset form", null, ActionType.RESET));
		addConfiguredControlBar(controlBar);

		execute();

		System.out.println(project.getProperties());
		System.exit(0);
	}
}
