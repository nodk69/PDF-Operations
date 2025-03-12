package com.example.pdf.exception;

public class Exceptions {
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    public static class DuplicateEmailException extends RuntimeException {
        public DuplicateEmailException(String message) {
            super(message);
        }
    }

    public static class InvalidCredentialsException extends RuntimeException {
        public InvalidCredentialsException(String message) {
            super(message);
        }
    }

    public static class PdfProcessingException extends RuntimeException {
        public PdfProcessingException(String message) {
            super(message);
        }

        public PdfProcessingException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}