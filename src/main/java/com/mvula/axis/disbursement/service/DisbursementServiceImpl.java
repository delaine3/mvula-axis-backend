package com.mvula.axis.disbursement.service;

import com.mvula.axis.disbursement.dto.CreateDisbursementPaymentRequest;
import com.mvula.axis.disbursement.dto.CreateDisbursementRequest;
import com.mvula.axis.disbursement.dto.DisbursementResponse;
import com.mvula.axis.disbursement.entity.Disbursement;
import com.mvula.axis.disbursement.entity.DisbursementPayment;
import com.mvula.axis.disbursement.entity.DisbursementStatus;
import com.mvula.axis.disbursement.mapper.DisbursementMapper;
import com.mvula.axis.disbursement.repository.DisbursementPaymentRepository;
import com.mvula.axis.disbursement.repository.DisbursementRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DisbursementServiceImpl implements DisbursementService {

  private final DisbursementRepository disbursementRepository;
  private final DisbursementPaymentRepository paymentRepository;

  public DisbursementServiceImpl(
      DisbursementRepository disbursementRepository,
      DisbursementPaymentRepository paymentRepository) {
    this.disbursementRepository = disbursementRepository;
    this.paymentRepository = paymentRepository;
  }

  @Override
  public DisbursementResponse createDisbursement(CreateDisbursementRequest request) {
    validateInstallmentFields(request);

    Disbursement disbursement = new Disbursement();
    disbursement.setPayeeName(request.getPayeeName());
    disbursement.setPayeeType(request.getPayeeType());
    disbursement.setServiceDescription(request.getServiceDescription());
    disbursement.setTotalCharged(request.getTotalCharged());
    disbursement.setCurrency(
        request.getCurrency() == null || request.getCurrency().isBlank()
            ? "SZL"
            : request.getCurrency());
    disbursement.setDueDate(request.getDueDate());
    disbursement.setIsInstallment(request.getIsInstallment());
    disbursement.setInstallmentCount(
        request.getIsInstallment() ? request.getInstallmentCount() : null);
    disbursement.setNotes(request.getNotes());
    disbursement.setStatus(DisbursementStatus.UNPAID);

    Disbursement saved = disbursementRepository.save(disbursement);
    return DisbursementMapper.toResponse(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public List<DisbursementResponse> getAllDisbursements() {
    return disbursementRepository.findAll().stream().map(DisbursementMapper::toResponse).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public DisbursementResponse getDisbursementById(Long id) {
    Disbursement disbursement =
        disbursementRepository
            .findById(id)
            .orElseThrow(
                () -> new EntityNotFoundException("Disbursement not found with id: " + id));

    return DisbursementMapper.toResponse(disbursement);
  }

  @Override
  public DisbursementResponse addPayment(
      Long disbursementId, CreateDisbursementPaymentRequest request) {
    Disbursement disbursement =
        disbursementRepository
            .findById(disbursementId)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "Disbursement not found with id: " + disbursementId));

    DisbursementPayment payment = new DisbursementPayment();
    payment.setDisbursement(disbursement);
    payment.setDatePaid(request.getDatePaid());
    payment.setAmountPaid(request.getAmountPaid());
    payment.setPaymentMethod(request.getPaymentMethod());
    payment.setReferenceNumber(request.getReferenceNumber());
    payment.setNotes(request.getNotes());

    paymentRepository.save(payment);

    disbursement.getPayments().add(payment);
    updateStatus(disbursement);
    disbursementRepository.save(disbursement);

    return DisbursementMapper.toResponse(disbursement);
  }

  private void validateInstallmentFields(CreateDisbursementRequest request) {
    if (Boolean.TRUE.equals(request.getIsInstallment())) {
      if (request.getInstallmentCount() == null || request.getInstallmentCount() < 2) {
        throw new IllegalArgumentException(
            "Installment count must be at least 2 when isInstallment is true");
      }
    }
  }

  private void updateStatus(Disbursement disbursement) {
    BigDecimal totalPaid =
        disbursement.getPayments().stream()
            .map(DisbursementPayment::getAmountPaid)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal totalCharged = disbursement.getTotalCharged();

    int comparison = totalPaid.compareTo(totalCharged);

    if (totalPaid.compareTo(BigDecimal.ZERO) == 0) {
      disbursement.setStatus(DisbursementStatus.UNPAID);
    } else if (comparison < 0) {
      disbursement.setStatus(DisbursementStatus.PARTIALLY_PAID);
    } else if (comparison == 0) {
      disbursement.setStatus(DisbursementStatus.PAID);
    } else {
      disbursement.setStatus(DisbursementStatus.OVERPAID);
    }
  }
}
