package com.learning.microservices.billingbookservice.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Billing {

    @Id
    @GeneratedValue
    private Long id;

    private Long borrowId;
    private Date returnDate;
    private double finePerDay = 10;
    private double billAmount;
}
