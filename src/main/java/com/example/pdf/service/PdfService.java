package com.example.pdf.service;

import com.example.pdf.dto.PdfRequest;
import com.example.pdf.dto.PdfResponseDTO;
import com.example.pdf.dto.UserResponseDTO;
import com.example.pdf.entity.Pdf;
import com.example.pdf.entity.User;
import com.example.pdf.repository.PdfRepository;
import com.example.pdf.repository.UserRepository;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PdfService {

    private final PdfRepository pdfRepository;

    public PdfService(PdfRepository pdfRepository){
        this.pdfRepository=pdfRepository;
    }

    @Autowired
    private UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(PdfService.class);


    public byte[] generatePdf(PdfRequest request) throws IOException {
        //Create the PDF and get the byte content
        byte[] pdfBytes;

        try (PDDocument document = createPdfFromTemplate(
                request.getTemplatePath(),
                request.getContent(),
                request.getX(),
                request.getY());
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            document.save(outputStream);
            pdfBytes = outputStream.toByteArray();
        }

        // 2. Save to filesystem and DB
        String fileName = "generated_" + System.currentTimeMillis() + ".pdf";
        String storageDir = "pdf-storage";
        File dir = new File(storageDir);
        if (!dir.exists()) dir.mkdirs();

        File outputFile = new File(dir, fileName);
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(pdfBytes);
        }

        Pdf pdfEntity = new Pdf();
        pdfEntity.setFileName(fileName);
        pdfEntity.setFilePath(outputFile.getAbsolutePath());
        pdfEntity.setGeneratedAt(LocalDateTime.now());

        pdfRepository.save(pdfEntity);

        //return the generated PDF bytes for the API response
        return pdfBytes;
    }


    public PDDocument createPdfFromTemplate(String templatePath, String content, float x, float y) throws IOException {
        File file = new File(templatePath);

        if (!file.exists()) {
            throw new FileNotFoundException("Template not found at path: " + templatePath);
        }

        PDDocument document = PDDocument.load(file);
        PDFont font = PDType1Font.HELVETICA;

        Optional.ofNullable(document.getPage(0)).ifPresent(page -> {
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)) {
                contentStream.beginText();
                contentStream.setFont(font, 12);
                contentStream.newLineAtOffset(x, y);
                contentStream.showText(content);
                contentStream.endText();
            } catch (IOException e) {
                throw new UncheckedIOException("Error writing content to PDF", e);
            }
        });

        return document;
    }

    public List<Pdf> getAllPdfs() {
            List<Pdf> pdfList = pdfRepository.findAll();

            if (pdfList.isEmpty()) {
                logger.warn("No PDFs found in the database");
            } else {
                logger.info("Fetched {} PDFs");
            }

            return pdfList;
        }

            public void deleteById(Long id) {

                pdfRepository.findById(id).orElseThrow(() ->
                        new IllegalArgumentException("PDF with ID " + id + " not found.")
                );

                pdfRepository.deleteById(id);
            }


    public PdfResponseDTO getPdfById(Long id) {

        Pdf pdf = pdfRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("PDF with ID " + id + " does not exist"));

        PdfResponseDTO dto = new PdfResponseDTO();
        dto.setId(pdf.getId());
        dto.setFileName(pdf.getFileName());
        dto.setGeneratedAt(pdf.getGeneratedAt());
        return dto;
    }

    public byte[] mergePdfs(List<MultipartFile> files) {
        try {
            // Create PDFMergerUtility instance
            PDFMergerUtility merger = new PDFMergerUtility();

            // Output stream to hold the merged result
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            merger.setDestinationStream(outputStream);

            // Add each file input stream to the merger
            for (MultipartFile file : files) {
                InputStream inputStream = file.getInputStream();
                merger.addSource(inputStream);
            }

            // Merge all documents
            merger.mergeDocuments(null); // Use default memory settings

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error while merging PDFs: " + e.getMessage(), e);
        }
    }

    public byte[] splitPdf(MultipartFile file, int startPage, int endPage) {
        try (PDDocument originalDoc = PDDocument.load(file.getInputStream());
             PDDocument splitDoc = new PDDocument();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            int totalPages = originalDoc.getNumberOfPages();

            if (startPage < 1 || endPage > totalPages || startPage > endPage) {
                throw new IllegalArgumentException("Invalid start or end page range.");
            }

            // PDFBox uses 0-based index for pages
            for (int i = startPage - 1; i < endPage; i++) {
                splitDoc.addPage(originalDoc.getPage(i));
            }

            splitDoc.save(outputStream);
            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error while splitting PDF: " + e.getMessage(), e);
        }
    }

    @Value("${pdf.storage.directory}")
    private String storageDirectoryPath;


    public void renamePdf(Long id, String newFileName) {
        Pdf pdf = pdfRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("PDF with ID " + id + " does not exist."));

        File oldFile = new File(pdf.getFilePath());
        String newPath = storageDirectoryPath + File.separator + newFileName;

        File newFile = new File(newPath);

        if (newFile.exists()) {
            throw new RuntimeException("A file with the new name already exists.");
        }

        boolean renamed = oldFile.renameTo(newFile);
        if (!renamed) {
            throw new RuntimeException("Failed to rename the file on the filesystem.");
        }

        pdf.setFileName(newFileName);
        pdf.setFilePath(newPath);

        pdfRepository.save(pdf);
    }

    public byte[] editPdfContentById(Long id, String textToAdd) {
        Pdf pdf = pdfRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("PDF with ID " + id + " does not exist."));

        File file = new File(pdf.getFilePath());
        if (!file.exists()) {
            throw new RuntimeException("PDF file not found on the filesystem.");
        }

        try (PDDocument document = PDDocument.load(file);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            PDPage page = document.getPage(0); // you can loop through pages if needed

            PDPageContentStream contentStream = new PDPageContentStream(document, page,
                    PDPageContentStream.AppendMode.APPEND, true);

            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(100, 700); // set position
            contentStream.showText(textToAdd);
            contentStream.endText();
            contentStream.close();

            document.save(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to edit PDF content", e);
        }
    }

    public byte[] editPdfContent(MultipartFile file, String textToAdd) {
        try (InputStream inputStream = file.getInputStream();
             PDDocument document = PDDocument.load(inputStream);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // Edit the first page (or loop through all pages if needed)
            PDPage page = document.getPage(0);

            PDPageContentStream contentStream = new PDPageContentStream(document, page,
                    PDPageContentStream.AppendMode.APPEND, true);

            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(100, 700); // Customize position
            contentStream.showText(textToAdd);
            contentStream.endText();
            contentStream.close();

            document.save(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to edit PDF content", e);
        }
    }
}







