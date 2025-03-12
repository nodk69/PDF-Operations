package com.example.pdf.controller;

import com.example.pdf.dto.PdfRequest;
import com.example.pdf.dto.PdfResponseDTO;
import com.example.pdf.entity.Pdf;
import com.example.pdf.service.PdfService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/pdf")
public class PdfController {

    private final PdfService pdfService;

    public PdfController(PdfService pdfService){
        this.pdfService= pdfService;
    }

    private final Logger logger = LoggerFactory.getLogger(PdfController.class);

    //Generating the pdf here
    @PostMapping("/generate")
    public ResponseEntity<byte[]> generatePdf(@RequestBody PdfRequest request) {
        try {
            byte[] pdfBytes = pdfService.generatePdf(request);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "generated.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (IOException e) {
            log.error("Error generating PDF", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PdfResponseDTO> getById(@PathVariable Long id){

        try {
            return ResponseEntity.ok(pdfService.getPdfById(id));
        }catch (Exception e){
            logger.error("No PDF with id {}exist", id);
            return ResponseEntity.noContent().build();
        }
    }

    //fetching the pdf here
    @GetMapping
    public ResponseEntity<List<Pdf>> getAllPdfs() {
        try {
            return ResponseEntity.ok(pdfService.getAllPdfs());
        } catch (Exception e) {
            logger.error("Error retrieving PDFs", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        try{
            pdfService.deleteById(id);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            logger.error("No such file exist",e);
            return ResponseEntity.noContent().build();
        }
    }

}

