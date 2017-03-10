package org.jboss.reddeer.codegen.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.operation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import java.io.*;
import org.eclipse.ui.*;
import org.eclipse.ui.ide.IDE;
import org.hamcrest.core.IsInstanceOf;
import org.jboss.reddeer.codegen.builder.ClassBuilder;
import org.jboss.reddeer.codegen.builder.MethodBuilder;
import org.jboss.reddeer.codegen.finder.ControlFinder;
import org.jboss.reddeer.core.lookup.ShellLookup;
import org.jboss.reddeer.codegen.ButtonCodeGen;
import org.jboss.reddeer.codegen.CodeGen;

public class CodeGenWizard extends Wizard implements INewWizard {

	private FirstWizardPage firstPage;
	private MethodsPage methodsPage;
	private PreviewPage previewPage;
	private ISelection selection;
	private ControlFinder controlFinder;
	private List<CodeGen> codeGenerators;
	private final ClassBuilder classBuilder;

	/**
	 * Constructor for CodeGenWizard.
	 */
	public CodeGenWizard() {
		super();
		setNeedsProgressMonitor(true);
		controlFinder = new ControlFinder();
		classBuilder = new ClassBuilder("CodeGenClass.java", "org.test");
		codeGenerators = new ArrayList<CodeGen>();
		codeGenerators.add(new ButtonCodeGen());
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}

	/**
	 * Adding the page to the wizard.
	 */
	public void addPages() {
		this.setWindowTitle("Red Deer CodeGen");
		firstPage = new FirstWizardPage(selection);
		methodsPage = new MethodsPage(selection);
		previewPage = new PreviewPage(selection);
		addPage(firstPage);
		addPage(methodsPage);
		addPage(previewPage);
	}

	protected void generateCode(Control parentControl) {

		List<Control> controls = controlFinder.find(parentControl, new IsInstanceOf(Control.class));
		for (Control control : controls) {
			for (CodeGen codeGenerator : codeGenerators) {
				if (!codeGenerator.isSupported(control)) {
					continue;
				}
				List<MethodBuilder> generatedMethods = codeGenerator.getGeneratedMethods(control);
				if (!generatedMethods.isEmpty()) {
					classBuilder.addMethods(generatedMethods);
				}
			}
		}

		System.out.println();
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	public boolean performFinish() {

		// Shell[] sh = getShell().getDisplay().getShells(); // returns all
		// active shells includes CodeGen wizard
		Shell[] sh = ShellLookup.getInstance().getShells();
		// String className = sh[sh.length - 2].getText().replaceAll(" ", "") +
		// ".java"; // defines name of class same as shell title
		Control[] c = sh[sh.length - 2].getChildren();
		generateCode(c[0]);

		// CodeGen will generate class
		final String srcText = firstPage.getPackageFragmentRootText();
		final String packageName = firstPage.getPackageText();
		final String fileName = firstPage.getTypeName();
		
		classBuilder.setPackage(packageName);
		
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(srcText+"/"+packageName.replaceAll("\\.", "/"), fileName+".java", monitor);
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

	/**
	 * The worker method. It will find the container, create the file if missing
	 * or just replace its contents, and open the editor on the newly created
	 * file.
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
		sb.append("/** \n" + " * This class '" + fileName + "' was generated by RedDeer Code Generator. \n");
		sb.append(" * Additional selected options:");
		for (String s : methodsPage.getAdditionalOptions()) {
			sb.append(" " + s + ",");
		}
		if (!methodsPage.getAdditionalOptions().isEmpty())
			sb.deleteCharAt(sb.length() - 1);
		sb.append("\n */ \n");

		// sb.append(new ClassBuilder(fileName, containerName).toString());
		classBuilder.addImport("org.eclipse.swt.widgets.Button");
		sb.append(classBuilder.toString());

		return new ByteArrayInputStream(sb.toString().getBytes());
	}

	private void throwCoreException(String message) throws CoreException {

		IStatus status = new Status(IStatus.ERROR, "org.jboss.reddeer.codegen", IStatus.OK, message, null);
		throw new CoreException(status);
	}

}