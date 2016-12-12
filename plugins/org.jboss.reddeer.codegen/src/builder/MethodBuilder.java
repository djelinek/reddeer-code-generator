package org.jboss.reddeer.codegen.builder;

import java.util.Iterator;
import java.util.List;

public class MethodBuilder {

	private String name;	
	
	private static final String SPACE = " ";
	private static final String COMMA = ",";
	private static final String TAB = "\t";
	private static final String NEW_LINE = "\n";
	
	private String visibility;
	private String returnType;
	private List<String> parameters;
	private List<String> commands;
	
	
	
	@Override
	public String toString() {
		StringBuffer code = new StringBuffer();
		code.append(visibility).append(SPACE).append(returnType).append(SPACE).append(name).append("(");
		Iterator<String> parameterIterator = parameters.iterator();
		while (parameterIterator.hasNext()) {
			code.append(parameterIterator.next());
			if (parameterIterator.hasNext()) {
				code.append(COMMA).append(SPACE);
			}
		}
		code.append(")").append(SPACE).append("{").append(NEW_LINE);
		for (String command : commands) {
			code.append(TAB).append(command).append(NEW_LINE);
		}
		code.append("}");
		return code.toString();
	}

}
