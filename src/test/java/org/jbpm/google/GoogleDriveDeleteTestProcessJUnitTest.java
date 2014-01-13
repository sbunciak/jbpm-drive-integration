package org.jbpm.google;

import java.util.HashMap;
import java.util.Map;

import org.jbpm.google.handlers.GoogleDriveDelete;
import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.process.ProcessInstance;

public class GoogleDriveDeleteTestProcessJUnitTest extends
		JbpmJUnitBaseTestCase {

	private String fileId;
	private GoogleDriveService service = new GoogleDriveService();

	public GoogleDriveDeleteTestProcessJUnitTest() {
		super(true, false);
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
		fileId = service.insertFile("Hello", "Sample file uploaded from jBPM",
				null, new java.io.File("src/test/resources/Hello.txt")
						.getAbsolutePath()).getId();
		//System.out.println("Uploaded " + fileId);
	}

	@Test
	public void testImplicit() throws InterruptedException {
		createRuntimeManager("GoogleDriveDeleteTest.bpmn");

		RuntimeEngine runtimeEngine = getRuntimeEngine();
		KieSession ksession = runtimeEngine.getKieSession();
		ksession.getWorkItemManager().registerWorkItemHandler(
				"Google Drive Delete", new GoogleDriveDelete());
		Map<String, Object> params = new HashMap<String, Object>();
		// initialize variables here if necessary
		params.put("FileId", fileId); // type String
		ProcessInstance processInstance = ksession.startProcess(
				"GoogleDriveDeleteTest", params);
		// if necessary, complete request for service task "Google Drive Delete"

		// do your checks here
		assertProcessInstanceCompleted(processInstance.getId(), ksession);
		// TODO: race-condition workaround
		Thread.sleep(5000);
		assertNull(service.getFile(fileId));
	}
	
}