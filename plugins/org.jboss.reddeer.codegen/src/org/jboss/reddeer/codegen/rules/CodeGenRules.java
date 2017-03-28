package org.jboss.reddeer.codegen.rules;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.generator.framework.AnnotationRule;
import org.eclipse.swtbot.generator.framework.GenerationComplexRule;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.eclipse.swtbot.generator.framework.Generator;
import org.jboss.reddeer.codegen.rules.simple.ButtonCodeGenRule;
import org.jboss.reddeer.codegen.rules.simple.ComboCodeGenRule;
import org.jboss.reddeer.codegen.rules.simple.ShellCodeGenRule;
import org.jboss.reddeer.codegen.rules.simple.TextCodeGenRule;

/**
 * 
 * @author djelinek
 */
public class CodeGenRules implements Generator {

	@Override
	public List<GenerationSimpleRule> createSimpleRules() {
		List<GenerationSimpleRule> rules = new ArrayList<GenerationSimpleRule>();
		rules.add(new ButtonCodeGenRule());
		rules.add(new TextCodeGenRule());
		rules.add(new ComboCodeGenRule());
		rules.add(new ShellCodeGenRule());
		return rules;
	}

	public Image getImage() {
		InputStream is = getClass().getResourceAsStream("/icons/reddeer_logo.png");
		Image image = new Image(Display.getCurrent(), is);
		return image;
	}

	public String getLabel() {
		return "RedDeer CodeGen SWT";
	}

	@Override
	public List<AnnotationRule> createAnnotationRules() {
		return null;
	}

	@Override
	public List<GenerationComplexRule> createComplexRules() {
		return null;
	}
}
