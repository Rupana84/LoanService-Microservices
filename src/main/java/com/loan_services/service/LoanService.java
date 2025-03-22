package com.loan_services.service;

import com.loan_services.dto.Book;
import com.loan_services.entity.Loan;
import com.loan_services.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository; // Injects repository for database operations

    private final WebClient webClient; // WebClient to communicate with BookService

    public LoanService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build(); // ✅ Ensure BookService is running on port 8080
    }

    // Fetch book details from BookService
    public Optional<Book> getBookDetails(Long bookId) {
        return webClient.get()
                .uri("/books/" + bookId)  // Calls BookService's GET /books/{id} endpoint
                .retrieve()
                .bodyToMono(Book.class)  // Converts response into Book object
                .blockOptional();  // Blocking for simplicity (not recommended for high-performance apps)
    }
    public List<Loan> getActiveLoans() {
        return loanRepository.findByReturnedFalse();  // Fetch only active loans from DB
    }

    // ✅ Fetch loan by ID
    public Optional<Loan> getLoanById(Long id) {
        return loanRepository.findById(id); // Retrieves loan details from the database
    }


    //  Loan a book (only if available)
    public Loan loanBook(Long bookId, String borrowerName) {
        Optional<Book> bookOpt = getBookDetails(bookId);

        if (bookOpt.isEmpty()) {
            throw new RuntimeException("Book not found in BookService!");
        }

        // Check if book is already loaned
        boolean isAlreadyLoaned = loanRepository.existsByBookIdAndReturnDateIsNull(bookId);
        if (isAlreadyLoaned) {
            throw new RuntimeException("Book is already loaned and not returned!");
        }

        //  Create a new loan with explicit returned = false
        Loan loan = new Loan();
        loan.setBookId(bookId);
        loan.setBorrowerName(borrowerName);
        loan.setLoanDate(LocalDate.now());
        loan.setReturnDate(null); // No return date yet
        loan.setReturned(false);   //  Explicitly set returned = false

        return loanRepository.save(loan);
    }

    //  Method to return a book
    public void returnBook(Long id) {
        Optional<Loan> loanOptional = loanRepository.findById(id); //  Fetch loan by ID

        if (loanOptional.isPresent()) {
            Loan loan = loanOptional.get();
            loan.setReturned(true); //  Mark as returned
            loan.setReturnDate(LocalDate.now()); //  Set return date to today
            loanRepository.save(loan); //  Save updated loan entry
        } else {
            throw new RuntimeException("Loan not found!"); //  Error if loan ID doesn't exist
        }
    }
}