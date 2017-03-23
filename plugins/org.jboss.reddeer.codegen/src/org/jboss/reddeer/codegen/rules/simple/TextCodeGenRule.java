package org.jboss.reddeer.codegen.rules.simple;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtbot.generator.framework.WidgetUtils;
import org.jboss.reddeer.codegen.CodeGen;
import org.jboss.reddeer.codegen.builder.MethodBuilder;
import org.jboss.reddeer.swt.generator.internal.framework.rules.RedDeerUtils;
import org.jboss.reddeer.swt.generator.internal.framework.rules.simple.TextRule;

/**
 * 
 * @author djelinek
 */
public class TextCodeGenRule extends TextRule implements CodeGen {

	private String suffix = "TXT";

	@Override
	public boolean appliesTo(Event event) {
		event.type = SWT.Modify;
		if (event.widget instanceof Text)
			return true;
		else
			return false;
	}

	@Override
	public MethodBuilder constructor(Control control) {
		String type = "LabeledText";
		String label = getLabel();
		if (label == null || label.isEmpty()) {
			type = "DefaultText";
			label = String.valueOf(getIndex());
		} else {
			label = "\"" + label + "\"";
		}
		String ref = RedDeerUtils.getReferencedCompositeString(RedDeerUtils.getComposites(control));
		return MethodBuilder.method().returnType(type).get(label + suffix)
				.returnCommand("new " + type + "(" + ref + WidgetUtils.cleanText(label) + ")");
	}

	public MethodBuilder set(Control control) {
		String label = getLabel();
		if (label == null || label.isEmpty()) {
			label = String.valueOf(getIndex());
		} else {
			label = "\"" + label + "\"";
		}
		return MethodBuilder.method().name("setText" + label).parameter("String str").command(getCommand("set"));
	}

	public MethodBuilder get(Control control) {
		String label = getLabel();
		if (label == null || label.isEmpty()) {
			label = String.valueOf(getIndex());
		} else {
			label = "\"" + label + "\"";
		}
		return MethodBuilder.method().returnType("String").get("Text" + label).command(getCommand("get"));
	}

	@Override
	public List<MethodBuilder> getActionMethods(Control control) {
		List<MethodBuilder> forReturn = new ArrayList<>();
		forReturn.add(constructor(control));
		forReturn.add(set(control));
		forReturn.add(get(control));
		return forReturn;
	}

	public String getCommand(String type) {
		StringBuilder builder = new StringBuilder();
		String label = getLabel();
		if (label != null) {
			builder.append("new LabeledText(");
			builder.append(RedDeerUtils.getReferencedCompositeString(getComposites()));
			builder.append("\"" + label + "\"");
		} else {
			builder.append("new DefaultText(");
			builder.append(RedDeerUtils.getReferencedCompositeString(getComposites()));
			builder.append(getIndex());
		}
		if (type.equals("set"))
			builder.append(").setText(str)");
		else if (type.equals("get"))
			builder.append(").getText()");
		return builder.toString();
	}

}
