package org.jboss.reddeer.codegen;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swtbot.generator.framework.WidgetUtils;
import org.jboss.reddeer.codegen.builder.MethodBuilder;
import org.jboss.reddeer.core.handler.WidgetHandler;

public class ButtonCodeGen implements CodeGen {

	@Override
	public boolean isSupported(Control control) {
		return control instanceof Button;
	}

	@Override
	public MethodBuilder getConstructor(Control control) {
		if (!isSupported(control)) {
			throw new IllegalArgumentException("Given " + control.getClass() + "is not supported");
		}
		Button button = (Button) control;
		String label = WidgetHandler.getInstance().getText(button);
		String type = null;
		int style = button.getStyle();
		if ((style & SWT.PUSH) != 0) {
			type = "PushButton";
		} else if ((style & SWT.CHECK) != 0) {
			type = "CheckBox";
		} else if ((style & SWT.ARROW) != 0) {
			type = "ArrowButton";
		} else if ((style & SWT.RADIO) != 0) {
			type = "RadioButton";
		} else if ((style & SWT.TOGGLE) != 0) {
			type = "ToggleButton";
		}
		if (type == null) {
			throw new IllegalArgumentException("Unsupported button style " + style);
		}
		if (label == null || label.isEmpty()) {
			label = String.valueOf(WidgetUtils.getIndex(button));
		} else {
			label = "\"" + label + "\"";
		}
	
		String ref = ""; // RedDeerUtils.getReferencedCompositeString(RedDeerUtils.getComposites(button));
		return MethodBuilder.method().returnType(type).get(label).returnCommand("new " + type + "(" + ref + WidgetUtils.cleanText(label) + ")");
	}
	
	public MethodBuilder click(Control control) {
		if (!isSupported(control)) {
			throw new IllegalArgumentException("Given " + control.getClass() + "is not supported");
		}
		Button button = (Button) control;
		String label = WidgetHandler.getInstance().getText(button);
		String type = null;
		int style = button.getStyle();
		if ((style & SWT.PUSH) != 0) {
			type = "PushButton";
		} 
		if (type == null) {
			throw new IllegalArgumentException("Unsupported button style " + style);
		}
		if (label == null || label.isEmpty()) {
			label = String.valueOf(WidgetUtils.getIndex(button));
		} else {
			label = "\"" + label + "\"";
		}
		
		String ref = ""; // RedDeerUtils.getReferencedCompositeString(RedDeerUtils.getComposites(button));
		return MethodBuilder.method().name("click" + label).command("new " + type + "(" + ref + WidgetUtils.cleanText(label) + ").click()");
	}

	@Override
	public List<MethodBuilder> getGeneratedMethods(Control control) {
		
		List<MethodBuilder> m = new ArrayList<MethodBuilder>();
		m.add(getConstructor(control));
		try {
			m.add(click(control));			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return m; 
	}

}
