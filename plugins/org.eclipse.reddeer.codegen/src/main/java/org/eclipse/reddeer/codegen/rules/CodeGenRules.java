package org.eclipse.reddeer.codegen.rules;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.eclipse.reddeer.codegen.rules.simple.ButtonCodeGenRule;
import org.eclipse.reddeer.codegen.rules.simple.ComboCodeGenRule;
import org.eclipse.reddeer.codegen.rules.simple.ShellCodeGenRule;
import org.eclipse.reddeer.codegen.rules.simple.TextCodeGenRule;
import org.eclipse.reddeer.swt.generator.framework.rules.RedDeerSWTGeneratorRules;

/**
 * This class contains all available rules for RedDeer CodeGen.
 * 
 * @author djelinek
 */
public class CodeGenRules extends RedDeerSWTGeneratorRules {
	
	public static final String BUTTON_SUFFIX = "BTN";
	
	public static final String COMBO_SUFFIX = "CMB";

	public static final String SHELL_SUFFIX = "SHL";
	
	public static final String TEXT_SUFFIX = "TXT";
	
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
