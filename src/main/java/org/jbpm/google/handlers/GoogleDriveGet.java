package org.jbpm.google.handlers;

import java.util.HashMap;
import java.util.Map;

import org.jbpm.google.GoogleDriveService;
import org.jbpm.google.model.SerializableFile;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

import com.google.api.services.drive.model.File;

public class GoogleDriveGet implements WorkItemHandler {

	GoogleDriveService drive = new GoogleDriveService();

	public void abortWorkItem(WorkItem item, WorkItemManager manager) {
		System.err.println("Aborting GoogleDriveGet workitem " + item.getName()
				+ "(" + item.getId() + ")");
		manager.abortWorkItem(item.getId());
	}

	public void executeWorkItem(WorkItem item, WorkItemManager manager) {
		// get Google File
		File file = (File) drive.getFile((String) item.getParameter("FileId"));

		Map<String, Object> results = new HashMap<String, Object>();
		// put wrapped (serializable) file into results
		results.put("File", new SerializableFile(file));

		manager.completeWorkItem(item.getId(), results);
	}

}
