package org.jboss.reddeer.codegen.builder;

import java.util.ArrayList;
import java.util.List;

public class ClassBuilder {

	private String className;
	private String visibility;
	private StringBuilder classBuilder;
	private String packageName;
	private List<String> imports;
	private List<MethodBuilder> methods;

	private static final String SPACE = " ";
	private static final String SEMICOLON = ";";
	private static final String TAB = "\t";
	private static final String NEW_LINE = "\n";
	private static final String D_NEW_LINE = "\n\n";

	public ClassBuilder(String name, String projectPackage) {
		this.className = getClassName(name);
		this.visibility = "public";
		// list of imported packages
		this.packageName = projectPackage;
		// list of class methods
		methods = new ArrayList<>();
		methods.add(MethodBuilder.constructor(className));
		classBuilder = new StringBuilder();
		imports = new ArrayList<>();
	}

	public ClassBuilder(String name, String projectPackage, List<MethodBuilder> methods) {
		this.className = getClassName(name);
		this.visibility = "public";
		// list of imported packages
		this.packageName = projectPackage;
		// list of class methods
		this.methods = methods;
		classBuilder = new StringBuilder();
		imports = new ArrayList<>();
	}

	/**
	 * Default constructor with predefined class parameters
	 * 
	 * <ul>
	 * <li>className - DefaultCodeGenClass</li>
	 * <li>visibility - public</li>
	 * </ul>
	 */
	public ClassBuilder() {
		this.className = "DefaultCodeGenClass";
		this.visibility = "public";
		this.packageName = "org.jboss.reddeer";
		methods = new ArrayList<>();
		classBuilder = new StringBuilder();
		imports = new ArrayList<>();
	}

	/**
	 * Static method for ClassBuilder instance
	 * 
	 * @return ClassBuilder instance
	 */
	public static ClassBuilder getInstance() {
		return new ClassBuilder();
	}

	/**
	 * Create class method with defined name
	 * 
	 * @param name
	 *            String
	 */
	public void createMethod(String name) {
		methods.add(new MethodBuilder().name(name));
	}

	/**
	 * Adds method
	 * 
	 * @param method
	 *            MethodBuilder - method
	 */
	public void addMethod(MethodBuilder method) {
		methods.add(method);
	}

	/**
	 * Adds list of methods
	 * 
	 * @param methods
	 *            MethodBuilder - list of methods
	 */
	public void addMethods(List<MethodBuilder> methods) {
		this.methods.addAll(methods);
	}

	/**
	 * Adds package to class imported packages
	 * 
	 * @param packageName
	 *            String - name for class imported packages
	 */
	public void setPackage(String packageName) {
		this.packageName = packageName;
	}
	
	/**
	 * Adds import to class
	 * 
	 * @param importName
	 *            String - name for class import
	 */
	public void addImport(String importName) {
		imports.add(importName);
	}

	/**
	 * Sets name of class
	 * 
	 * @param name
	 *            String
	 */
	public void setClassName(String name) {
		this.className = name;
	}

	@Override
	public String toString() {
		classBuilder = new StringBuilder();
		// Add all packages into class
		classBuilder.append("package").append(SPACE).append(packageName).append(SEMICOLON).append(D_NEW_LINE);
		// Add all imports into class
		for (String projectImport : imports) {
			classBuilder.append("import").append(SPACE).append(projectImport).append(SEMICOLON).append(NEW_LINE);
		}
		if(!imports.isEmpty())
			classBuilder.append(NEW_LINE);
		// create head of class
		classBuilder.append(visibility).append(SPACE).append("class").append(SPACE).append(className).append(SPACE)
				.append("{");
		// class methods
		classBuilder.append(D_NEW_LINE).append(TAB).append("// Generated class methods").append(NEW_LINE);
		for (MethodBuilder method : methods) {
			classBuilder.append(method.toString()).append(D_NEW_LINE);
		}
		// end of class
		classBuilder.append("}");
		return classBuilder.toString();
	}

	private String getClassName(String name) {
		return name.substring(0, name.lastIndexOf("."));
	}

}
