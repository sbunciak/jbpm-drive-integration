package org.jbpm.google;

import java.util.HashMap;
import java.util.Map;

import org.jbpm.google.handlers.GoogleDriveList;
import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.process.ProcessInstance;

public class Google_Drive_List_Test_ProcessJUnitTest extends
		JbpmJUnitBaseTestCase {

	public Google_Drive_List_Test_ProcessJUnitTest() {
		super(false, false);
	}

	@Test
	public void testImplicit() {
		createRuntimeManager("GoogleDriveListTest.bpmn");

		RuntimeEngine runtimeEngine = getRuntimeEngine();
		KieSession ksession = runtimeEngine.getKieSession();
		// TaskService taskService = runtimeEngine.getTaskService();

		ksession.getWorkItemManager().registerWorkItemHandler(
				"Google Drive List", new GoogleDriveList());

		Map<String, Object> params = new HashMap<String, Object>();
		// initialize variables here if necessary
		//params.put("Files", files); // type java.util.List

		ProcessInstance processInstance = ksession.startProcess(
				"GoogleDriveListTest", params);
		//System.out.println(this.getVariableValue("Files", processInstance.getId(), ksession));
		// if necessary, complete request for service task "Google Drive List"

		// do your checks here
		assertProcessInstanceCompleted(processInstance.getId(), ksession);
	}

}