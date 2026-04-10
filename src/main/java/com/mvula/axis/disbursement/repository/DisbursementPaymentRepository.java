package com.mvula.axis.disbursement.repository;

import com.mvula.axis.disbursement.entity.DisbursementPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisbursementPaymentRepository extends JpaRepository<DisbursementPayment, Long> {

  Page<DisbursementPayment> findByDisbursementId(Long disbursementId, Pageable pageable);

  boolean existsByReferenceNumber(String referenceNumber);
}
