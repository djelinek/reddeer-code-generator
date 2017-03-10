package org.jboss.reddeer.codegen.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jboss.reddeer.codegen.wizards.CodeGenWizard;
import org.jboss.reddeer.core.lookup.ShellLookup;
import org.jboss.reddeer.workbench.core.lookup.WorkbenchShellLookup;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class CodeGenHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		openCodeGen(window); // when click on toolbar button
		window.getShell().getDisplay().addFilter(SWT.KeyDown, new Listener() {
			public void handleEvent(Event e) {
				if ((e.stateMask == SWT.ALT) && (e.keyCode == 'g')) { 
					// Shortcut – ALT + G
					// System.out.println("Listener activated");
					openCodeGen(window);
				}
			}
		});	
		return null;
	}
	
	private void openCodeGen(IWorkbenchWindow window) {
		
		// System.out.println("shortcut captured");
		IWizard wizard = new CodeGenWizard();
		WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
		dialog.open();
	}
}
