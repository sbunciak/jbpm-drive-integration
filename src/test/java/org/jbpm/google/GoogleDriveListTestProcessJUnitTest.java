package org.jbpm.google;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.google.handlers.GoogleDriveList;
import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.TaskSummary;

import com.google.api.services.drive.model.File;

public class GoogleDriveListTestProcessJUnitTest extends JbpmJUnitBaseTestCase {

	public GoogleDriveListTestProcessJUnitTest() {
		super(true, false);
	}

	@Test
	public void testImplicit() {
		createRuntimeManager("GoogleDriveListTest.bpmn");

		RuntimeEngine runtimeEngine = getRuntimeEngine();
		KieSession ksession = runtimeEngine.getKieSession();
		TaskService taskService = runtimeEngine.getTaskService();

		ksession.getWorkItemManager().registerWorkItemHandler(
				"Google Drive List", new GoogleDriveList());

		Map<String, Object> params = new HashMap<String, Object>();
		// initialize variables here if necessary
		// params.put("Files", files); // type java.util.List

		ProcessInstance processInstance = ksession.startProcess(
				"GoogleDriveListTest", params);

		@SuppressWarnings("unchecked")
		List<File> files = (List<File>) this.getVariableValue("Files",
				processInstance.getId(), ksession);
		// if necessary, complete request for service task "Google Drive List"
		assertNotNull(files);

		TaskSummary task = taskService.getTasksAssignedAsPotentialOwner(
				"admin", "en-UK").get(0);
		taskService.start(task.getId(), "admin");
		taskService.complete(task.getId(), "admin", null);

		// do your checks here
		assertProcessInstanceCompleted(processInstance.getId(), ksession);
	}

}