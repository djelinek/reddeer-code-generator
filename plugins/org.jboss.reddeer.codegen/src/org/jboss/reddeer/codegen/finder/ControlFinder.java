package org.jboss.reddeer.codegen.finder;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * 
 * @author djelinek
 */
public class ControlFinder extends Finder<Control> {

	@Override
	public List<Control> getChildren(Control child) {
		List<Control> children = new ArrayList<Control>();
		if (child instanceof Composite) {
			Control[] controls = ((Composite) child).getChildren();
			for (Control control : controls) {
				children.add(control);
			}
		}
		return children;
	}
}
