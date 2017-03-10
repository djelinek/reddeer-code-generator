package org.jboss.reddeer.codegen;

import java.util.List;

import org.eclipse.swt.widgets.Control;
import org.jboss.reddeer.codegen.builder.MethodBuilder;

/**
 * Interface for all code generators. If the code generator supports a given
 * control/widget then it will generate the appropriate code (it will generate
 * constants, getter/setter methods, etc).
 */
public interface CodeGen {

	/**
	 * Returns whether a given control/widget is supported.
	 * 
	 * @param control
	 *            control/widget
	 * @return whether a given control/widget is supported
	 */
	boolean isSupported(Control control);

	/**
	 * Returns lookup constructor for a given control/widget
	 * 
	 * @param control
	 *            control/widget
	 * @return lookup constructor for a given control/widget
	 */
	MethodBuilder getConstructor(Control control);
	
	///
	List<MethodBuilder> getGeneratedMethods(Control control);
}
