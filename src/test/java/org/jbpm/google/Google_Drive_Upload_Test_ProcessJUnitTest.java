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

public class Google_Drive_Upload_Test_ProcessJUnitTest extends JbpmJUnitTestCase {

	@Test
    public void testImplicit() {
        KieSession ksession = createKnowledgeSession("GoogleDriveUploadTest.bpmn");
        ksession.getWorkItemManager().registerWorkItemHandler("Google Drive Upload", new SystemOutWorkItemHandler());
        Map<String, Object> params = new HashMap<String, Object>();
        // initialize variables here if necessary
        // params.put("File", value); // type java.lang.Object
        // params.put("Title", value); // type String
        // params.put("Description", value); // type String
        // params.put("Filepath", value); // type String
        ProcessInstance processInstance = ksession.startProcess("GoogleDriveUploadTest", params);
        // if necessary, complete request for service task "Google Drive Upload"
        // do your checks here
        // for example, assertProcessInstanceCompleted(processInstance.getId(), ksession);
    }

}