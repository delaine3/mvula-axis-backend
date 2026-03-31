package com.mvula.axis.disbursement.mapper;

import com.mvula.axis.disbursement.dto.DisbursementPaymentResponse;
import com.mvula.axis.disbursement.dto.DisbursementResponse;
import com.mvula.axis.disbursement.entity.Disbursement;
import com.mvula.axis.disbursement.entity.DisbursementPayment;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class DisbursementMapper {

  private DisbursementMapper() {}

  public static DisbursementResponse toResponse(Disbursement disbursement) {
    DisbursementResponse response = new DisbursementResponse();

    response.setId(disbursement.getId());
    response.setPayeeName(disbursement.getPayeeName());
    response.setPayeeType(disbursement.getPayeeType());
    response.setServiceDescription(disbursement.getServiceDescription());
    response.setTotalCharged(disbursement.getTotalCharged());
    response.setCurrency(disbursement.getCurrency());
    response.setDueDate(disbursement.getDueDate());
    response.setStatus(disbursement.getStatus());
    response.setNotes(disbursement.getNotes());
    response.setCreatedBy(disbursement.getCreatedBy());
    response.setUpdatedBy(disbursement.getUpdatedBy());
    response.setCreatedAt(disbursement.getCreatedAt());
    response.setUpdatedAt(disbursement.getUpdatedAt());

    List<DisbursementPaymentResponse> payments =
        disbursement.getPayments() == null
            ? Collections.emptyList()
            : disbursement.getPayments().stream()
                .map(DisbursementMapper::toPaymentResponse)
                .toList();

    response.setPayments(payments);

    int paymentCount = payments.size();
    response.setIsInstallment(paymentCount > 1);
    response.setInstallmentCount(paymentCount);

    BigDecimal totalPaid =
        payments.stream()
            .map(DisbursementPaymentResponse::getAmountPaid)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    response.setTotalPaid(totalPaid);
    response.setBalanceOutstanding(disbursement.getTotalCharged().subtract(totalPaid));

    return response;
  }

  public static DisbursementPaymentResponse toPaymentResponse(DisbursementPayment payment) {
    DisbursementPaymentResponse response = new DisbursementPaymentResponse();

    response.setId(payment.getId());
    response.setDatePaid(payment.getDatePaid());
    response.setAmountPaid(payment.getAmountPaid());
    response.setPaymentMethod(payment.getPaymentMethod());
    response.setReferenceNumber(payment.getReferenceNumber());
    response.setNotes(payment.getNotes());
    response.setCreatedAt(payment.getCreatedAt());

    return response;
  }
}
