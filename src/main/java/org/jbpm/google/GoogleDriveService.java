package org.jbpm.google;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.logging.Logger;
import org.jbpm.google.util.AuthHelper;

public class GoogleDriveService {

	Logger log = Logger.getLogger(GoogleDriveService.class);

	/**
	 * Be sure to specify the name of your application. If the application name
	 * is {@code null} or blank, the application will log a warning. Suggested
	 * format is "MyCompany-ProductName/1.0".
	 */
	private static final String APPLICATION_NAME = "jBPM Integration";

	private Drive service;

	public GoogleDriveService() {
		// Create a new authorized API client
		try {
			service = new Drive.Builder(
					GoogleNetHttpTransport.newTrustedTransport(),
					JacksonFactory.getDefaultInstance(), AuthHelper.authorize())
					.setApplicationName(APPLICATION_NAME).build();
		} catch (Exception e) {
			log.error("An error occured during connection setup: ", e);
		}
	}

	/**
	 * Get file's metadata.
	 * 
	 * @param fileId
	 *            ID of the file to print metadata for.
	 */
	public File getFile(String fileId) {
		File file = null;
		try {
			file = service.files().get(fileId).execute();
		} catch (IOException e) {
			log.error("An error occured while getting file metadata: ", e);
		}

		return file;
	}

	/**
	 * Download a file's content.
	 * 
	 * @param file
	 *            Drive File instance.
	 * @return InputStream containing the file's content if successful,
	 *         {@code null} otherwise.
	 */
	public InputStream downloadFile(File file) {
		if (file.getDownloadUrl() != null && file.getDownloadUrl().length() > 0) {
			try {
				HttpResponse resp = service.getRequestFactory()
						.buildGetRequest(new GenericUrl(file.getDownloadUrl()))
						.execute();
				return resp.getContent();
			} catch (IOException e) {
				log.error("An error occurred while downloading file: ", e);
				return null;
			}
		} else {
			// The file doesn't have any content stored on Drive.
			return null;
		}
	}

	/**
	 * Insert new file.
	 * 
	 * @param title
	 *            Title of the file to insert, including the extension.
	 * @param description
	 *            Description of the file to insert.
	 * @param parentId
	 *            Optional parent folder's ID.
	 * @param mimeType
	 *            MIME type of the file to insert.
	 * @param filename
	 *            Filename of the file to insert.
	 * @return Inserted file metadata if successful, {@code null} otherwise.
	 */
	public File insertFile(String title, String description, String parentId,
			String filename) {
		// File's metadata.
		String mimeType = URLConnection.guessContentTypeFromName(filename);

		File body = new File();
		body.setTitle(title);
		body.setDescription(description);
		body.setMimeType(mimeType);

		// Set the parent folder.
		if (parentId != null && parentId.length() > 0) {
			body.setParents(Arrays.asList(new ParentReference().setId(parentId)));
		}

		// File's content.
		java.io.File fileContent = new java.io.File(filename);
		FileContent mediaContent = new FileContent(mimeType, fileContent);
		try {
			File file = service.files().insert(body, mediaContent).execute();

			// Uncomment the following line to print the File ID.
			// System.out.println("File ID: %s" + file.getId());

			return file;
		} catch (IOException e) {
			log.error("An error occured while uploading file: ", e);
			return null;
		}
	}

	/**
	 * Update an existing file's metadata and content.
	 * 
	 * @param fileId
	 *            ID of the file to update.
	 * @param newTitle
	 *            New title for the file.
	 * @param newDescription
	 *            New description for the file.
	 * @param newMimeType
	 *            New MIME type for the file.
	 * @param newFilepath
	 *            Filename of the new content to upload.
	 * @return Updated file metadata if successful, {@code null} otherwise.
	 */
	public File updateFile(String fileId, String newTitle,
			String newDescription, String newFilepath) {
		try {
			String newMimeType = URLConnection
					.guessContentTypeFromName(newFilepath);

			// First retrieve the file from the API.
			File file = service.files().get(fileId).execute();

			// File's new metadata.
			file.setTitle(newTitle);
			file.setDescription(newDescription);
			file.setMimeType(newMimeType);

			// File's new content.
			java.io.File fileContent = new java.io.File(newFilepath);
			FileContent mediaContent = new FileContent(newMimeType, fileContent);

			// Send the request to the API.
			File updatedFile = service.files()
					.update(fileId, file, mediaContent).execute();

			return updatedFile;
		} catch (IOException e) {
			log.error("An error occurred while updating file: ", e);
			return null;
		}
	}

	/**
	 * Permanently delete a file, skipping the trash.
	 * 
	 * @param fileId
	 *            ID of the file to delete.
	 */
	public void deleteFile(String fileId) {
		try {
			service.files().delete(fileId).executeUnparsed();
		} catch (IOException e) {
			log.error("An error occurred while deleting file: ", e);
		}
	}

	/**
	 * Retrieve a list of File resources.
	 * 
	 * @return List of File resources.
	 */
	public List<File> retrieveAllFiles() throws IOException {
		List<File> result = new ArrayList<File>();
		Files.List request = service.files().list();

		do {
			try {
				FileList files = request.execute();

				result.addAll(files.getItems());
				request.setPageToken(files.getNextPageToken());
			} catch (IOException e) {
				log.error("An error occurred while retrieving files: ", e);
				request.setPageToken(null);
			}
		} while (request.getPageToken() != null
				&& request.getPageToken().length() > 0);

		return result;
	}

}
