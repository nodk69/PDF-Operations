package com.example.pdf.model;

import lombok.Data;
import java.util.Map;

@Data
public class PdfFormData {
    private String templateId;
    private Map<String, String> formFields;
} 