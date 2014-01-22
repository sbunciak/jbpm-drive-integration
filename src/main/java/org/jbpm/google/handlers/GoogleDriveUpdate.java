package org.jbpm.google.handlers;

import java.util.HashMap;
import java.util.Map;

import org.jbpm.google.GoogleDriveService;
import org.jbpm.google.model.SerializableFile;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

public class GoogleDriveUpdate implements WorkItemHandler {

	GoogleDriveService drive = new GoogleDriveService();

	public void abortWorkItem(WorkItem item, WorkItemManager manager) {
		System.err.println("Aborting GoogleDriveUpdate workitem "
				+ item.getName() + "(" + item.getId() + ")");
		manager.abortWorkItem(item.getId());
	}

	public void executeWorkItem(WorkItem item, WorkItemManager manager) {

		Map<String, Object> results = new HashMap<String, Object>();

		String fileId = (String) item.getParameter("FileId");
		String newTitle = (String) item.getParameter("NewTitle");
		String newDescription = (String) item.getParameter("NewDescription");
		String newFilepath = (String) item.getParameter("NewFilepath");

		results.put(
				"NewFile",
				new SerializableFile(drive.updateFile(fileId, newTitle,
						newDescription, newFilepath)));

		manager.completeWorkItem(item.getId(), results);
	}

}
