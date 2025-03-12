package com.example.pdf.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PdfResponseDTO {
    private Long id;
   // private String title;
    private String fileName;
    //private String contentType;
    //private Long size;
    private LocalDateTime generatedAt;
    //private String uploadedBy;
}
