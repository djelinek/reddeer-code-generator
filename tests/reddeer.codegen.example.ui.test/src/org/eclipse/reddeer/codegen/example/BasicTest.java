package org.eclipse.reddeer.codegen.example;

import static org.junit.Assert.assertTrue;

import org.eclipse.reddeer.codegen.example.utils.NewRedDeerTestPluginPage;
import org.eclipse.reddeer.codegen.example.utils.NewRedDeerTestPluginWizard;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.eclipse.condition.ProjectExists;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.junit.Test;

public class BasicTest {

	@Test
	public void testCreateRedDeerTestPlugin() {

		// Open wizard for creating new RedDeer test plug-in
		new NewRedDeerTestPluginWizard().open();

		// Wait condition until wizard isn't open
		new WaitUntil(new ShellIsAvailable("New RedDeer Test Plugin"));

		// Set wizard parameters via API (class), which was generated by RedDeer
		// CogeGen plug-in
		NewRedDeerTestPluginPage codegen = new NewRedDeerTestPluginPage();
		codegen.activateNewRedDeerTestPlugin();
		codegen.setTextPluginName("org.test.codegen");
		codegen.setTextPluginId("testProject");
		codegen.setTextProvider("reddeer");
		codegen.toggleApplicationIdGroup(true);
		codegen.finish();

		// Check if project was properly created
		assertTrue("Project wasn't created.", new ProjectExists("testProject").test());

		// Check errors in Problems view
		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		assertTrue("Problems view contains ERROR messages", problemsView.getProblems(ProblemType.ERROR).isEmpty());
	}

}
