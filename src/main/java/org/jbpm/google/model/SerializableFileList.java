package org.jbpm.google.model;

import java.util.ArrayList;
import java.util.Collection;

import com.google.api.services.drive.model.File;

/**
 * 
 * @author sbunciak
 *
 */
public class SerializableFileList extends ArrayList<SerializableFile> {

	private static final long serialVersionUID = -611841495591939911L;

	public SerializableFileList() {
		super();
	}

	public SerializableFileList(Collection<File> files) {
		this();
		/*
		 * TODO: Ugly workaround for Issue#1 (NotSerializableException). Should
		 * be fixed in future versions.
		 */
		for (File file : files) {
			this.add(new SerializableFile(file));
		}
	}

}
