package org.jboss.reddeer.codegen.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jboss.reddeer.codegen.Activator;
import org.eclipse.ui.IWorkbench;

public class MainPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private Group group;
	
	public MainPreferencePage() {

		super(GRID);
	}
	
	@Override
	public void init(IWorkbench workbench) {

		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Basic properties, which can be set for more specific function of RedDeer Code Generator");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {

		//addField(new BooleanFieldEditor(PreferenceConstants.P_VIEW, "Show CodeGen view in Eclipse IDE",
			//	getFieldEditorParent()));

		Label label = new Label(getFieldEditorParent(), SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 1));

		addField(new RadioGroupFieldEditor(PreferenceConstants.P_GENERAL, "General:", 1,
				new String[][] { { "Default (Generate code into CodeGen view)", "default" },
						{ "Generate code into project class", "generate" } },
				getFieldEditorParent()));

		// ---
		group = new Group(getFieldEditorParent(), SWT.VERTICAL);
		group.setLayout(new FillLayout(SWT.VERTICAL));
		group.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		// ---
		addField(new StringFieldEditor(PreferenceConstants.P_CLASSNAME, "Name of generated class:", group));
		addField(new DirectoryFieldEditor(PreferenceConstants.P_PROJECTPATH, "Project path:", group));
		//addField(new FileFieldEditor(PreferenceConstants.P_PROJECTPATH,  "Project path", group));
		// ---
		if(this.getPreferenceStore().getString("general") == "generate")
			setGroupDisabled(true);	
		else
			setGroupDisabled(false);
			
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		
		String prefName = null;
		Object object = event.getSource();
		if(object instanceof RadioGroupFieldEditor)
			prefName = ((RadioGroupFieldEditor) object).getPreferenceName();
		
		if (prefName == "general") {
			String value = event.getNewValue().toString();
			if (value == "generate") {
				setGroupDisabled(true);
			} else {
				setGroupDisabled(false);
			}
		}

	}
	
	@Override
	protected void performDefaults() {

		super.performDefaults();
		setGroupDisabled(false);
	}
	
	private void setGroupDisabled(boolean status) {
		
		for (Control child : group.getChildren())
			child.setEnabled(status);
	}
	

}