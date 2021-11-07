package com.learning.microservices.billingbookservice.controller;

import com.learning.microservices.billingbookservice.bean.Billing;
import com.learning.microservices.billingbookservice.service.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillingController {
    @Autowired
    private BillingService billingService;

    @PostMapping("/billing/{borrowBookId}")
    public ResponseEntity<Billing> generateBill(@PathVariable Long borrowBookId) {

        billingService.returnBook(borrowBookId);

        return new ResponseEntity<>(new Billing(), HttpStatus.CREATED);
    }

    @GetMapping("/billing/getbill/{billId}")

    public ResponseEntity<Billing> getBill(@PathVariable Long billId) {
        return new ResponseEntity<>(billingService.getBill(billId), HttpStatus.OK);

    }

}
