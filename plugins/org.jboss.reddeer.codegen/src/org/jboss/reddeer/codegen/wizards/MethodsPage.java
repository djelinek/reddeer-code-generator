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
import org.eclipse.swt.widgets.Button;
import org.jboss.reddeer.codegen.Activator;
import org.jboss.reddeer.codegen.builder.ClassBuilder;

/**
 * 
 * @author djelinek
 */
@SuppressWarnings("restriction")
public class MethodsPage extends NewTypeWizardPage {

	private ISelection selection;
	private List<String> selectedOptional;
	private ClassBuilder classBuilder;

	private MethodStubsSelectionButtonGroup fMethodOptionalStubsButtons;
	private MethodStubsSelectionButtonGroup fMethodBasicStubsButtons;

	/**
	 * Constructor for MethodsPage.
	 * 
	 * @param pageName
	 */
	public MethodsPage(ISelection selection, ClassBuilder builder) {

		super(true, "codeGenWizardPageTwo");
		setTitle("Methods setup");
		setDescription("Specify CodeGen options (aditional methods, class properties, etc.)");
		setImageDescriptor(ImageDescriptor.createFromURL(
				FileLocator.find(Platform.getBundle(Activator.PLUGIN_ID), new Path("icons/reddeer_logo.png"), null)));
		this.selection = selection;
		this.classBuilder = builder;
		selectedOptional = new ArrayList<String>();
		selectedOptional.add("Constructor");
		selectedOptional.add("Getter");
		setPageComplete(false);
	}

	@Override
	public void createControl(Composite parent) {

		initializeDialogUnits(parent);
		Composite composite = new Composite(parent, SWT.NONE);
		int nColumns = 4;

		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		String[] basicButtonNames = new String[] { "Constructor" };
		fMethodBasicStubsButtons = new MethodStubsSelectionButtonGroup(SWT.CHECK, basicButtonNames, 1) {
			@Override
			protected void doWidgetSelected(SelectionEvent e) {
				super.doWidgetSelected(e);
				// saveWidgetValues();
				handleSelectedOptional(e);
			}
		};

		String[] optionalButtonNames = new String[] { "Getter", "Setter", "Allow inheriting" };
		fMethodOptionalStubsButtons = new MethodStubsSelectionButtonGroup(SWT.CHECK, optionalButtonNames, nColumns) {
			@Override
			protected void doWidgetSelected(SelectionEvent e) {
				super.doWidgetSelected(e);
				// saveWidgetValues();
				handleSelectedOptional(e);
			}
		};

		createMethodStubSelectionControls(composite, nColumns);
		fMethodBasicStubsButtons.setSelection(0, true);
		fMethodOptionalStubsButtons.setSelection(0, true);
		setControl(composite);
		Dialog.applyDialogFont(composite);
	}

	public ClassBuilder getClassBuilder() {
		return this.classBuilder;
	}

	private void handleSelectedOptional(SelectionEvent e) {
		boolean bool = ((Button) e.widget).getSelection();
		String name = ((Button) e.widget).getText();
		if (bool && !selectedOptional.contains(name))
			selectedOptional.add(name);
		else if (!bool && selectedOptional.contains(name))
			selectedOptional.remove(name);
	}

	protected void createMethodStubSelectionControls(Composite composite, int nColumns) {
		LayoutUtil.createEmptySpace(composite, nColumns);
		// LayoutUtil.setHorizontalSpan(fMethodBasicStubsButtons.getLabelControl(composite),
		// nColumns);
		LayoutUtil.setHorizontalSpan(fMethodBasicStubsButtons.getSelectionButtonsGroup(composite), nColumns - 1);
		createSeparator(composite, nColumns);
		// LayoutUtil.setHorizontalSpan(fMethodOptionalStubsButtons.getLabelControl(composite),
		// nColumns);
		LayoutUtil.setHorizontalSpan(fMethodOptionalStubsButtons.getSelectionButtonsGroup(composite), nColumns - 1);
	}

	public List<String> getSelectedOptional() {
		return selectedOptional;
	}

}