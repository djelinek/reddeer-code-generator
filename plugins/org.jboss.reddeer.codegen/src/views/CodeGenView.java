package org.jboss.reddeer.codegen.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.part.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;

public class CodeGenView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.jboss.reddeer.codegen.views.CodeGenView";
	private Composite composite;

	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	/**
	 * The constructor.
	 */
	public CodeGenView() {
		
		super();
	}
	
	public void activate() throws PartInitException {
		
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ID);
	}
	
	private StyledText text;
	
	/**
	 * Creates styled text.
	 */
	public void createTextHeader(String header) {

		text = new StyledText(composite, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.READ_ONLY);
		text.setText(header);		
	}
	
	public void initTextHeader() {

		text = new StyledText(composite, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.READ_ONLY);
		text.setText("/* This view will show CodeGen output */");		
	}

	public void createPartControl(Composite parent) {

		this.composite = parent;
		initTextHeader();
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		//viewer.getControl().setFocus();
	}

}
