package com.loan_services.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "loans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Unique Loan ID

    @Column(nullable = false)
    private Long bookId;  // The ID of the book being loaned

    @Column(nullable = false)
    private String borrowerName;  // Name of the person borrowing

    @Column(nullable = false, updatable = false)
    private LocalDate loanDate;  // When the loan was created

    @Column(nullable = true)
    private LocalDate returnDate;  // When the book should be returned

    @Column(nullable = false)
    private Boolean returned = Boolean.FALSE;  //  Ensure correct naming
}