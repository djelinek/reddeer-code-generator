package reddeer.codegen;

import java.util.List;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.eclipse.ui.internal.dialogs.WorkbenchPreferenceDialog;
import org.hamcrest.core.IsInstanceOf;
import reddeer.codegen.builder.ClassBuilder;
import reddeer.codegen.builder.MethodBuilder;
import reddeer.codegen.finder.ControlFinder;
import reddeer.codegen.rules.CodeGenRules;
import reddeer.codegen.rules.simple.ButtonCodeGenRule;
import reddeer.codegen.rules.simple.ComboCodeGenRule;
import reddeer.codegen.rules.simple.ShellCodeGenRule;
import reddeer.codegen.rules.simple.TextCodeGenRule;
import reddeer.codegen.wizards.MethodsPage;
import org.eclipse.reddeer.core.lookup.ShellLookup;

/**
 * Class for generating source code for all supported widget rules. This class
 * is also looking for parent control.
 * 
 * @author djelinek
 */
@SuppressWarnings("restriction")
public class CodeGenerator {

	private ControlFinder controlFinder;
	private ClassBuilder classBuilder;
	private List<String> options;

	public static final String WIZARD_DIALOG = "WizardDialog";
	public static final String WIZARD_DIALOG_IMPORT = "org.eclipse.reddeer.jface.wizard.WizardDialog";
	public static final String PREFERENCE_DIALOG = "PreferenceDialog";
	public static final String PREFERENCE_DIALOG_IMPORT = "org.eclipse.reddeer.jface.wizard.PreferenceDialog";

	/**
	 * Default Code Generator constructor
	 * 
	 * @param className
	 * @param packageName
	 * @param optional
	 *            list of checked optional properties from second wizard page
	 */
	public CodeGenerator(String className, String packageName, List<String> optional) {
		controlFinder = new ControlFinder();
		classBuilder = new ClassBuilder();
		this.classBuilder.setClassName(className);
		this.classBuilder.setPackage(packageName);
		this.options = optional;
	}

	/**
	 * This method is looking for first shell under Code Generator wizard
	 * 
	 * @return shell control
	 */
	public Control getShellControl() {
		Shell[] sh = ShellLookup.getInstance().getShells();
		return sh[sh.length - 2];
	}

	/**
	 * This method is looking for parent Control of first wizard under Code
	 * Generator wizard
	 * 
	 * @return parent wizard Control
	 */
	public Control getControl() {
		Shell[] sh = ShellLookup.getInstance().getShells();
		Control[] c = sh[sh.length - 2].getChildren();
		Object o = sh[sh.length - 2].getData();
		if (!options.contains(MethodsPage.INCLUDE_ALL) && !options.contains(MethodsPage.INHERITING)) {
			if (o instanceof WizardDialog) {
				return ((WizardDialog) o).getCurrentPage().getControl();
			} else if (o instanceof WorkbenchPreferenceDialog) {
				return ((WorkbenchPreferenceDialog) o).getCurrentPage().getControl();
			} else
				return c[0];
		} else {
			if (options.contains(MethodsPage.INHERITING)) {
				if (o instanceof WizardDialog) {
					classBuilder.setExtendedClass(WIZARD_DIALOG);
				} else if (o instanceof WorkbenchPreferenceDialog) {
					classBuilder.setExtendedClass(PREFERENCE_DIALOG);
				}
			}
			return c[0];
		}
	}

	/**
	 * Generates code (methods, imports, etc.) for all supported widgets at
	 * found Control.
	 * 
	 * @return ClassBuilder instance
	 */
	public ClassBuilder generateCode() {
		classBuilder.addOptions(options);
		classBuilder.clearImports();
		List<Control> controls = controlFinder.find(getControl(), new IsInstanceOf(Control.class));
		controls.add(getShellControl());
		List<GenerationSimpleRule> simples = new CodeGenRules().createSimpleRules();
		Event e = new Event();
		for (Control control : controls) {
			e.widget = control;
			for (GenerationSimpleRule rule : simples) {
				if (!rule.appliesTo(e)) {
					continue;
				}
				rule.initializeForEvent(e);
				if (rule instanceof ButtonCodeGenRule) {
					for (MethodBuilder meth : ((ButtonCodeGenRule) rule).getActionMethods(control)) {
						if (options.contains(meth.getMethodType())) {
							classBuilder.addMethod(meth);
							classBuilder.addImports(rule.getImports());
						}
					}
				} else if (rule instanceof TextCodeGenRule) {
					for (MethodBuilder meth : ((TextCodeGenRule) rule).getActionMethods(control)) {
						if (options.contains(meth.getMethodType())) {
							classBuilder.addMethod(meth);
							classBuilder.addImports(rule.getImports());
						}
					}
				} else if (rule instanceof ComboCodeGenRule) {
					for (MethodBuilder meth : ((ComboCodeGenRule) rule).getActionMethods(control)) {
						if (options.contains(MethodsPage.CONSTANTS))
							classBuilder.addConstants(((ComboCodeGenRule) rule).getSelectionList(control));
						if (options.contains(meth.getMethodType())) {
							classBuilder.addMethod(meth);
							classBuilder.addImports(rule.getImports());
						}
					}
				} else if (rule instanceof ShellCodeGenRule) {
					for (MethodBuilder meth : ((ShellCodeGenRule) rule).getActionMethods(control)) {
						if (options.contains(meth.getMethodType())) {
							classBuilder.addMethod(meth);
							classBuilder.addImports(rule.getImports());
						}
					}
				}
			}
			if (classBuilder.getExtendedClass().equals(WIZARD_DIALOG) && classBuilder.isExtendible())
				classBuilder.addImport(WIZARD_DIALOG_IMPORT);
			else if (classBuilder.getExtendedClass().equals(PREFERENCE_DIALOG) && classBuilder.isExtendible())
				classBuilder.addImport(PREFERENCE_DIALOG_IMPORT);
		}
		return classBuilder;
	}
}
