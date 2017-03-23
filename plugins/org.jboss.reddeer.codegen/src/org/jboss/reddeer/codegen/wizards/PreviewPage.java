package org.jboss.reddeer.codegen.wizards;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.jboss.reddeer.codegen.Activator;
import org.jboss.reddeer.codegen.builder.ClassBuilder;

/**
 * 
 * @author djelinek
 */
public class PreviewPage extends NewTypeWizardPage {

	private ISelection selection;
	private StyledText area;
	private ClassBuilder classBuilder;

	public PreviewPage(ISelection selection) {
		super(true, "codeGenWizardPageThree");
		setTitle("CodeGen preview");
		setDescription("This page shows preview of generated class");
		setImageDescriptor(ImageDescriptor.createFromURL(
				FileLocator.find(Platform.getBundle(Activator.PLUGIN_ID), new Path("icons/reddeer_logo.png"), null)));
		this.selection = selection;
		setPageComplete(false);
	}

	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		composite.setLayout(layout);
		area = new StyledText(composite, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		area.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		area.setEditable(true);
		setControl(composite);
		Dialog.applyDialogFont(composite);
	}

	public void updateAreaContent(ClassBuilder builder) {
		if (!area.getText().isEmpty())
			area.cut();
		area.setText(builder.toString());
	}

	public void setAreaTXT(String str) {
		area.setText(str);
	}

	public String getAreaTXT() {
		return area.getText();
	}

	public StyledText getArea() {
		return area;
	}

	public ClassBuilder getPreviewClassBuilder() {
		return this.classBuilder;
	}

	public void setClassBuilder(ClassBuilder classBuilder) {
		this.classBuilder = classBuilder;
	}

}
