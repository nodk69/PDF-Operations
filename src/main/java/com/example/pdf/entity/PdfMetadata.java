package com.example.pdf.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "pdf_metadata")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PdfMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; // 📌 Title of the document

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Pdf getPdf() {
        return pdf;
    }

    public void setPdf(Pdf pdf) {
        this.pdf = pdf;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    private String description; // Optional

    private String author; // 📌 Author of the PDF document

    @Column(length = 500)
    private String keywords; // 📌 Tags or keywords for searchability

    @Column(name = "`key`")
    private String key; // 🔑 New field to store metadata key

    private String value; // 📌 New field to store metadata value

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "pdf_id", nullable = false)
    private Pdf pdf; // The associated PDF file

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt; // 📌 Auto-generated creation timestamp

    @UpdateTimestamp
    private LocalDateTime updatedAt; // 📌 Auto-updated modification timestamp
}
