package org.jbpm.google;

import java.util.HashMap;
import java.util.Map;

import org.jbpm.google.handlers.GoogleDriveUpload;
import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.junit.After;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.TaskSummary;

import com.google.api.services.drive.model.File;

public class GoogleDriveUploadTestProcessJUnitTest extends
		JbpmJUnitBaseTestCase {

	private GoogleDriveService service = new GoogleDriveService();
	private java.io.File file = new java.io.File("src/test/resources/Hello.txt");
	private File uploadedFile;

	public GoogleDriveUploadTestProcessJUnitTest() {
		super(true, false);
	}

	@Test
	public void testImplicit() {
		createRuntimeManager("GoogleDriveUploadTest.bpmn");

		RuntimeEngine runtimeEngine = getRuntimeEngine();
		KieSession ksession = runtimeEngine.getKieSession();
		TaskService taskService = runtimeEngine.getTaskService();

		ksession.getWorkItemManager().registerWorkItemHandler(
				"Google Drive Upload", new GoogleDriveUpload());
		Map<String, Object> params = new HashMap<String, Object>();
		// initialize variables here if necessary
		// params.put("File", value); // type java.lang.Object
		params.put("Title", "Hello"); // type String
		params.put("Description", "Sample file upload from jBPM"); // String
		params.put("Filepath", file.getAbsolutePath()); // type String
		ProcessInstance processInstance = ksession.startProcess(
				"GoogleDriveUploadTest", params);
		// if necessary, complete request for service task "Google Drive Upload"
		uploadedFile = (File) this.getVariableValue("File",
				processInstance.getId(), ksession);

		assertNotNull(uploadedFile);

		TaskSummary task = taskService.getTasksAssignedAsPotentialOwner(
				"admin", "en-UK").get(0);
		taskService.start(task.getId(), "admin");
		taskService.complete(task.getId(), "admin", null);

		// do your checks here
		assertProcessInstanceCompleted(processInstance.getId(), ksession);
		// check file on drive
		assertNotNull(service.getFile(uploadedFile.getId()));
	}

	@After
	public void tearDown() throws Exception {
		service.deleteFile(uploadedFile.getId());
		super.tearDown();
	}
}