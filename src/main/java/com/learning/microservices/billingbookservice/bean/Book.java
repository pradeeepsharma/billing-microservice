package com.learning.microservices.billingbookservice.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {


    private Long id;
    private String category;
    private String bookName;
    private String bookUrl;
    private int availableBooks;
    private String status;


}
