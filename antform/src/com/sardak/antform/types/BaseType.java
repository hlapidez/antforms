/***************************************************************************\*
 *                                                                            *
 *    AntForm form-based interaction for Ant scripts                          *
 *    Copyright (C) 2005 René Ghosh                                           *
 *                                                                            *
 *   This library is free software; you can redistribute it and/or modify it  *
 *   under the terms of the GNU Lesser General Public License as published by *
 *   the Free Software Foundation; either version 2.1 of the License, or (at  *
 *   your option) any later version.                                          *
 *                                                                            *
 *   This library is distributed in the hope that it will be useful, but      *
 *   WITHOUT ANY WARRANTY; without even the implied warranty of               *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser  *
 *   General Public License for more details.                                 *
 *                                                                            *
 *   You should have received a copy of the GNU Lesser General Public License *
 *   along with this library; if not, write to the Free Software Foundation,  *
 *   Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA              *
 \****************************************************************************/
package com.sardak.antform.types;

import org.apache.tools.ant.Project;

import com.sardak.antform.gui.ControlPanel;
import com.sardak.antform.interfaces.ValueHandle;

/**
 * @author René Ghosh 1 avr. 2005
 */
public abstract class BaseType {
	private String If, unless;

	public String getIf() {
		return If;
	}

	public void setIf(String if1) {
		If = if1;
	}

	public String getUnless() {
		return unless;
	}

	public void setUnless(String unless) {
		this.unless = unless;
	}

	public boolean shouldBeDisplayed(Project project) {
		boolean shouldBeDisplayed = true;
		if (getIf() != null) {
			if (!project.getProperties().containsKey(getIf())) {
				shouldBeDisplayed = false;
			}
		}
		if (getUnless() != null) {
			if (project.getProperties().containsKey(getUnless())) {
				shouldBeDisplayed = false;
			}
		}
		return shouldBeDisplayed;
	}

	public abstract ValueHandle addToControlPanel(ControlPanel panel);
}
