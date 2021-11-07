package com.learning.microservices.billingbookservice.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowBook {


    private Long id;
    private Long userId;
    private Long bookId;
    // @JsonDeserialize
    private Date bookingDate;
    private double chargePerDay;
    private int noOfDays;
    private BorrowStatus borrowStatus;
}
