package org.jboss.reddeer.codegen.rules;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.jboss.reddeer.codegen.rules.simple.ButtonCodeGenRule;
import org.jboss.reddeer.codegen.rules.simple.ComboCodeGenRule;
import org.jboss.reddeer.codegen.rules.simple.ShellCodeGenRule;
import org.jboss.reddeer.codegen.rules.simple.TextCodeGenRule;
import org.jboss.reddeer.swt.generator.framework.rules.RedDeerSWTGeneratorRules;

/**
 * 
 * @author djelinek
 */
public class CodeGenRules extends RedDeerSWTGeneratorRules {

	@Override
	public List<GenerationSimpleRule> createSimpleRules() {
		List<GenerationSimpleRule> rules = new ArrayList<GenerationSimpleRule>();
		rules.add(new ButtonCodeGenRule());
		rules.add(new TextCodeGenRule());
		rules.add(new ComboCodeGenRule());
		rules.add(new ShellCodeGenRule());
		return rules;
	}

	public String getLabel() {
		return "RedDeer CodeGen SWT";
	}
}
