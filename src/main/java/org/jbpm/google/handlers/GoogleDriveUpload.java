package org.jbpm.google.handlers;

import java.util.HashMap;
import java.util.Map;

import org.jbpm.google.GoogleDriveService;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

public class GoogleDriveUpload implements WorkItemHandler {

	GoogleDriveService drive = new GoogleDriveService();

	@Override
	public void abortWorkItem(WorkItem item, WorkItemManager manager) {
		System.err.println("Aborting GoogleDriveUpload workitem "
				+ item.getName() + "(" + item.getId() + ")");
		manager.abortWorkItem(item.getId());
	}

	@Override
	public void executeWorkItem(WorkItem item, WorkItemManager manager) {

		Map<String, Object> results = new HashMap<String, Object>();

		String title = (String) item.getParameter("Title");
		String description = (String) item.getParameter("Description");
		String filepath = (String) item.getParameter("Filepath");

		try {
			results.put("File",
					drive.insertFile(title, description, null, filepath));
		} catch (Exception e) {
			e.printStackTrace();
			this.abortWorkItem(item, manager);
		}

		manager.completeWorkItem(item.getId(), results);
	}

}
