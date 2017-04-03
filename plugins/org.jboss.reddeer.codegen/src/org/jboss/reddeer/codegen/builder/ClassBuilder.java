package org.jboss.reddeer.codegen.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jboss.reddeer.codegen.wizards.MethodsPage;

/**
 * 
 * @author djelinek
 */
public class ClassBuilder {

	private String className;
	private String visibility;
	private StringBuffer classBuilder;
	private String packageName;
	private List<String> imports;
	private List<String> selectedOptionals;
	private List<MethodBuilder> methods;
	private Map<String, String> constants;
	private String extendedClass;

	private static final String SPACE = " ";
	private static final String SEMICOLON = ";";
	private static final String TAB = "\t";
	private static final String NEW_LINE = "\n";
	private static final String D_NEW_LINE = "\n\n";

	public ClassBuilder(String name, String projectPackage) {
		this.className = getClassName(name);
		this.visibility = "public";
		this.packageName = projectPackage;
		methods = new ArrayList<>();
		imports = new ArrayList<>();
		selectedOptionals = new ArrayList<>();
		constants = new TreeMap<>();
		classBuilder = new StringBuffer();
	}

	public ClassBuilder(String name, String projectPackage, List<MethodBuilder> methods) {
		this.className = getClassName(name);
		this.visibility = "public";
		this.packageName = projectPackage;
		this.methods = methods;
		imports = new ArrayList<>();
		selectedOptionals = new ArrayList<>();
		constants = new TreeMap<>();
		classBuilder = new StringBuffer();
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
		this.className = "CodeGenDefault";
		this.visibility = "public";
		this.packageName = "org";
		methods = new ArrayList<>();
		imports = new ArrayList<>();
		selectedOptionals = new ArrayList<>();
		constants = new TreeMap<>();
		classBuilder = new StringBuffer();
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
		for (MethodBuilder m : methods) {
			if (m.equals(method))
				return;
		}
		methods.add(method);
	}

	/**
	 * Adds list of methods
	 * 
	 * @param methods
	 *            MethodBuilder - list of methods
	 */
	public void addMethods(List<MethodBuilder> meths) {
		boolean exist;
		for (MethodBuilder meth : meths) {
			exist = false;
			for (MethodBuilder m : methods) {
				if (m.getName().equals(meth.getName()))
					exist = true;
			}
			if (!exist)
				this.methods.add(meth);
		}
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
		if (!this.imports.contains(importName))
			this.imports.add(importName);
	}

	/**
	 * Adds imports to class
	 * 
	 * @param imports
	 *            List of imports
	 */
	public void addImports(List<String> imports) {
		for (String im : imports) {
			if (!this.imports.contains(im))
				this.imports.add(im);
		}
	}

	public void clearImports() {
		this.imports.clear();
	}

	public void addOptionals(List<String> optionals) {
		this.selectedOptionals = optionals;
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

	public String getClassName() {
		return this.className;
	}

	public String getPackageName() {
		return this.packageName;
	}

	public String getExtendedClass() {
		return this.extendedClass;
	}

	public void setExtendedClass(String name) {
		this.extendedClass = name;
	}

	public void addConstants(Map<String, String> map) {
		if (map != null)
			this.constants.putAll(map);
	}

	@Override
	public String toString() {
		classBuilder = new StringBuffer(iniComment());
		// Add all packages into class
		classBuilder.append("package").append(SPACE).append(packageName).append(SEMICOLON).append(D_NEW_LINE);
		// is extendible for extending WizardDialog ?
		boolean extendible = isExtendible();
		// Add all imports into class
		for (String projectImport : imports) {
			classBuilder.append("import").append(SPACE).append(projectImport).append(SEMICOLON).append(NEW_LINE);
		}
		if (!imports.isEmpty())
			classBuilder.append(NEW_LINE);
		// create head of class
		if (extendible && selectedOptionals.contains(MethodsPage.INHERITING))
			classBuilder.append(visibility).append(SPACE).append("class").append(SPACE).append(getClassName(className))
					.append(SPACE).append("extends").append(SPACE).append(extendedClass).append(SPACE).append("{");
		else
			classBuilder.append(visibility).append(SPACE).append("class").append(SPACE).append(getClassName(className))
					.append(SPACE).append("{");
		// class constants
		if (!constants.isEmpty()) {
			for (String constant : constants.keySet()) {
				classBuilder.append(D_NEW_LINE).append(TAB).append("public static final String").append(SPACE)
						.append(constant).append(" = ").append("\"" + constants.get(constant) + "\"").append(SEMICOLON);
			}
		}
		// class methods
		Collections.sort(methods);
		classBuilder.append(D_NEW_LINE).append(TAB).append("// Generated class methods").append(D_NEW_LINE);
		for (MethodBuilder method : methods) {
			classBuilder.append(method.toString()).append(D_NEW_LINE);
		}
		// end of class
		classBuilder.append("}");
		return classBuilder.toString();
	}

	public boolean isExtendible() {

		boolean extendible = false;
		List<MethodBuilder> toRemove = new ArrayList<>();
		for (MethodBuilder meth : this.methods) {
			if (meth.getName().contains("Finish") || meth.getName().contains("Cancel")
					|| meth.getName().contains("Next") || meth.getName().contains("Back")) {
				toRemove.add(meth);
				extendible = true;
			}
		}
		if (extendible && selectedOptionals.contains(MethodsPage.INHERITING)) {
			for (MethodBuilder rm : toRemove) {
				methods.remove(rm);
			}
		}
		return extendible;
	}

	private String iniComment() {

		StringBuffer sb = new StringBuffer();
		sb.append("/** \n" + " * This class '" + getFileName(className)
				+ "' was generated by RedDeer Code Generator. \n");
		sb.append(" * Selected optional:");
		for (String optional : selectedOptionals) {
			sb.append("\n *").append(TAB).append(TAB).append(optional);
		}
		sb.append("\n */ \n");
		return sb.toString();
	}

	private String getClassName(String name) {
		try {
			if (name.substring(name.lastIndexOf("."), name.length()).equals(".java"))
				return name.substring(0, name.lastIndexOf("."));
			else
				return name;
		} catch (Exception e) {
			return name;
		}
	}

	private String getFileName(String name) {
		try {
			if (name.substring(name.lastIndexOf("."), name.length()).equals(".java"))
				return name;
			else
				return name + ".java";
		} catch (Exception e) {
			return name + ".java";
		}
	}

}
