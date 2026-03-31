package com.mvula.axis.disbursement.service;

import com.mvula.axis.disbursement.dto.CreateDisbursementPaymentRequest;
import com.mvula.axis.disbursement.dto.CreateDisbursementRequest;
import com.mvula.axis.disbursement.dto.DisbursementResponse;
import com.mvula.axis.disbursement.entity.Disbursement;
import com.mvula.axis.disbursement.entity.DisbursementPayment;
import com.mvula.axis.disbursement.entity.DisbursementStatus;
import com.mvula.axis.disbursement.entity.PayeeType;
import com.mvula.axis.disbursement.mapper.DisbursementMapper;
import com.mvula.axis.disbursement.repository.DisbursementPaymentRepository;
import com.mvula.axis.disbursement.repository.DisbursementRepository;
import com.mvula.axis.disbursement.specification.DisbursementSpecification;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    disbursement.setNotes(request.getNotes());
    disbursement.setStatus(DisbursementStatus.UNPAID);

    Disbursement saved = disbursementRepository.save(disbursement);
    return DisbursementMapper.toResponse(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<DisbursementResponse> getAllDisbursements(
      String search,
      String payeeName,
      PayeeType payeeType,
      DisbursementStatus status,
      String serviceDescription,
      Pageable pageable) {

    Specification<Disbursement> specification = DisbursementSpecification.searchAll(search);

    if (payeeName != null && !payeeName.isBlank()) {
      specification =
          specification == null
              ? DisbursementSpecification.hasPayeeName(payeeName)
              : specification.and(DisbursementSpecification.hasPayeeName(payeeName));
    }

    if (payeeType != null) {
      specification =
          specification == null
              ? DisbursementSpecification.hasPayeeType(payeeType)
              : specification.and(DisbursementSpecification.hasPayeeType(payeeType));
    }

    if (status != null) {
      specification =
          specification == null
              ? DisbursementSpecification.hasStatus(status)
              : specification.and(DisbursementSpecification.hasStatus(status));
    }

    if (serviceDescription != null && !serviceDescription.isBlank()) {
      specification =
          specification == null
              ? DisbursementSpecification.hasServiceDescription(serviceDescription)
              : specification.and(
                  DisbursementSpecification.hasServiceDescription(serviceDescription));
    }

    return disbursementRepository
        .findAll(specification, pageable)
        .map(DisbursementMapper::toResponse);
  }

  @Override
  public DisbursementResponse updateDisbursement(Long id, CreateDisbursementRequest request) {
    Disbursement disbursement =
        disbursementRepository
            .findById(id)
            .orElseThrow(
                () -> new EntityNotFoundException("Disbursement not found with id: " + id));

    disbursement.setPayeeName(request.getPayeeName());
    disbursement.setPayeeType(request.getPayeeType());
    disbursement.setServiceDescription(request.getServiceDescription());
    disbursement.setTotalCharged(request.getTotalCharged());
    disbursement.setCurrency(
        request.getCurrency() == null || request.getCurrency().isBlank()
            ? "SZL"
            : request.getCurrency());
    disbursement.setDueDate(request.getDueDate());
    disbursement.setNotes(request.getNotes());

    updateStatus(disbursement);

    Disbursement saved = disbursementRepository.save(disbursement);
    return DisbursementMapper.toResponse(saved);
  }

  @Override
  public void deleteDisbursement(Long id) {
    if (!disbursementRepository.existsById(id)) {
      throw new EntityNotFoundException("Disbursement not found with id: " + id);
    }

    disbursementRepository.deleteById(id);
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

    if (disbursement.getStatus() == DisbursementStatus.CANCELLED) {
      throw new IllegalStateException("Cannot add payment to a cancelled disbursement");
    }

    DisbursementPayment payment = new DisbursementPayment();
    payment.setDisbursement(disbursement);
    payment.setDatePaid(request.getDatePaid());
    payment.setAmountPaid(request.getAmountPaid());
    payment.setPaymentMethod(request.getPaymentMethod());
    payment.setReferenceNumber(generateUniqueReferenceNumber());
    payment.setNotes(request.getNotes());

    paymentRepository.save(payment);

    disbursement.getPayments().add(payment);
    updateStatus(disbursement);
    disbursementRepository.save(disbursement);

    return DisbursementMapper.toResponse(disbursement);
  }

  @Override
  public DisbursementResponse cancelDisbursement(Long id) {
    Disbursement disbursement =
        disbursementRepository
            .findById(id)
            .orElseThrow(
                () -> new EntityNotFoundException("Disbursement not found with id: " + id));

    disbursement.setStatus(DisbursementStatus.CANCELLED);

    Disbursement saved = disbursementRepository.save(disbursement);
    return DisbursementMapper.toResponse(saved);
  }

  private String generateUniqueReferenceNumber() {
    String referenceNumber;

    do {
      String datePart = java.time.LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
      String randomPart = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
      referenceNumber = "DPAY-" + datePart + "-" + randomPart;
    } while (paymentRepository.existsByReferenceNumber(referenceNumber));

    return referenceNumber;
  }

  private void updateStatus(Disbursement disbursement) {
    if (disbursement.getStatus() == DisbursementStatus.CANCELLED) {
      return;
    }

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
