package org.jboss.reddeer.codegen.wizards;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.internal.junit.util.LayoutUtil;
import org.eclipse.jdt.internal.junit.wizards.MethodStubsSelectionButtonGroup;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.jboss.reddeer.codegen.Activator;

@SuppressWarnings("restriction")
public class MethodsPage extends NewTypeWizardPage {

	private ISelection selection;
	private List<String> additionalOptions;

	private MethodStubsSelectionButtonGroup fMethodStubsButtons;

	/**
	 * Constructor for MethodsPage.
	 * 
	 * @param pageName
	 */
	public MethodsPage(ISelection selection) {
		
		super(true, "codeGenWizardPageTwo");
		setTitle("Methods setup");
		setDescription("Specify CodeGen options (aditional methods, class properties, etc.)");
		setImageDescriptor(ImageDescriptor.createFromURL(
				FileLocator.find(Platform.getBundle(Activator.PLUGIN_ID), new Path("icons/reddeer_logo.png"), null)));
		this.selection = selection;
		additionalOptions = new ArrayList<String>();
		setPageComplete(false);
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	@Override
	public void createControl(Composite parent) {

		initializeDialogUnits(parent);
		Composite composite = new Composite(parent, SWT.NONE);
		int nColumns = 4;

		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		String[] buttonNames = new String[] { "Generate getters", "Generate setters", "Extends parent classes" };
		fMethodStubsButtons = new MethodStubsSelectionButtonGroup(SWT.CHECK, buttonNames, 2) {
			@Override
			protected void doWidgetSelected(SelectionEvent e) {
				super.doWidgetSelected(e);
				// saveWidgetValues();
			}
		};
		fMethodStubsButtons.setLabelText("Optional");
		
		createMethodStubSelectionControls(composite, nColumns);
		setControl(composite);
		Dialog.applyDialogFont(composite);
	}

	protected void createMethodStubSelectionControls(Composite composite, int nColumns) {
		LayoutUtil.setHorizontalSpan(fMethodStubsButtons.getLabelControl(composite), nColumns);
		LayoutUtil.createEmptySpace(composite, 1);
		LayoutUtil.setHorizontalSpan(fMethodStubsButtons.getSelectionButtonsGroup(composite), nColumns - 1);
	}

	public List<String> getAdditionalOptions() {
		return additionalOptions;
	}

}