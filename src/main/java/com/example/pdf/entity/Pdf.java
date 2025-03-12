package com.example.pdf.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "pdf")
public class Pdf {

    // Getters and setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;


    private LocalDateTime generatedAt;

    private String filePath;

//    @JsonBackReference
//    @ManyToOne
   // @JoinColumn(name = "user_id", nullable = false) // Ensure generatedBy is not null
   // private User generatedBy; // The user who generated the PDF

    @Lob
   // @Column(columnDefinition = "BLOB", nullable = false)
    private byte[] pdfData;

    @OneToOne(mappedBy = "pdf", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PdfMetadata metadata;

    public void generatedAt(LocalDateTime now) {
        this.generatedAt = LocalDateTime.now();
    }

}