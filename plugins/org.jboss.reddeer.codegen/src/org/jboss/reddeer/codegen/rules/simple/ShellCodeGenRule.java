package org.jboss.reddeer.codegen.rules.simple;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.generator.framework.WidgetUtils;
import org.jboss.reddeer.codegen.CodeGen;
import org.jboss.reddeer.codegen.builder.MethodBuilder;
import org.jboss.reddeer.codegen.wizards.MethodsPage;
import org.eclipse.reddeer.swt.generator.framework.rules.simple.ShellRule;

/**
 * RedDeer CodeGen rules for Shell widget/control.
 * 
 * @author djelinek
 */
public class ShellCodeGenRule extends ShellRule implements CodeGen {

	@Override
	public boolean appliesTo(Event event) {
		return event.widget instanceof Shell;
	}

	/**
	 * Create constructor method
	 * 
	 * @param control
	 *            SWT widget
	 * @return MethodBuilder instance
	 */
	@Override
	public MethodBuilder constructor(Control control) {
		String title = "\"" + getShellTitle() + "\"";
		return MethodBuilder.method().returnType("DefaultShell").get("Shell" + WidgetUtils.cleanText(title))
				.returnCommand("new DefaultShell(" + title + ")").type(MethodsPage.GETTER);
	}

	/**
	 * Create getter method
	 * 
	 * @param control
	 *            SWT widget
	 * @return MethodBuilder instance
	 */
	@Override
	public MethodBuilder get(Control control) {
		String title = "\"" + getShellTitle() + "\"";
		return MethodBuilder.method().returnType("String").get("Text" + title)
				.returnCommand("new DefaultShell(" + title + ").getText()").type(MethodsPage.GETTER);
	}

	/**
	 * Returns lit of available/created methods
	 * 
	 * @param control
	 *            SWT widget
	 * @return List<MethodBuilder>
	 */
	@Override
	public List<MethodBuilder> getActionMethods(Control control) {
		List<MethodBuilder> forReturn = new ArrayList<>();
		forReturn.add(constructor(control));
		forReturn.add(get(control));
		return forReturn;
	}

}
