package com.example.pdf.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "pdf_metadata")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PdfMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String author;
    private String keywords;
    @Column(name = "`key`")
    private String key;
    private String value;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "pdf_id", nullable = false)
    private Pdf pdf;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
