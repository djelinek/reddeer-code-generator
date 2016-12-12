package org.jboss.reddeer.codegen.builder;

public class ClassBuilder {
	
	private String className;
	private String projectPackage;
	private StringBuilder classBuilder;
	
	private static final String SPACE = " ";
	private static final String COMMA = ",";
	private static final String SEMICOLON = ";";
	private static final String TAB = "\t";
	private static final String NEW_LINE = "\n";
	private static final String D_NEW_LINE = "\n\n";
	
	private String visibility;
	
	public ClassBuilder(String name, String projectPackage) {
		
		this.className = name.substring(0, name.lastIndexOf("."));
		this.projectPackage = projectPackage.replaceAll("/", ".").substring(projectPackage.indexOf("src") + 4, projectPackage.length());
		visibility = "public";
	}
	
	public String buildClass() {
		
		classBuilder = new StringBuilder();
		classBuilder.append("package").append(SPACE).append(projectPackage).append(SEMICOLON).append(D_NEW_LINE);
		classBuilder.append(visibility).append(SPACE).append("class").append(SPACE).append(className).append(SPACE).append("{").append(D_NEW_LINE);	
		
		// here will be adding all class methods
		classBuilder.append(buildDefaultConstructor()).append(D_NEW_LINE).append(TAB).append("// HERE will be generated all class methods");
	
		classBuilder.append(D_NEW_LINE).append("}");
		return classBuilder.toString();
	}
	
	public String buildDefaultConstructor() {
		
		StringBuilder constructor = new StringBuilder();
		constructor.append(TAB).append(visibility).append(SPACE).append(className).append("()");
		constructor.append(SPACE).append("{").append(NEW_LINE).append(TAB).append("}");		
		return constructor.toString(); 
	}
	
	
}
