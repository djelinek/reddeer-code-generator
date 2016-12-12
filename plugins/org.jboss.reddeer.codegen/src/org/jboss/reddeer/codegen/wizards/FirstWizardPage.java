package org.jboss.reddeer.codegen.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.WizardPage;
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
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class FirstWizardPage extends WizardPage {
	
	private Text projectPath;
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
		
		super("wizardPage");
		setTitle("RedDeer CodeGen Setup");
		setDescription("This wizard creates a new CodeGen class with *.java");
		this.selection = selection;
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		
		container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 1;
		layout.verticalSpacing = 9;
		// ---
		
		radioDefault = new Button(container, SWT.RADIO);
		radioDefault.setText("Default (Generate code into CodeGen view)");
		radioDefault.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setGroupEnabled(false);
				dialogChanged();
			}
		});
		
		radioGenerate = new Button(container, SWT.RADIO);
		radioGenerate.setText("Generate code into project class");
		radioGenerate.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setGroupEnabled(true);
				dialogChanged();
			}
		});
		
		group = new Group(container, SWT.VERTICAL);
		GridLayout groupLayout = new GridLayout();
		group.setLayout(groupLayout);
		groupLayout.numColumns = 3;
		groupLayout.verticalSpacing = 9;
		group.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			
		// ---
		Label label = new Label(group, SWT.NULL);
		label.setText("&Project path:");

		projectPath = new Text(group, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		projectPath.setLayoutData(gd);
		projectPath.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		Button button = new Button(group, SWT.PUSH);
		button.setText("Browse");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});
		// ---
		
		// ---
		label = new Label(group, SWT.NULL);
		label.setText("&Class name:");

		className = new Text(group, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		className.setLayoutData(gd);
		className.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		// ---
		
		initialize();
		dialogChanged();
		setControl(container);
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
					projectPath.setText(container.getFullPath().toString()); // bere posledni zvolenou cestu z historie workspacu
					//projectPath.setText(""); // defaultne bude cesta null
			}
		}
		className.setText("DefaultCodeGenClass.java");
	}

	/**
	 * Uses the standard container selection dialog to choose the new value for
	 * the container field.
	 */
	private void handleBrowse() {
		
		/*
		ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(getShell(), new WorkbenchLabelProvider(),
				new WorkbenchContentProvider());
		
		dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
		dialog.setAllowMultiple(false);
		dialog.setMessage("Select path for new CodeGen class");
		if (dialog.open() == ElementTreeSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				projectPath.setText(((Path) result[0]).toString());
			}
		}
		*/
		
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(
				getShell(), ResourcesPlugin.getWorkspace().getRoot(), false,
				"Select path for new CodeGen class");
		if (dialog.open() == ContainerSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				projectPath.setText(((Path) result[0]).toString());
			}
		}
		
	}

	/**
	 * Ensures that both text fields are set.
	 */
	private void dialogChanged() {
		
		IResource container = ResourcesPlugin.getWorkspace().getRoot()
				.findMember(new Path(getProjectPath()));
		String fileName = getClassName();

		if (getProjectPath().length() == 0 && radioGenerate.getSelection()) {
			updateStatus("Project path must be specified");
			return;
		}
		if (container == null
				|| (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0 && radioGenerate.getSelection()) {
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
		}
		else
		{
			updateStatus("Class extension must be specified");
			return;
		}
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getProjectPath() {
		return projectPath.getText();
	}

	public String getClassName() {
		return className.getText();
	}
	
	private void setGroupEnabled(boolean status) {
		
		for (Control child : group.getChildren())
			child.setEnabled(status);
	}
	
	public boolean getRadioDefault() {
		
		return this.radioDefault.getSelection();
	}
	
	public boolean getRadioGenerate() {
		
		return this.radioGenerate.getSelection();
	}
	
	
}