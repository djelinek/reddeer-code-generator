package org.jboss.reddeer.codegen.rules.simple;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.jboss.reddeer.codegen.CodeGen;
import org.jboss.reddeer.codegen.builder.MethodBuilder;
import org.jboss.reddeer.swt.generator.framework.rules.simple.ToolBarRule;

/**
 * 
 * @author djelinek
 */
public class ToolBarCodeGenRule extends ToolBarRule implements CodeGen {

	@Override
	public boolean appliesTo(Event event) {
		event.type = SWT.Selection;
		return super.appliesTo(event);
	}

	@Override
	public MethodBuilder constructor(Control control) {
		String returnType = "ToolItem";
		int type = getType();
		if (type == WORKBENCH) {
			returnType = "WorkbenchToolItem";
		} else if (type == VIEW) {
			returnType = "ViewToolItem";
		} else if (type == SHELL) {
			returnType = "ShellToolItem";
		}
		return MethodBuilder.method().get(getToolTipText()).returnType(returnType).returnCommand(getActions().get(0));
	}

	@Override
	public List<MethodBuilder> getActionMethods(Control control) {
		List<MethodBuilder> forReturn = new ArrayList<>();
		forReturn.add(constructor(control));
		return forReturn;
	}

}
