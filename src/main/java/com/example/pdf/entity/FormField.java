package com.example.pdf.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "form_fields")
@Data
public class FormField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;


    private String type;


    private float x;


    private float y;


    private float width;


    private float height;


    private String defaultValue;


    private boolean required = false;

    @ManyToOne
    @JoinColumn(name = "template_id", nullable = false)
    private FormTemplate template;
} 