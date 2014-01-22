package org.jbpm.google;

import java.util.HashMap;
import java.util.Map;

import org.jbpm.google.handlers.GoogleDriveGet;
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

public class GoogleDriveGetTestProcessJUnitTest extends JbpmJUnitBaseTestCase {

	private File file;
	private GoogleDriveService service = new GoogleDriveService();

	public GoogleDriveGetTestProcessJUnitTest() {
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
		createRuntimeManager("GoogleDriveGetTest.bpmn");

		RuntimeEngine runtimeEngine = getRuntimeEngine();
		KieSession ksession = runtimeEngine.getKieSession();
		TaskService taskService = runtimeEngine.getTaskService();

		ksession.getWorkItemManager().registerWorkItemHandler(
				"Google Drive Get", new GoogleDriveGet());
		Map<String, Object> params = new HashMap<String, Object>();
		// initialize variables here if necessary
		params.put("FileId", file.getId()); // type String

		ProcessInstance processInstance = ksession.startProcess(
				"GoogleDriveGetTest", params);
		// if necessary, complete request for service task "Google Drive Get"

		SerializableFile uploadedFile = (SerializableFile) this.getVariableValue("File",
				processInstance.getId(), ksession);
		// if necessary, complete request for service task "Google Drive List"
		assertNotNull(uploadedFile);

		TaskSummary task = taskService.getTasksAssignedAsPotentialOwner(
				"admin", "en-UK").get(0);
		taskService.start(task.getId(), "admin");
		taskService.complete(task.getId(), "admin", null);

		// do your checks here
		assertEquals(file.getFileSize(), uploadedFile.getFile().getFileSize());
		assertProcessInstanceCompleted(processInstance.getId(), ksession);
	}

	@After
	public void tearDown() throws Exception {
		service.deleteFile(file.getId());
		super.tearDown();
	}
}