package com.learning.microservices.billingbookservice.repository;

import com.learning.microservices.billingbookservice.bean.Billing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingRepository extends CrudRepository<Billing, Long> {
}
