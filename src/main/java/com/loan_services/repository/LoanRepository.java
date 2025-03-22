package com.loan_services.repository;

import com.loan_services.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {


    boolean existsByBookIdAndReturnDateIsNull(Long bookId);
    List<Loan> findByReturnedFalse();

}