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
	public MethodBuilder get(Control control) {
		String label = getLabel();
		if (label == null || label.isEmpty()) {
			label = String.valueOf(getIndex());
		} else {
			label = "\"" + label + "\"";
		}
		return MethodBuilder.method().returnType("String").get("Text" + label).command(getCommand("get"));
	}

	public MethodBuilder getSelection(Control control) {
		String label = getLabel();
		if (label == null || label.isEmpty()) {
			label = String.valueOf(getIndex());
		} else {
			label = "\"" + label + "\"";
		}
		return MethodBuilder.method().returnType("String").get("Selection" + label).command(getCommand("getSelection"));
	}

	public MethodBuilder getItems(Control control) {
		String label = getLabel();
		if (label == null || label.isEmpty()) {
			label = String.valueOf(getIndex());
		} else {
			label = "\"" + label + "\"";
		}
		return MethodBuilder.method().returnType("List<String>").get("Items" + label).command(getCommand("items"));
	}

	public MethodBuilder setSelection(Control control) {
		String label = getLabel();
		if (label == null || label.isEmpty()) {
			label = String.valueOf(getIndex());
		} else {
			label = "\"" + label + "\"";
		}
		return MethodBuilder.method().name("setSelection " + label).parameter("String str")
				.command(getCommand("setSelection"));
	}

	@Override
	public List<MethodBuilder> getActionMethods(Control control) {
		List<MethodBuilder> forReturn = new ArrayList<>();
		forReturn.add(constructor(control));
		forReturn.add(get(control));
		forReturn.add(getSelection(control));
		forReturn.add(getItems(control));
		forReturn.add(setSelection(control));
		return forReturn;
	}

	public String getCommand(String type) {
		StringBuffer sb = new StringBuffer();
		String label = getLabel();
		if (label != null) {
			sb.append("new LabeledCombo(");
			sb.append(RedDeerUtils.getReferencedCompositeString(getComposites()));
			sb.append("\"" + WidgetUtils.cleanText(label) + "\"");
		} else {
			sb.append("new DefaultCombo(");
			sb.append(RedDeerUtils.getReferencedCompositeString(getComposites()));
			sb.append(getIndex());
		}
		if (type.equals("setSelection"))
			sb.append(").setSelection(str)");
		else if (type.equals("get"))
			sb.append(").getText()");
		else if (type.equals("getSelection"))
			sb.append(").getSelection()");
		else if (type.equals("items"))
			sb.append(").getItems()");
		return sb.toString();
	}

}
