
# 📄 PDF Management System - Spring Boot Application

A full-featured PDF processing REST API built with **Spring Boot**, supporting PDF generation, uploading, downloading, editing (name & content), merging, splitting, and searching by filename. The application uses **MySQL** as its persistent storage and supports file storage on disk.

---

## 🚀 Features

- ✅ Generate PDF from request data
- ✅ Upload PDF files
- ✅ Download PDFs by ID
- ✅ Search PDFs by filename
- ✅ Merge multiple PDF files
- ✅ Split PDF by page range
- ✅ Edit PDF file names
- ✅ Modify existing PDF content (in DB or on-the-fly)
- ✅ Delete PDFs by ID

---

## 🧰 Tech Stack

| Layer       | Technology                  |
|------------|------------------------------|
| Backend     | Spring Boot, Spring MVC      |
| Database    | MySQL                        |
| Logging     | SLF4J + Logback              |
| File Upload | MultipartFile                |
| PDF Tools   | Apache PDFBox or iText (as used) |
| Build Tool  | Maven                        |
| Java Version| Java 17+ (recommended)       |

---

## ⚙️ Setup Instructions

### ✅ Prerequisites

- Java 17+
- Maven
- MySQL

### ⚙️ Configure `application.properties`

```properties
# Server Configuration
server.port=8080

# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/pdf_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=nadeem
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.format_sql=true

# File Upload Configuration
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Logging Configuration
logging.level.com.example.pdf=DEBUG

spring.application.name=pdf

pdf.storage.directory=D:/Spring Boot/pdf/pdf-storage
```

## 📂 API Endpoints

### 📌 Base Paths:
- `http://localhost:8080/pdf`
- `http://localhost:8080/pdf/file`
- `http://localhost:8080/pdf/operations`

### 🧾 PDF Generation & Retrieval

| Method | Endpoint                      | Description                    |
|--------|-------------------------------|--------------------------------|
| POST   | `/pdf/generate`               | Generate PDF from request body |
| GET    | `/pdf/{id}`                   | Get PDF metadata by ID         |
| GET    | `/pdf`                        | Get all stored PDFs            |
| DELETE | `/pdf/{id}`                   | Delete PDF by ID               |

---

### 📤 Upload & 📥 Download PDFs

| Method | Endpoint                          | Description              |
|--------|-----------------------------------|--------------------------|
| POST   | `/pdf/file/upload`                | Upload a PDF file        |
| GET    | `/pdf/file/download/{id}`         | Download PDF by ID       |
| GET    | `/pdf/file/search?keyword=name`   | Search PDFs by filename  |

---

### 🛠️ PDF Operations

| Method | Endpoint                                | Description                       |
|--------|-----------------------------------------|-----------------------------------|
| POST   | `/pdf/operations/merge`                 | Merge multiple uploaded PDFs      |
| POST   | `/pdf/operations/split`                 | Split PDF by page range           |
| PUT    | `/pdf/operations/{id}/editFileName`     | Rename a PDF by ID                |
| PUT    | `/pdf/operations/{id}/edit-content`     | Edit content of stored PDF by ID |
| PUT    | `/pdf/operations/edit-content`          | Edit uploaded PDF (in memory)     |

---

## 🐞 Logging

Uses `@Slf4j` and `LoggerFactory` for structured error and debug logs.

---

## 🗂️ Project Structure

```
src/
├── controller/
│   ├── PdfController.java
│   ├── PdfFileController.java
│   └── PdfOperationController.java
├── dto/
│   ├── PdfRequest.java
│   └── PdfResponseDTO.java
├── service/
│   ├── PdfService.java
│   └── PdfFileService.java
├── entity/
│   └── Pdf.java
├── repository/
│   └── PdfRepository.java
```
