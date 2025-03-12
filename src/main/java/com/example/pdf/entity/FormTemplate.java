package com.example.pdf.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "form_templates")
@Data
public class FormTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;


    private String description;


    private String templatePath;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] pdfData;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL)
    private List<FormField> fields;


    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public byte[] getPdfData() {
        return pdfData;
    }

    public void setPdfData(byte[] pdfData) {
        this.pdfData = pdfData;
    }
} 