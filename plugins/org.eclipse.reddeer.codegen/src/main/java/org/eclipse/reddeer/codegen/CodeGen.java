package org.eclipse.reddeer.codegen;

import java.util.List;

import org.eclipse.reddeer.codegen.builder.MethodBuilder;
import org.eclipse.swt.widgets.Control;

/**
 * Interface for all code generators. If the code generator supports a given
 * control/widget then it will generate the appropriate code (it will generate
 * constants, getter/setter methods, etc).
 * 
 * @author djelinek
 */
public interface CodeGen {

	/**
	 * Returns lookup constructor for a given control/widget
	 * 
	 * @param control
	 *            control/widget
	 * @return lookup constructor for a given control/widget
	 */
	MethodBuilder constructor(Control control);

	/**
	 * Returns lookup getter for a given control/widget
	 * 
	 * @param control
	 *            control/widget
	 * @return lookup getter for a given control/widget
	 */
	MethodBuilder get(Control control);

	/**
	 * Returns list of actions methods for given control/widget
	 * 
	 * @param control
	 *            control/widget
	 * @return list of MethodBuilder action methods for given control/widget
	 */
	List<MethodBuilder> getActionMethods(Control control);
}
