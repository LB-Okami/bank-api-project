package com.bankapi.bank.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bankapi.bank.model.Report;
import com.bankapi.bank.model.ReportDTO;
import com.bankapi.bank.services.ReportService;

@RestController
@RequestMapping("/reports")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Report> findAllReports() {
        return reportService.findAllReports();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Report createReport(@RequestBody ReportDTO reportDTO) {
        return reportService.createReport(reportDTO);
    }
}
