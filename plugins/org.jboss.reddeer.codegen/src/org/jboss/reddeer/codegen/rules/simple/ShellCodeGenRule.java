package org.jboss.reddeer.codegen.rules.simple;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.jboss.reddeer.codegen.CodeGen;
import org.jboss.reddeer.codegen.builder.MethodBuilder;
import org.jboss.reddeer.codegen.wizards.MethodsPage;
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
		String title = "\"" + getShellTitle() + "\"";
		return MethodBuilder.method().returnType("DefaultShell").name("activate")
				.returnCommand("new DefaultShell(" + title + ")").type(MethodsPage.CONSTRUCTOR);
	}

	@Override
	public MethodBuilder get(Control control) {
		String title = "\"" + getShellTitle() + "\"";
		return MethodBuilder.method().returnType("String").get("Text" + title)
				.returnCommand("new DefaultShell(" + title + ").getText()").type(MethodsPage.GETTER);
	}

	@Override
	public List<MethodBuilder> getActionMethods(Control control) {
		List<MethodBuilder> forReturn = new ArrayList<>();
		forReturn.add(constructor(control));
		forReturn.add(get(control));
		return forReturn;
	}

}
