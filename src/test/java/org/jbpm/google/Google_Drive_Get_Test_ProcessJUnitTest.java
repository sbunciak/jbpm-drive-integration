package org.jbpm.google;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.process.instance.impl.demo.SystemOutWorkItemHandler;
import org.jbpm.test.JbpmJUnitTestCase;

import org.junit.Test;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.TaskSummary;

public class Google_Drive_Get_Test_ProcessJUnitTest extends JbpmJUnitTestCase {

	@Test
    public void testImplicit() {
        KieSession ksession = createKnowledgeSession("GoogleDriveGetTest.bpmn");
        ksession.getWorkItemManager().registerWorkItemHandler("Google Drive Get", new SystemOutWorkItemHandler());
        Map<String, Object> params = new HashMap<String, Object>();
        // initialize variables here if necessary
        // params.put("FileId", value); // type String
        ProcessInstance processInstance = ksession.startProcess("GoogleDriveGetTest", params);
        // if necessary, complete request for service task "Google Drive Get"
        // do your checks here
        // for example, assertProcessInstanceCompleted(processInstance.getId(), ksession);
    }

}