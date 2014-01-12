package org.jbpm.google.handlers;

import java.util.HashMap;

import org.jbpm.google.GoogleDriveService;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

public class GoogleDriveDelete implements WorkItemHandler {

	GoogleDriveService drive = new GoogleDriveService();

	@Override
	public void abortWorkItem(WorkItem item, WorkItemManager manager) {
		System.err.println("Aborting GoogleDriveDelete workitem "
				+ item.getName() + "(" + item.getId() + ")");
		manager.abortWorkItem(item.getId());
	}

	@Override
	public void executeWorkItem(WorkItem item, WorkItemManager manager) {

		drive.deleteFile((String) item.getParameter("FileId"));

		manager.completeWorkItem(item.getId(), new HashMap<String, Object>());
	}

}
