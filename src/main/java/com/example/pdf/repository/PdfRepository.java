package com.example.pdf.repository;

import com.example.pdf.entity.Pdf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PdfRepository extends JpaRepository<Pdf, Long> {

    // Correct query method to find PDFs by the ID of the associated user
//    List<Pdf> findByGeneratedBy_Id(Long userId);
//
//    boolean existsByFilename(String filename);

    List<Pdf> findByFileNameContainingIgnoreCase(String keyword);


//    List<Pdf> findByUploadedBy(String username);
}