package org.jboss.reddeer.codegen.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.jboss.reddeer.codegen.Activator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.P_GETSET, false);
		store.setDefault(PreferenceConstants.P_GENERAL, "general");
		store.setDefault(PreferenceConstants.P_CLASSNAME, "DefaultCodeGenClass.java");
		store.setDefault(PreferenceConstants.P_PROJECTPATH, "");		
	}

}
