package com.mvula.axis.disbursement.repository;

import com.mvula.axis.disbursement.entity.Disbursement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DisbursementRepository
    extends JpaRepository<Disbursement, Long>, JpaSpecificationExecutor<Disbursement> {}
