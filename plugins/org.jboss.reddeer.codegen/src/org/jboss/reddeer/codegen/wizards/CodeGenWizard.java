package org.jboss.reddeer.codegen.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.operation.*;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import java.io.*;
import org.eclipse.ui.*;
import org.eclipse.ui.ide.IDE;
import org.jboss.reddeer.codegen.builder.ClassBuilder;
import org.jboss.reddeer.codegen.views.CodeGenView;

public class CodeGenWizard extends Wizard implements INewWizard {
	
	private FirstWizardPage firstPage;
	private MethodsPage methodsPage;
	private ISelection selection;
	
	private static final String ID = "org.jboss.reddeer.codegen.wizards.CodeGenWizard";

	/**
	 * Constructor for CodeGenWizard.
	 */
	public CodeGenWizard() {
	
		super();
		setNeedsProgressMonitor(true);
	}
	
	/**
	 * Adding the page to the wizard.
	 */
	public void addPages() {
		
		firstPage = new FirstWizardPage(selection);
		methodsPage = new MethodsPage(selection);
		addPage(firstPage);
		addPage(methodsPage);
	}

	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	public boolean performFinish() {
		
		// CodeGen will generate class 
		if (firstPage.getRadioGenerate()) {
			final String containerName = firstPage.getProjectPath();
			final String fileName = firstPage.getClassName();
			IRunnableWithProgress op = new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) throws InvocationTargetException {
					try {
						doFinish(containerName, fileName, monitor);
					} catch (CoreException e) {
						throw new InvocationTargetException(e);
					} finally {
						monitor.done();
					}
				}
			};
			try {
				getContainer().run(true, false, op);
			} catch (InterruptedException e) {
				return false;
			} catch (InvocationTargetException e) {
				Throwable realException = e.getTargetException();
				MessageDialog.openError(getShell(), "Error", realException.getMessage());
				return false;
			}
			return true;
		}
		else
		{
			// open view for default variant of CodeGen
			IRunnableWithProgress op = new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) throws InvocationTargetException {
					try {
						monitor.worked(1);
						getShell().getDisplay().asyncExec(new Runnable() {
							public void run() {
								CodeGenView view = new CodeGenView();
								try {
									view.activate();
									view.createTextHeader(initViewContent("DefaultCodeGenClass.java"));
								} catch (PartInitException e) {
								}
							}
						});
						monitor.worked(1);
					} catch (Exception e) {
						throw new InvocationTargetException(e);
					} finally {
						monitor.done();
					}
				}
			};
			try {
				getContainer().run(true, false, op);
			} catch (InterruptedException e) {
				return false;
			} catch (InvocationTargetException e) {
				Throwable realException = e.getTargetException();
				MessageDialog.openError(getShell(), "Error", realException.getMessage());
				return false;
			}
			return true;
		}
	}
	
	/**
	 * The worker method. It will find the container, create the
	 * file if missing or just replace its contents, and open
	 * the editor on the newly created file.
	 */
	private void doFinish(String containerName, String fileName, IProgressMonitor monitor) throws CoreException {
		
		// create a sample file
		monitor.beginTask("Creating " + fileName, 2);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(containerName));
		if (!resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException("Container \"" + containerName + "\" does not exist.");
		}
		IContainer container = (IContainer) resource;
		final IFile file = container.getFile(new Path(fileName));
		try {
			InputStream stream = openContentStream(fileName, containerName);
			if (file.exists()) {
				file.setContents(stream, true, true, monitor);
			} else {
				file.create(stream, true, monitor);
			}
			stream.close();
		} catch (IOException e) {
		}
		monitor.worked(1);
		monitor.setTaskName("Opening file for editing...");
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					IDE.openEditor(page, file, true);
				} catch (PartInitException e) {
				}
			}
		});
		monitor.worked(1);
	}
	
	/**
	 * We will initialize file contents with a sample text.
	 */
	private InputStream openContentStream(String fileName, String containerName) {
			
		StringBuilder sb = new StringBuilder();
		sb.append("/** \n"
				+ " * This class '" + fileName + "' was generated by RedDeer Code Generator. \n");
		sb.append(" * Additional selected options:");
		for (String s : methodsPage.getAdditionalOptions()) {
			sb.append(" " + s + ",");
		}
		if(!methodsPage.getAdditionalOptions().isEmpty())
			sb.deleteCharAt(sb.length()-1);
		sb.append("\n */ \n\n");
		
		sb.append(new ClassBuilder(fileName, containerName).buildClass());
		
		return new ByteArrayInputStream(sb.toString().getBytes());
	}
	
	private String initViewContent(String fileName) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("/** \n"
				+ " * This class '" + fileName + "' was generated by RedDeer Code Generator. \n");
		sb.append(" * Additional selected options:");
		for (String s : methodsPage.getAdditionalOptions()) {
			sb.append(" " + s + ",");
		}
		if(!methodsPage.getAdditionalOptions().isEmpty())
			sb.deleteCharAt(sb.length()-1);
		sb.append("\n */");
		return sb.toString();
	}

	private void throwCoreException(String message) throws CoreException {
		
		IStatus status =
			new Status(IStatus.ERROR, "org.jboss.reddeer.codegen", IStatus.OK, message, null);
		throw new CoreException(status);
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		
		this.selection = selection;
	}
	
}