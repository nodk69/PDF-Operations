package com.example.pdf.controller;

import com.example.pdf.dto.PdfResponseDTO;
import com.example.pdf.service.PdfFileService;
import com.example.pdf.service.PdfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/pdf/file")
public class PdfFileController {

    @Autowired
    private PdfFileService pdfFileService;

    @Autowired
    private PdfService pdfService;

    private final Logger logger = LoggerFactory.getLogger(PdfFileController.class);

    @PostMapping("/upload")
    public ResponseEntity<String> uploadPdf(@RequestParam("file") MultipartFile file) {
        try {
            pdfFileService.uploadPdf(file);
            return ResponseEntity.ok("PDF uploaded successfully.");
        } catch (Exception e) {
            logger.error("Upload failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) {
        try {
            PdfResponseDTO pdfFile = pdfService.getPdfById(id); // fetch full PdfFile entity
            byte[] pdfData = pdfFileService.downloadPdfById(id); // fetch the file content from disk

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pdfFile.getFileName())
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfData);
        } catch (Exception e) {
            logger.error("PDF not found for id {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @GetMapping("/search")
    public ResponseEntity<List<PdfResponseDTO>> searchByFilename(@RequestParam String keyword) {
        return ResponseEntity.ok(pdfFileService.searchByFilename(keyword));
    }


//    @GetMapping("/my-pdfs")
//    public ResponseEntity<List<PdfResponseDTO>> getMyPdfs() {
//        return ResponseEntity.ok(pdfFileService.getPdfsUploadedByCurrentUser());
//    }


}
