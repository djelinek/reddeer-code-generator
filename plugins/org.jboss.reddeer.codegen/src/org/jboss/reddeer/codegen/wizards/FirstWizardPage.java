package org.jboss.reddeer.codegen.wizards;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.junit.wizards.WizardMessages;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.wizards.NewInterfaceWizardPage;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.jboss.reddeer.codegen.Activator;

public class FirstWizardPage extends NewTypeWizardPage { //WizardPage {

	private Text projectPath;
	private Text packagePath;
	private Text className;
	private Group group;
	private Button radioDefault, radioGenerate;
	private ISelection selection;
	private Composite container;

	/**
	 * Constructor for FirstWizardPage.
	 * 
	 * @param pageName
	 */
	public FirstWizardPage(ISelection selection) {

		super(true, "codeGenWizardPageOne");
		setTitle("Class definition");
		setDescription("This wizard allows generate a new CodeGen class with *.java");
		setImageDescriptor(ImageDescriptor.createFromURL(FileLocator.find(
				Platform.getBundle(Activator.PLUGIN_ID), new Path(
						"icons/reddeer_logo.png"), null)));
		setPageComplete(false);
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	@Override
	public void createControl(Composite parent) {

		initializeDialogUnits(parent);
		Composite composite= new Composite(parent, SWT.NONE);	
		int nColumns= 4;

		GridLayout layout= new GridLayout();
		layout.numColumns= nColumns;
		composite.setLayout(layout);
		createContainerControls(composite, nColumns);
		createPackageControls(composite, nColumns);
		createSeparator(composite, nColumns);
		createTypeNameControls(composite, nColumns);
		setControl(composite);
		//restoreWidgetValues();
		Dialog.applyDialogFont(composite);
	}
	
	
	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */
	private void initialize() {

		radioDefault.setSelection(true);
		setGroupEnabled(false);
		// ---

		if (selection != null && selection.isEmpty() == false && selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.size() > 1)
				return;
			Object obj = ssel.getFirstElement();
			if (obj instanceof IResource) {
				IContainer container;
				if (obj instanceof IContainer)
					container = (IContainer) obj;
				else
					container = ((IResource) obj).getParent();
				// bere posledni zvolenou cestu z historie workspacu
				projectPath.setText(container.getFullPath().toString());		
				// projectPath.setText(""); // defaultne bude cesta null
			}
		}
		
		className.setText("DefaultCodeGenClass.java");
	}

	/**
	 * Uses the standard container selection dialog to choose the new value for
	 * the container field.
	 * 
	 * @throws URISyntaxException
	 */
	private void handleBrowse() throws URISyntaxException {

		ElementListSelectionDialog dialog = new ElementListSelectionDialog(getShell(), new WorkbenchLabelProvider());
		dialog.setElements(ResourcesPlugin.getWorkspace().getRoot().getProjects());
		dialog.setMessage("Choose package for new CodeGen class.");
		dialog.setTitle("Package selection");
		if (dialog.open() == ElementTreeSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				projectPath.setText(((IResource) result[0]).getFullPath().toOSString());
			}
		}

	}

	/**
	 * Ensures that both text fields are set.
	 */
	private void dialogChanged() {

		IResource container = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(getPackageText()));
		String fileName = getTypeName();

		if (getPackageText().length() == 0 && radioGenerate.getSelection()) {
			updateStatus("Project path must be specified");
			return;
		}
		if (container == null || (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0
				&& radioGenerate.getSelection()) {
			updateStatus("Project path must exist");
			return;
		}
		if (!container.isAccessible() && radioGenerate.getSelection()) {
			updateStatus("Project must be writable");
			return;
		}
		if (fileName.length() == 0 && radioGenerate.getSelection()) {
			updateStatus("Class name must be specified");
			return;
		}
		if (fileName.replace('\\', '/').indexOf('/', 1) > 0 && radioGenerate.getSelection()) {
			updateStatus("Class name must be valid");
			return;
		}
		int dotLoc = fileName.lastIndexOf('.');
		if (dotLoc != -1) {
			String ext = fileName.substring(dotLoc + 1);
			if (ext.equals("java") == false) {
				updateStatus("Class extension must be \"java\"");
				return;
			}
		} else {
			updateStatus("Class extension must be specified");
			return;
		}
		
		updateStatus("ERROR");
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	private void setGroupEnabled(boolean status) {
		for (Control child : group.getChildren())
			child.setEnabled(status);
	}

}