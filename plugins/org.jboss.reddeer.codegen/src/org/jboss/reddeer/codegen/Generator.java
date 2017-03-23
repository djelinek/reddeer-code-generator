package org.jboss.reddeer.codegen;

import java.util.List;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.hamcrest.core.IsInstanceOf;
import org.jboss.reddeer.codegen.builder.ClassBuilder;
import org.jboss.reddeer.codegen.finder.ControlFinder;
import org.jboss.reddeer.codegen.rules.CodeGenRules;
import org.jboss.reddeer.codegen.rules.simple.ButtonCodeGenRule;
import org.jboss.reddeer.codegen.rules.simple.ComboCodeGenRule;
import org.jboss.reddeer.codegen.rules.simple.ShellCodeGenRule;
import org.jboss.reddeer.codegen.rules.simple.TextCodeGenRule;
import org.jboss.reddeer.codegen.rules.simple.ToolBarCodeGenRule;
import org.jboss.reddeer.core.lookup.ShellLookup;

/**
 * 
 * @author djelinek
 */
public class Generator {

	private ControlFinder controlFinder;
	private ClassBuilder classBuilder;
	private List<String> options;

	public Generator(String className, String packageName, List<String> optional) {
		controlFinder = new ControlFinder();
		classBuilder = new ClassBuilder();
		this.classBuilder.setClassName(className);
		this.classBuilder.setPackage(packageName);
		this.options = optional;
	}

	public Control getControl() {
		Shell[] sh = ShellLookup.getInstance().getShells();
		Control[] c = sh[sh.length - 2].getChildren();
		return c[0];
	}

	public ClassBuilder generateCode(Control parentControl) {

		classBuilder.addOptionals(options);
		List<Control> controls = controlFinder.find(parentControl, new IsInstanceOf(Control.class));
		List<GenerationSimpleRule> simples = new CodeGenRules().createSimpleRules();
		Event e = new Event();
		for (Control control : controls) {
			e.widget = control;
			for (GenerationSimpleRule rule : simples) {
				if (!rule.appliesTo(e)) {
					continue;
				}
				rule.initializeForEvent(e);
				for (String im : rule.getImports()) {
					classBuilder.addImport(im);
				}
				if (rule instanceof ButtonCodeGenRule) {
					classBuilder.addMethod(((ButtonCodeGenRule) rule).constructor(control));
					classBuilder.addMethods(((ButtonCodeGenRule) rule).getActionMethods(control));
				} else if (rule instanceof TextCodeGenRule) {
					classBuilder.addMethod(((TextCodeGenRule) rule).constructor(control));
					classBuilder.addMethods(((TextCodeGenRule) rule).getActionMethods(control));
				} else if (rule instanceof ComboCodeGenRule) {
					classBuilder.addMethod(((ComboCodeGenRule) rule).constructor(control));
				} else if (rule instanceof ShellCodeGenRule) {
					classBuilder.addMethod(((ShellCodeGenRule) rule).constructor(control));
				} else if (rule instanceof ToolBarCodeGenRule) {
					classBuilder.addMethod(((ToolBarCodeGenRule) rule).constructor(control));
				}

			}

		}
		return classBuilder;
	}
}
