package org.jboss.reddeer.codegen.builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author djelinek
 */
public class MethodBuilder {

	private String name;
	private String visibility;
	private String returnType;
	private List<String> parameters;
	private List<String> commands;

	private static final String SPACE = " ";
	private static final String COMMA = ",";
	private static final String TAB = "\t";
	private static final String NEW_LINE = "\n";

	/**
	 * Constructor which initializes the following attributes
	 * <ul>
	 * <li>visibility to "public"
	 * <li>type to "void"
	 * <li>name to "foo"
	 * </ul>
	 */
	public MethodBuilder() {
		visibility = "public";
		returnType = "void";
		name = "foo";
		parameters = new ArrayList<String>();
		commands = new ArrayList<String>();
	}

	/**
	 * Static method for creating default code builder.
	 * 
	 * @return code builder
	 */
	public static MethodBuilder method() {
		return new MethodBuilder();
	}

	/**
	 * Static method for creating a default constructor
	 * 
	 * @param name
	 *            String
	 * @return method builder
	 */
	public static MethodBuilder constructor(String name) {
		return new MethodBuilder().returnType("").name(name);
	}

	/**
	 * Sets visibility.
	 * 
	 * @param visibility
	 *            visibility
	 * @return current code builder
	 */
	public MethodBuilder visibility(String visibility) {
		this.visibility = visibility;
		return this;
	}

	/**
	 * Sets return type.
	 * 
	 * @param type
	 *            return type
	 * @return current code builder
	 */
	public MethodBuilder returnType(String type) {
		this.returnType = type;
		return this;
	}

	/**
	 * Sets method name.
	 * 
	 * @param name
	 *            method name
	 * @return current code builder
	 */
	public MethodBuilder name(String name) {
		this.name = methodNameMask(name).replaceAll("\\W", "");
		return this;
	}

	/**
	 * Sets get method name, e.g. get("Name") returns "getName".
	 * 
	 * @param name
	 *            method name
	 * @return current code builder
	 */
	public MethodBuilder get(String name) {
		return name("get " + name);
	}

	/**
	 * Sets set method name, e.g. set("Name") returns "setName".
	 * 
	 * @param name
	 *            method name
	 * @return current code builder
	 */
	public MethodBuilder set(String name) {
		return name("set " + name);
	}

	/**
	 * Sets method parameter, e.g. "String name".
	 * 
	 * @param parameter
	 *            method parameter
	 * @return current code builder
	 */
	public MethodBuilder parameter(String parameter) {
		parameters.add(parameter);
		return this;
	}

	/**
	 * Adds a given command into the method.
	 * 
	 * @param command
	 *            commands inside method
	 * @return current code builder
	 */
	public MethodBuilder command(String command) {
		if (!command.endsWith(";")) {
			command += ";";
		}
		commands.add(command);
		return this;
	}

	/**
	 * Adds a return command to the method.
	 * 
	 * @param command
	 *            return command
	 * @return current code builder
	 */
	public MethodBuilder returnCommand(String command) {
		return command("return " + command);
	}

	public String getName() {
		return name;
	}

	public String getMethodType() {
		if (name.startsWith("get"))
			try {
				if (name.substring(0, 7).equals("getText") || name.substring(0, 8).equals("getItems")
						|| name.substring(0, 12).equals("getSelection"))
					return "Getter";
				else
					return "Constructor";
			} catch (Exception e) {
				return "Constructor";
			}
		else if (name.startsWith("setText") || name.startsWith("setSelection"))
			return "Setter";
		else
			return "Action method";
	}

	private String methodNameMask(String name) {
		String[] words = name
				.replaceAll("[\\d\\'\\+\\-\\:\\;\\(\\)\\[\\]\\{\\}\\~\\^\\*\\&\\#\\@\\$\\<\\>\\,\\_\\.\\\"]", "")
				.split(" ");
		String forReturn = words[0];
		for (int i = 1; i < words.length; i++) {
			if (words[i].isEmpty())
				continue;
			if (Character.isDigit(words[i].charAt(0)))
				forReturn = forReturn + words[i];
			else
				forReturn = forReturn + words[i].replaceFirst(String.valueOf(words[i].charAt(0)),
						String.valueOf(words[i].charAt(0)).toUpperCase());
		}
		return forReturn;
	}

	@Override
	public String toString() {
		StringBuffer code = new StringBuffer();
		if (returnType.isEmpty())
			code.append(TAB).append(visibility).append(SPACE).append(name).append("(");
		else
			code.append(TAB).append(visibility).append(SPACE).append(returnType).append(SPACE).append(name).append("(");
		Iterator<String> parameterIterator = parameters.iterator();
		while (parameterIterator.hasNext()) {
			code.append(parameterIterator.next());
			if (parameterIterator.hasNext()) {
				code.append(COMMA).append(SPACE);
			}
		}
		code.append(")").append(SPACE).append("{").append(NEW_LINE);
		for (String command : commands) {
			code.append(TAB).append(TAB).append(command).append(NEW_LINE);
		}
		code.append(TAB).append("}");
		return code.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MethodBuilder other = (MethodBuilder) obj;
		if (commands == null) {
			if (other.commands != null)
				return false;
		} else if (commands.equals(other.commands))
			return true;
		if (name == other.name)
			return true;
		return false;
	}

}
