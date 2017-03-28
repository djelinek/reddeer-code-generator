package org.jboss.reddeer.codegen.rules.simple;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtbot.generator.framework.WidgetUtils;
import org.jboss.reddeer.codegen.CodeGen;
import org.jboss.reddeer.codegen.builder.MethodBuilder;
import org.jboss.reddeer.swt.generator.framework.rules.RedDeerUtils;
import org.jboss.reddeer.swt.generator.framework.rules.simple.ButtonRule;

/**
 * 
 * @author djelinek
 */
public class ButtonCodeGenRule extends ButtonRule implements CodeGen {

	private String suffix;

	@Override
	public boolean appliesTo(Event event) {
		event.type = SWT.Selection;
		return super.appliesTo(event);
	}

	@Override
	public MethodBuilder constructor(Control control) {
		String label = getText();
		if (label == null || label.isEmpty()) {
			label = String.valueOf(getIndex());
		} else {
			label = "\"" + label + "\"";
		}
		String type = getType();
		String ref = RedDeerUtils.getReferencedCompositeString(getComposites());
		if (!ref.isEmpty())
			suffix = suffix + "group";
		return MethodBuilder.method().returnType(type).get(label + suffix)
				.returnCommand("new " + type + "(" + ref + WidgetUtils.cleanText(label) + ")");
	}

	public MethodBuilder action(Control control) {
		String label = getText();
		if (label == null || label.isEmpty()) {
			label = String.valueOf(getIndex());
		} else {
			label = "\"" + label + "\"";
		}
		String comm = getCommand("btn");
		String actionText = comm.substring(comm.lastIndexOf("."), comm.lastIndexOf("("));
		String ref = RedDeerUtils.getReferencedCompositeString(getComposites());
		if (!ref.isEmpty())
			suffix = suffix + "group";
		if (actionText.equals(".toggle"))
			return MethodBuilder.method().name(actionText + " " + WidgetUtils.cleanText(label) + suffix)
					.parameter("boolean choice").command(comm);
		else
			return MethodBuilder.method().name(actionText + " " + WidgetUtils.cleanText(label) + suffix).command(comm);
	}

	public MethodBuilder get(Control control) {
		String label = getText();
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
		forReturn.add(get(control));
		forReturn.add(action(control));
		return forReturn;
	}

	public String getType() {
		String type = null;
		int style = getStyle();
		if ((style & SWT.PUSH) != 0) {
			type = "PushButton";
			suffix = "BTN";
		} else if ((style & SWT.CHECK) != 0) {
			type = "CheckBox";
			suffix = "CHB";
		} else if ((style & SWT.ARROW) != 0) {
			type = "ArrowButton";
			suffix = "ARR";
		} else if ((style & SWT.RADIO) != 0) {
			type = "RadioButton";
			suffix = "RDB";
		} else if ((style & SWT.TOGGLE) != 0) {
			type = "ToggleButton";
			suffix = "TGB";
		}
		if (type == null) {
			throw new IllegalArgumentException("Unsupported button style " + style);
		}
		return type;
	}

	public String getCommand(String type) {
		StringBuilder builder = new StringBuilder("new " + getType() + "(");
		builder.append(RedDeerUtils.getReferencedCompositeString(getComposites()));
		String text = getText();
		int index = getIndex();
		if (text == null || text.isEmpty()) {
			builder.append(index);
		} else {
			builder.append("\"" + WidgetUtils.cleanText(text) + "\"");
		}
		builder.append(")");
		if (type.equals("btn"))
			if ((getStyle() & SWT.CHECK) != 0) {
				builder.append(".toggle(choice)");
			} else {
				builder.append(".click()");
			}
		else if (type.equals("get")) {
			builder.append(".getText()");
		}
		return builder.toString();
	}

}
