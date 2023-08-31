package com.bankapi.bank.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bankapi.bank.enums.Operation;
import com.bankapi.bank.model.Card;
import com.bankapi.bank.model.Report;
import com.bankapi.bank.repositories.ReportRepository;

@Service
public class ReportService {
    @Autowired
    private ReportRepository reportRepository;

    public List<Report> findAllReports() {
        return reportRepository.findAll();
    }

    public Report findReportById(Long id) {
        Optional<Report> reportById = reportRepository.findById(id);

        if(!reportById.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Report ID not found");
        }

        return reportRepository.findById(id).get();
    }

    public Report createReportFromCard(Double value, Operation operation, Card card) {
        Report report = new Report();

        setReportAttributesFromCard(value, operation, card, report);

        return reportRepository.save(report);
    }

    public Report setReportAttributesFromCard(Double value, Operation operation, Card card, Report report) {
        
        report.setOperation(operation);
        report.setValue(value);
        report.setCard(card);

        if(report.getCreationDate() == null) {
            report.setCreationDate(LocalDate.now());
        }
        report.setLastUpdate(LocalDateTime.now());

        return report;
    }

    public void deleteReport(Long id) {
        Optional<Report> reportById = reportRepository.findById(id);

        if(!reportById.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Report not found");
        }

        reportRepository.deleteById(id);
    }
}
