package com.example.pdf.dto;

import lombok.Data;
import lombok.Setter;

// DTO class for request body
@Setter
@Data
public class PdfRequest {
    private String templatePath;
    private String content;
    private float x;
    private float y;

    public PdfRequest() {}

}
