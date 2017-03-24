package org.jboss.reddeer.codegen.rules.simple;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.generator.framework.WidgetUtils;
import org.jboss.reddeer.codegen.CodeGen;
import org.jboss.reddeer.codegen.builder.MethodBuilder;
import org.jboss.reddeer.core.lookup.ShellLookup;
import org.jboss.reddeer.swt.generator.framework.rules.simple.ShellRule;

/**
 * 
 * @author djelinek
 */
public class ShellCodeGenRule extends ShellRule implements CodeGen {

	@Override
	public boolean appliesTo(Event event) {
		return event.widget instanceof Shell;
	}

	@Override
	public MethodBuilder constructor(Control control) {
		Shell[] sh = ShellLookup.getInstance().getShells();
		String title = sh[sh.length - 2].getText();
		return MethodBuilder.method().returnType("DefaultShell").name("activate")
				.returnCommand("new DefaultShell(" + WidgetUtils.cleanText(title) + ")");
	}

	@Override
	public List<MethodBuilder> getActionMethods(Control control) {
		List<MethodBuilder> forReturn = new ArrayList<>();
		forReturn.add(constructor(control));
		return forReturn;
	}

}
