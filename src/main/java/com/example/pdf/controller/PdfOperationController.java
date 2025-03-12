package com.example.pdf.controller;

import com.example.pdf.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pdf/operations")
@RequiredArgsConstructor
public class PdfOperationController {

    @Autowired
    private PdfService pdfService;

    @PostMapping("/merge")
    public ResponseEntity<byte[]> mergePdfs(@RequestParam("files") List<MultipartFile> files) {
        try {
            byte[] mergedPdf = pdfService.mergePdfs(files);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=merged.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(mergedPdf);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/split")
    public ResponseEntity<byte[]> splitPdf(@RequestParam("file") MultipartFile file,
                                           @RequestParam int startPage,
                                           @RequestParam int endPage) {
        try {
            byte[] splitPdf = pdfService.splitPdf(file, startPage, endPage);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=split.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(splitPdf);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //Editing PDF name
    @PutMapping("/{id}/editFileName")
    public ResponseEntity<String> editPdfName(@PathVariable Long id,
                                          @RequestBody Map<String, String> updates) {
        try {
            String newFileName = updates.get("newFileName");
            pdfService.renamePdf(id, newFileName);
            return ResponseEntity.ok("PDF renamed successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to rename PDF.");
        }
    }

    //this one will fetch the pdf from the database and can edit
    @PutMapping("/{id}/edit-content")
    public ResponseEntity<byte[]> editPdfContentById(@PathVariable Long id,
                                                     @RequestBody Map<String, String> payload) {
        try {
            String textToAdd = payload.get("text");
            byte[] editedPdf = pdfService.editPdfContentById(id, textToAdd);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=edited.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(editedPdf);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //we can do in memory edit
    @PutMapping("/edit-content")
    public ResponseEntity<byte[]> editUploadedPdf(@RequestParam("file") MultipartFile file,
                                                  @RequestParam("text") String textToAdd) {
        try {
            byte[] editedPdf = pdfService.editPdfContent(file, textToAdd);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=edited-uploaded.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(editedPdf);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }






}