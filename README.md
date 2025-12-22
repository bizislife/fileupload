# File Upload Project

A Spring Boot application with MySQL backend and a modern HTML/JavaScript/CSS frontend for uploading images, PDFs, and text files. Files are stored as BLOBs in the database. The UI supports drag-and-drop and click-to-upload, shows upload progress, and allows removal of files from both UI and DB.

## Features
- Upload images, PDFs, and text files
- Drag-and-drop or click to select files
- Upload progress bar for each file
- Remove files from UI and database

## Tech Stack
- Spring Boot (Java)
- MySQL
- HTML, CSS, JavaScript (vanilla)

## Setup
1. Create a MySQL database named `fileuploaddb` and update credentials in `src/main/resources/application.properties`.
2. Build and run the Spring Boot application:
   ```
   mvn spring-boot:run
   ```
3. Open [http://localhost:8080/](http://localhost:8080/) in your browser.

## Notes
- Max file size: 10MB per file (configurable in `application.properties`)
- Uploaded files are stored as BLOBs in MySQL

## Folder Structure
- `src/main/java/com/example/fileupload/` - Java source code
- `src/main/resources/static/` - Frontend (HTML, CSS, JS)
- `src/main/resources/application.properties` - DB and app config

---
Replace `yourpassword` in `application.properties` with your MySQL password.
