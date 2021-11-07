package com.learning.microservices.billingbookservice.service;


import com.learning.microservices.billingbookservice.bean.Billing;
import com.learning.microservices.billingbookservice.bean.Book;
import com.learning.microservices.billingbookservice.bean.BorrowBook;
import com.learning.microservices.billingbookservice.bean.BorrowStatus;
import com.learning.microservices.billingbookservice.config.ApplicationConfig;
import com.learning.microservices.billingbookservice.exception.BillNotFoundException;
import com.learning.microservices.billingbookservice.repository.BillingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Service
public class BillingService {
    @Autowired
    BillingRepository billingRepository;
    @Autowired
    ApplicationConfig applicationConfig;


    @Transactional
    public void returnBook(Long borrowId) throws RuntimeException {
        BorrowBook borrowBook = getBorrowedBookDetails(borrowId);
        borrowBook.setBorrowStatus(BorrowStatus.RETURNED);
        updateBorrowingDetails(borrowBook);

        returnBookApi(borrowBook.getBookId());

        Billing billing = generateBill(borrowBook);
        billingRepository.save(billing);
    }

    private BorrowBook getBorrowedBookDetails(Long borrowId) {
        BorrowBook borrowBook = WebClient.create().get().uri(applicationConfig.getBorrowBookService() + "/" + borrowId).retrieve().bodyToMono(BorrowBook.class).block();
        return borrowBook;
    }

    private void updateBorrowingDetails(BorrowBook borrowedBook){
        String borrowingUpdated = WebClient.create().post().uri(applicationConfig.getBorrowBookService()+"/update").body(Mono.just(borrowedBook), BorrowBook.class).retrieve().bodyToMono(String.class).block();
        System.out.println("borrowingUpdated----->"+borrowingUpdated);
    }

    private void returnBookApi(Long bookId){
        String bookReturnedStatus = WebClient.create().get().uri(applicationConfig.getBookServiceApi() + "/return/" + bookId).retrieve().bodyToMono(String.class).block();
        System.out.println("bookReturnedStatus ----->"+bookReturnedStatus);
    }

    private Billing generateBill(BorrowBook borrowBook){

        Billing billing = new Billing();

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy");//DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDate borrowedDate = LocalDate.parse(borrowBook.getBookingDate().toString(), formatter);
        Long totalDays =  ChronoUnit.DAYS.between(borrowedDate,today);
        double totalAmount = (totalDays*borrowBook.getChargePerDay())+(billing.getFinePerDay()*(totalDays-borrowBook.getNoOfDays()));

        billing.setBillAmount(totalAmount);
        billing.setReturnDate(Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        billing.setBorrowId(borrowBook.getId());
        return billing;


    }

    public Billing getBill(Long billId)throws BillNotFoundException {
        Optional<Billing> billingOptional = billingRepository.findById(billId);
        if(billingOptional.isEmpty())
            throw new BillNotFoundException("No bill for requested id :"+billId);
        return billingOptional.get();
    }
}
