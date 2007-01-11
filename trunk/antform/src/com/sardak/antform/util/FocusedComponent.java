package com.sardak.antform.util;

import javax.swing.JComponent;

public class FocusedComponent {
	private int tabIndex = -1;
	private JComponent component = null;
	
	public FocusedComponent(int tabIndex, JComponent component) {
		this.tabIndex = tabIndex;
		this.component = component;
	}

	public JComponent getComponent() {
		return component;
	}

	public void setComponent(JComponent component) {
		this.component = component;
	}

	public int getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(int tabIndex) {
		this.tabIndex = tabIndex;
	}
}
