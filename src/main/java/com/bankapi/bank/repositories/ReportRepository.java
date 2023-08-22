package com.bankapi.bank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bankapi.bank.model.Report;

public interface ReportRepository extends JpaRepository<Report, Long>{
    
}
