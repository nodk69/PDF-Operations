package com.example.pdf.service;

import com.example.pdf.dto.PdfResponseDTO;
import com.example.pdf.entity.Pdf;
import com.example.pdf.repository.PdfRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PdfFileService {

    private final Logger logger = LoggerFactory.getLogger(PdfFileService.class);

    @Autowired
    private PdfRepository pdfRepository;

    //this will create a folder name uploads
    private static final String UPLOAD_DIR = "uploads";

    @Transactional
    public void uploadPdf(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }

        // Create upload directory if it doesn't exist
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            boolean created = uploadDir.mkdirs();
            if (!created) {
                throw new IOException("Could not create upload directory");
            }
        }

        // Save file to disk
        String filePath = UPLOAD_DIR + File.separator + file.getOriginalFilename();
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(file.getBytes());
        }

        // Save metadata to DB
        Pdf pdf = new Pdf();
        pdf.setFileName(file.getOriginalFilename());
        pdf.setGeneratedAt(LocalDateTime.now()); // Use `uploadedAt` if more specific
        // pdf.setUploadedBy(...) // optional: link to User entity if needed

        pdfRepository.save(pdf);
        logger.info("PDF '{}' uploaded and saved to DB", file.getOriginalFilename());
    }

    public byte[] downloadPdfById(Long id) {
        Pdf pdfFile = pdfRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("PDF not found with id: " + id));

        String path = pdfFile.getFilePath();
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read PDF file from disk: " + path, e);
        }

    }

    public List<PdfResponseDTO> searchByFilename(String keyword) {
        List<Pdf> matchingFiles = pdfRepository.findByFileNameContainingIgnoreCase(keyword);

        return matchingFiles.stream().map(pdf -> {
            PdfResponseDTO dto = new PdfResponseDTO();
            dto.setId(pdf.getId());
            dto.setFileName(pdf.getFileName());
            dto.setGeneratedAt(pdf.getGeneratedAt());
            return dto;
        }).collect(Collectors.toList());

    }

//    public List<PdfResponseDTO> getPdfsUploadedByCurrentUser() {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        // Fetch PDFs uploaded by this user
//        List<Pdf> pdfs = pdfRepository.findByUploadedBy(username);
//
//        return pdfs.stream().map(pdf -> {
//            PdfResponseDTO dto = new PdfResponseDTO();
//            dto.setId(pdf.getId());
//            dto.setFileName(pdf.getFileName());
//            dto.setGeneratedAt(pdf.getGeneratedAt());
//            return dto;
//        }).collect(Collectors.toList());
//    }
}
