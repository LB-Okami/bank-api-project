package com.bankapi.bank.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bankapi.bank.model.Card;
import com.bankapi.bank.model.Report;
import com.bankapi.bank.model.ReportDTO;
import com.bankapi.bank.repositories.ReportRepository;

@Service
public class ReportService {
    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private CardService cardService;

    public List<Report> findAllReports() {
        return reportRepository.findAll();
    }

    public Report createReport(ReportDTO reportDTO) {
        Report report = new Report();

        setReportAttributes(report, reportDTO);

        return reportRepository.save(report);
    }

    public Report setReportAttributes(Report report, ReportDTO reportDTO) {

        Card cardById = cardService.findCardById(reportDTO.getCardId());
        
        report.setOperation(reportDTO.getOperation());
        report.setValue(reportDTO.getValue());
        report.setCard(cardById);

        if(report.getCreationDate() == null) {
            report.setCreationDate(LocalDate.now());
        }
        report.setLastUpdate(LocalDateTime.now());

        return report;
    }
}
