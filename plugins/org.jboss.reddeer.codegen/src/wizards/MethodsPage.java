package org.jboss.reddeer.codegen.wizards;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class MethodsPage extends WizardPage {
	
	private Button buttonGetter;
	private Button buttonSetter;
	private Button buttonEnableExtends;
	private ISelection selection;
	private List<String> additionalOptions;
	/**
	 * Constructor for MethodsPage.
	 * 
	 * @param pageName
	 */
	public MethodsPage(ISelection selection) {
		
		super("wizardPage");
		setTitle("RedDeer CodeGen Setup");
		setDescription("Specify additional code generator options for a new CodeGen class.");
		this.selection = selection;
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		
		additionalOptions = new ArrayList<String>();
		
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 1;
		layout.verticalSpacing = 9;
		
		buttonGetter = new Button(container, SWT.CHECK);
		buttonGetter.setText("Generate getters");
		buttonGetter.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//handleBrowse();
				additionalOptions.add(buttonGetter.getText());
			}
		});
		
		buttonSetter = new Button(container, SWT.CHECK);
		buttonSetter.setText("Generate setters");
		buttonSetter.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//handleBrowse();
				System.out.println();
				additionalOptions.add(buttonSetter.getText());
			}
		});
		
		buttonEnableExtends = new Button(container, SWT.CHECK);
		buttonEnableExtends.setText("Allow implements or extends parent classes");
		buttonEnableExtends.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//handleBrowse();
				additionalOptions.add(buttonEnableExtends.getText());
			}
		});
		
		initialize();
		dialogChanged();
		setControl(container);
	}
	
	private void initialize() {
		
		return;
	}
	
	private void dialogChanged() {
		
		return;
	}

	private void handleBrowse() {
		
		return;
	}

	private void updateStatus(String message) {
		
		setErrorMessage(message);
		setPageComplete(message == null);
	}
	
	public List<String> getAdditionalOptions() {
		
		return additionalOptions;
	}

}