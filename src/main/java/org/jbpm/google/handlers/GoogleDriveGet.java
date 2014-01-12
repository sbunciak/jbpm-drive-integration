package org.jbpm.google.handlers;

import java.util.HashMap;
import java.util.Map;

import org.jbpm.google.GoogleDriveService;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

public class GoogleDriveGet implements WorkItemHandler {

	GoogleDriveService drive = new GoogleDriveService();
	
	@Override
	public void abortWorkItem(WorkItem item, WorkItemManager manager) {
		System.err.println("Aborting GoogleDriveGet workitem " + item.getName() + "("
				+ item.getId() + ")");
		manager.abortWorkItem(item.getId());
	}

	@Override
	public void executeWorkItem(WorkItem item, WorkItemManager manager) {

		Map<String, Object> results = new HashMap<String, Object>(); 
		
		results.put("File", drive.getFile((String)item.getParameter("FileId")));
		
		manager.completeWorkItem(item.getId(), results);
	}

}
