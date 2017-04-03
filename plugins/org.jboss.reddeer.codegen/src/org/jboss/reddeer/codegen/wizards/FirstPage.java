package org.jboss.reddeer.codegen.wizards;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.jboss.reddeer.codegen.Activator;

/**
 * 
 * @author djelinek
 */
public class FirstPage extends NewTypeWizardPage {

	private ISelection selection;
	private MethodsPage methodsPage;

	/**
	 * Constructor for FirstWizardPage.
	 * 
	 * @param pageName
	 */
	public FirstPage(ISelection selection, MethodsPage methodsPage) {
		super(true, "codeGenWizardPageOne");
		this.setTitle("Class definition");
		this.setDescription("This wizard allows generate a new CodeGen class with *.java");
		this.setImageDescriptor(ImageDescriptor.createFromURL(
				FileLocator.find(Platform.getBundle(Activator.PLUGIN_ID), new Path("icons/reddeer_logo.png"), null)));
		this.selection = selection;
		this.methodsPage = methodsPage;
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
		createContainerControls(composite, nColumns);
		createPackageControls(composite, nColumns);
		createSeparator(composite, nColumns);
		createTypeNameControls(composite, nColumns);

		setTypeName("DefaultCodeGen", true);
		setControl(composite);
		// restoreWidgetValues();
		Dialog.applyDialogFont(composite);
	}

	@Override
	public boolean canFlipToNextPage() {
		if (!getPackageFragmentRootText().isEmpty() && !getPackageText().isEmpty() && !getTypeName().isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void handleFieldChanged(String fieldName) {

		if (fieldName.equals(CONTAINER)) {
			if (getPackageFragmentRootText().isEmpty() && !getPackageText().isEmpty())
				;
			// updateStatus(containerChanged());
		} else if (fieldName.equals(TYPENAME)) {
			methodsPage.getClassBuilder().setClassName(getTypeName());
			// updateStatus(typeNameChanged());
		} else if (fieldName.equals(PACKAGE)) {
			methodsPage.getClassBuilder().setPackage(getPackageText());
			// updateStatus(packageChanged());
		}
		dialogChanged();
		getContainer().updateButtons();
		super.handleFieldChanged(fieldName);
	}

	/**
	 * Ensures that both text fields are set.
	 */
	public void dialogChanged() {

		IResource container = ResourcesPlugin.getWorkspace().getRoot().findMember(
				new Path("/" + getPackageFragmentRootText() + "/" + getPackageText().replaceAll("\\.", "/")));
		String fileName = getTypeName();

		updateStatus(containerChanged());
		updateStatus(packageChanged());
		updateStatus(typeNameChanged());

		if (getPackageFragmentRootText().length() == 0) {
			updateStatusMsg("Source folder must be specified");
			return;
		}
		if (getPackageText().length() == 0) {
			updateStatusMsg("Package must be specified");
			return;
		}
		if (container == null || (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
			updateStatusMsg("Project must exist");
			return;
		}
		if (!container.isAccessible()) {
			updateStatusMsg("Project must be writable");
			return;
		}
		if (fileName.length() == 0) {
			updateStatusMsg("Class name must be specified");
			return;
		}
		if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
			updateStatusMsg("Class name must be valid");
			return;
		}
		updateStatusMsg(null);
	}

	private void updateStatusMsg(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

}