package com.bankapi.bank.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bankapi.bank.model.Report;
import com.bankapi.bank.services.ReportService;

@RestController
@RequestMapping("/reports")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReportController {
    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Report> findAllReports() {
        return reportService.findAllReports();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Report findReportById(@PathVariable Long id) {
        return reportService.findReportById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
    }
}
