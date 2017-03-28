package org.jboss.reddeer.codegen.rules.simple;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtbot.generator.framework.WidgetUtils;
import org.jboss.reddeer.codegen.CodeGen;
import org.jboss.reddeer.codegen.builder.MethodBuilder;
import org.jboss.reddeer.swt.generator.framework.rules.RedDeerUtils;
import org.jboss.reddeer.swt.generator.framework.rules.simple.ComboRule;

/**
 * 
 * @author djelinek
 */
public class ComboCodeGenRule extends ComboRule implements CodeGen {

	private String suffix = "CMB";

	@Override
	public boolean appliesTo(Event event) {
		event.type = SWT.Modify;
		if (event.widget instanceof Combo)
			try {
				if (!WidgetUtils.cleanText(WidgetUtils.getLabel((Combo) event.widget)).isEmpty())
					return true;
				else
					return false;
			} catch (Exception e) {
				return false;
			}
		else
			return false;
	}

	@Override
	public MethodBuilder constructor(Control control) {
		String type = "LabeledCombo";
		String label = getLabel();
		if (label == null || label.isEmpty()) {
			type = "DefaultCombo";
			label = String.valueOf(getIndex());
		} else {
			label = "\"" + label + "\"";
		}
		String ref = RedDeerUtils.getReferencedCompositeString(getComposites());
		return MethodBuilder.method().returnType(type).get(label + suffix)
				.returnCommand("new " + type + "(" + ref + WidgetUtils.cleanText(label) + ")");
	}

	/**
	 * new LabeledCCombo(""). getSelection setSelection getText getItems
	 */

	@Override
	public List<MethodBuilder> getActionMethods(Control control) {
		List<MethodBuilder> forReturn = new ArrayList<>();
		forReturn.add(constructor(control));
		return forReturn;
	}

}
