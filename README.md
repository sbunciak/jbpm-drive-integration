jbpm-drive-integration
======================

Sample integration of Google Drive withinin jBPM 6.
---------------------------------------------------

## About
This project demonstrates how to interact with Google Drive from jBPM business process. It contains custom Workitem handler implementation which is responsible for authenticating using OAuth 2.0 protocol authorizing access to user data, and providing an easy-to-use API for communication with the Google Drive services.

## Prerequisites
Before you start, you need to register your application for Drive API in Google Developers Console at https://cloud.google.com/console/flows/enableapi?apiid=drive and download application specific client_secrets.json. This JSON file needs to be placed in [src/main/resources](https://github.com/sbunciak/jbpm-drive-integration/blob/master/src/main/resources/client_secrets.json "client_secrets.json")


## Installation
+ You need to build this maven project:

        mvn clean install -DskipTests=true

+ Copy artifacts to jBPM installer:

        copy target/*.jar and target/lib/*.jar to jbpm-installer/dependencies

+ Add to jbpm-installer/conf/META-INF/CustomWorkItemHandlers.conf following definitions:

        ...
        "GoogleDriveGet" : new org.jbpm.google.handlers.GoogleDriveGet(),
        "GoogleDriveList" : new org.jbpm.google.handlers.GoogleDriveList(),
        "GoogleDriveUpload" : new org.jbpm.google.handlers.GoogleDriveUpload(),
        "GoogleDriveDelete" : new org.jbpm.google.handlers.GoogleDriveDelete(),
        "GoogleDriveUpdate" : new org.jbpm.google.handlers.GoogleDriveUpdate(),
        ...

+ Finally, run jbpm-installer, see http://docs.jboss.org/jbpm/v6.0/userguide/jBPMInstaller.html

## Ready to go
Now You can create new project in jBPM console with use of Google Drive services. All you ned is to create WorkItemDefinitions in your jBPM project workspace according to [WorkItems.wid](https://github.com/sbunciak/jbpm-drive-integration/blob/master/src/main/resources/META-INF/WorkItems.wid "WorkItems.wid")

## Sample
+ Create Process:
![Process designer](https://raw2.github.com/sbunciak/jbpm-drive-integration/master/img/process_designer.png)
+ Complete task:
![Task](https://raw2.github.com/sbunciak/jbpm-drive-integration/master/img/task.png)
+ Voilà - file uploaded to Google Drive:
![Uploaded file](https://raw2.github.com/sbunciak/jbpm-drive-integration/master/img/drive.png)
