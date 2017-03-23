package org.jboss.reddeer.codegen.wizards;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.dialogs.NewWizard;
import org.jboss.reddeer.codegen.builder.ClassBuilder;
import org.jboss.reddeer.common.logging.Logger;

/**
 * 
 * @author djelinek
 */
@SuppressWarnings("restriction")
public class CodeGenWizard extends NewWizard implements INewWizard {

	private final Logger log = Logger.getLogger(CodeGenWizard.class);

	private FirstPage firstPage;
	private MethodsPage methodsPage;
	private PreviewPage previewPage;
	private ISelection selection;
	private ClassBuilder classBuilder;

	/**
	 * Constructor for CodeGenWizard.
	 */
	public CodeGenWizard() {
		super();
		this.setWindowTitle("Red Deer CodeGen");
		setNeedsProgressMonitor(true);
		classBuilder = new ClassBuilder();
	}

	@Override
	public void init(IWorkbench aWorkbench, IStructuredSelection currentSelection) {
		super.init(aWorkbench, currentSelection);
	}

	/**
	 * Adding the page to the wizard.
	 */
	public void addPages() {
		methodsPage = new MethodsPage(selection, classBuilder);
		firstPage = new FirstPage(selection, methodsPage);
		previewPage = new PreviewPage(selection);
		addPage(firstPage);
		addPage(methodsPage);
		addPage(previewPage);
	}

	@Override
	public boolean canFinish() {
		if (getContainer().getCurrentPage() instanceof PreviewPage)
			return true;
		else
			return false;
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	@Override
	public boolean performFinish() {

		log.info("Trying finish after pressing 'Finish button'.");
		String srcText = firstPage.getPackageFragmentRootText();
		String packageName = firstPage.getPackageText();
		String fileName = firstPage.getTypeName();
		InputStream stream = new ByteArrayInputStream(previewPage.getAreaTXT().getBytes());

		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(srcText + "/" + packageName.replaceAll("\\.", "/"), getFileName(fileName), stream,
							monitor);
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
	private void doFinish(String containerName, String fileName, InputStream stream, IProgressMonitor monitor)
			throws CoreException {

		monitor.beginTask("Creating " + fileName, 2);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(containerName));
		if (!resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException("Container \"" + containerName + "\" does not exist.");
		}
		IContainer container = (IContainer) resource;
		final IFile file = container.getFile(new Path(fileName));
		try {
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

	private void throwCoreException(String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR, "org.jboss.reddeer.codegen", IStatus.OK, message, null);
		throw new CoreException(status);
	}

	private String getFileName(String name) {
		try {
			if (name.substring(name.lastIndexOf("."), name.length()).equals(".java"))
				return name;
			else
				return name + ".java";
		} catch (Exception e) {
			return name + ".java";
		}
	}

}