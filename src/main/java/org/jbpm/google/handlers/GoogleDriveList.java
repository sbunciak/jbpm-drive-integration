package org.jbpm.google.handlers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.google.GoogleDriveService;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

public class GoogleDriveList implements WorkItemHandler {

	GoogleDriveService drive = new GoogleDriveService();

	@Override
	public void abortWorkItem(WorkItem item, WorkItemManager manager) {
		System.err.println("Aborting GoogleDriveList workitem " + item.getName() + "("
				+ item.getId() + ")");
		manager.abortWorkItem(item.getId());
	}

	@Override
	public void executeWorkItem(WorkItem item, WorkItemManager manager) {
		
		Map<String, Object> results = new HashMap<String, Object>(); 
		try {
			results.put("Files", drive.retrieveAllFiles());
		} catch (IOException e) {
			e.printStackTrace();
			this.abortWorkItem(item, manager);
		}
		
		manager.completeWorkItem(item.getId(), results);
	}

}
