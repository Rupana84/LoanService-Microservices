package com.loan_services.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private Long id;       // Unique identifier for the book
    private String title;  // Book title
    private String author; // Book author
    private boolean available; // Availability status (true = available, false = already loaned)
}