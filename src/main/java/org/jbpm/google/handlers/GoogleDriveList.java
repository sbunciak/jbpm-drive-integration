package org.jbpm.google.handlers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.google.GoogleDriveService;
import org.jbpm.google.model.SerializableFileList;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

/**
 * Work item handler implementation for listing all files from Google Drive.
 * 
 * @author sbunciak
 * 
 */
public class GoogleDriveList implements WorkItemHandler {

	GoogleDriveService drive = new GoogleDriveService();

	public void abortWorkItem(WorkItem item, WorkItemManager manager) {
		System.err.println("Aborting GoogleDriveList workitem "
				+ item.getName() + "(" + item.getId() + ")");
		manager.abortWorkItem(item.getId());
	}

	public void executeWorkItem(WorkItem item, WorkItemManager manager) {

		Map<String, Object> results = new HashMap<String, Object>();

		try {
			results.put("Files",
					new SerializableFileList(drive.retrieveAllFiles()));
		} catch (IOException e) {
			e.printStackTrace();
			this.abortWorkItem(item, manager);
		}

		manager.completeWorkItem(item.getId(), results);
	}

}
