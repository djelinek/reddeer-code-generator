package org.jboss.reddeer.codegen;

import java.util.List;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.eclipse.ui.internal.dialogs.WorkbenchPreferenceDialog;
import org.hamcrest.core.IsInstanceOf;
import org.jboss.reddeer.codegen.builder.ClassBuilder;
import org.jboss.reddeer.codegen.builder.MethodBuilder;
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
@SuppressWarnings("restriction")
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
		Object o = sh[sh.length - 2].getData();
		if(o instanceof WizardDialog) {
			classBuilder.setExtendedClass("NewWizardDialog");
			return ((WizardDialog)o).getCurrentPage().getControl();
		}
		else if(o instanceof WorkbenchPreferenceDialog) {
			classBuilder.setExtendedClass("PreferenceDialog");
			return ((WorkbenchPreferenceDialog)o).getCurrentPage().getControl();
		}
		else
			return c[0];
	}

	public ClassBuilder generateCode() {

		classBuilder.addOptionals(options);
		List<Control> controls = controlFinder.find(getControl(), new IsInstanceOf(Control.class));
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
					for (MethodBuilder meth : ((ButtonCodeGenRule) rule).getActionMethods(control)) {
						if (options.contains(meth.getMethodType()))
							classBuilder.addMethod(meth);
					}
				} else if (rule instanceof TextCodeGenRule) {
					for (MethodBuilder meth : ((TextCodeGenRule) rule).getActionMethods(control)) {
						if (options.contains(meth.getMethodType()))
							classBuilder.addMethod(meth);
					}
				} else if (rule instanceof ComboCodeGenRule) {
					for (MethodBuilder meth : ((ComboCodeGenRule) rule).getActionMethods(control)) {
						if (options.contains(meth.getMethodType()))
							classBuilder.addMethod(meth);
					}
				} else if (rule instanceof ShellCodeGenRule) {
					for (MethodBuilder meth : ((ShellCodeGenRule) rule).getActionMethods(control)) {
						if (options.contains(meth.getMethodType()))
							classBuilder.addMethod(meth);
					}
				} else if (rule instanceof ToolBarCodeGenRule) {
					for (MethodBuilder meth : ((ToolBarCodeGenRule) rule).getActionMethods(control)) {
						if (options.contains(meth.getMethodType()))
							classBuilder.addMethod(meth);
					}
				}

			}

		}
		return classBuilder;
	}
}
