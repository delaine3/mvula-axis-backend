package com.mvula.axis.disbursement.repository;

import com.mvula.axis.disbursement.entity.DisbursementPayment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisbursementPaymentRepository extends JpaRepository<DisbursementPayment, Long> {

  List<DisbursementPayment> findByDisbursementId(Long disbursementId);
}
