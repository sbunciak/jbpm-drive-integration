package org.jbpm.google;

import java.util.HashMap;
import java.util.Map;

import org.jbpm.google.handlers.GoogleDriveUpdate;
import org.jbpm.google.model.SerializableFile;
import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.TaskSummary;

import com.google.api.services.drive.model.File;

public class GoogleDriveUpdateTestProcessJUnitTest extends
		JbpmJUnitBaseTestCase {

	private File file;
	private GoogleDriveService service = new GoogleDriveService();

	public GoogleDriveUpdateTestProcessJUnitTest() {
		super(true, true);
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
		file = service.insertFile("Hello", "Sample file uploaded from jBPM",
				null, new java.io.File("src/test/resources/Hello.txt")
						.getAbsolutePath());
	}

	@Test
	public void testImplicit() {
		createRuntimeManager("GoogleDriveUpdateTest.bpmn");

		RuntimeEngine runtimeEngine = getRuntimeEngine();
		KieSession ksession = runtimeEngine.getKieSession();
		TaskService taskService = runtimeEngine.getTaskService();

		ksession.getWorkItemManager().registerWorkItemHandler(
				"Google Drive Update", new GoogleDriveUpdate());
		Map<String, Object> params = new HashMap<String, Object>();
		// initialize variables here if necessary
		// params.put("NewFile", value); // type java.lang.Object
		params.put("NewTitle", "Hello updated"); // type String
		params.put("NewDescription", "xxx"); // type String
		params.put("NewFilepath", new java.io.File(
				"src/test/resources/Hello.txt").getAbsolutePath()); // type
																	// String
		params.put("FileId", file.getId()); // type String
		ProcessInstance processInstance = ksession.startProcess(
				"GoogleDriveUpdateTest", params);
		// if necessary, complete request for service task "Google Drive Update"
		SerializableFile newFile = (SerializableFile) this.getVariableValue("NewFile",
				processInstance.getId(), ksession);
		assertNotNull(newFile);

		TaskSummary task = taskService.getTasksAssignedAsPotentialOwner(
				"admin", "en-UK").get(0);
		taskService.start(task.getId(), "admin");
		taskService.complete(task.getId(), "admin", null);

		// do your checks here
		assertProcessInstanceCompleted(processInstance.getId(), ksession);
		
		assertEquals(service.getFile(file.getId()).getTitle(),"Hello updated");
		assertEquals(service.getFile(file.getId()).getDescription(),"xxx");
	}

	@After
	public void tearDown() throws Exception {
		service.deleteFile(file.getId());
		super.tearDown();
	}
}