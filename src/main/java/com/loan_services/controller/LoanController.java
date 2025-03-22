package com.loan_services.controller;

import com.loan_services.entity.Loan;
import com.loan_services.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/loans")
public class LoanController {

    @Autowired
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    // ✅ Create a new loan (POST)
    @PostMapping("/")
    public ResponseEntity<?> createLoan(@RequestBody Loan loanRequest) {
        try {
            Loan loan = loanService.loanBook(loanRequest.getBookId(), loanRequest.getBorrowerName());
            return ResponseEntity.ok(loan);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
    // ✅ Get Active Loans Only
    @GetMapping("/")
    public ResponseEntity<List<Loan>> getActiveLoans() {
        List<Loan> activeLoans = loanService.getActiveLoans();
        return ResponseEntity.ok(activeLoans);
    }

    // ✅ Get loan by ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<Object> getLoanById(@PathVariable Long id) {
        Optional<Loan> loan = loanService.getLoanById(id);
        return loan.<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body("Loan not found"));
    }

    // ✅ Return a book (PUT)
    @PutMapping("/return/{id}")
    public ResponseEntity<?> returnBook(@PathVariable Long id) {
        try {
            loanService.returnBook(id);
            return ResponseEntity.ok("Book returned successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}