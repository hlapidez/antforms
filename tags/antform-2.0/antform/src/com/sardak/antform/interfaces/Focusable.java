package com.sardak.antform.interfaces;

import javax.swing.JComponent;

public interface Focusable {
	public boolean isFocus();
	// tells the component to request the focus.
	public JComponent getFocusableComponent();
}
