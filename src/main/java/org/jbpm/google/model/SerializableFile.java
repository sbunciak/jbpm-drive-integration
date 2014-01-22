package org.jbpm.google.model;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.io.StringReader;

import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.model.File;

/**
 * Serializable wrapper class for
 * {@link com.google.api.services.drive.model.File File}.
 * 
 * @author sbunciak
 * 
 */
public class SerializableFile implements Serializable {

	private static final long serialVersionUID = 8929757917530946055L;

	private transient File file;

	public SerializableFile() {
		// intentionally left blank for now
	}

	public SerializableFile(File file) {
		setFile(file);
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		//System.out.println("Serializing: Start.");
		//System.out.println("Serializing: Default object write.");
		out.defaultWriteObject();
		//System.out.println("Serializing: Custom handling.");
		out.writeUTF(file.toString());
		//System.out.println("Serializing: Done.");
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		//System.out.println("Deserializing: Start.");
		//System.out.println("Deserializing: Default object read.");
		in.defaultReadObject();
		//System.out.println("Deserializing: Custom handling.");

		file = new JsonObjectParser(new JacksonFactory()).parseAndClose(
				new StringReader(in.readUTF()), File.class);

		//System.out.println("Deserializing: Done.");
	}

	@SuppressWarnings("unused")
	private void readObjectNoData() throws ObjectStreamException {
		// intentionally left blank for now
		System.out.println("(De)Serializing: readObjeactNoData()");
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
