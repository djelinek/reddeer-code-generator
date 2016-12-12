package org.jboss.reddeer.codegen.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jboss.reddeer.codegen.Activator;
import org.eclipse.ui.IWorkbench;

public class MethodsPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public MethodsPreferencePage() {

		super(GRID);
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {

		Label label = new Label(getFieldEditorParent(), SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 1));

		addField(new BooleanFieldEditor(PreferenceConstants.P_GETSET, "Getters", SWT.CHECK, getFieldEditorParent()));

		addField(new BooleanFieldEditor(PreferenceConstants.P_GETSET, "Setters", SWT.CHECK, getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {

		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Choose methods, which will be generated in CodeGen class");
	}

}