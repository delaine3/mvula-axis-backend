package com.mvula.axis.disbursement.controller;

import com.mvula.axis.disbursement.dto.CreateDisbursementPaymentRequest;
import com.mvula.axis.disbursement.dto.CreateDisbursementRequest;
import com.mvula.axis.disbursement.dto.DisbursementResponse;
import com.mvula.axis.disbursement.entity.DisbursementStatus;
import com.mvula.axis.disbursement.entity.PayeeType;
import com.mvula.axis.disbursement.service.DisbursementService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/disbursements")
public class DisbursementController {

  private final DisbursementService disbursementService;

  public DisbursementController(DisbursementService disbursementService) {
    this.disbursementService = disbursementService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public DisbursementResponse createDisbursement(
      @Valid @RequestBody CreateDisbursementRequest request) {
    return disbursementService.createDisbursement(request);
  }

  @GetMapping
  public Page<DisbursementResponse> getAllDisbursements(
      @RequestParam(required = false) String search,
      @RequestParam(required = false) String payeeName,
      @RequestParam(required = false) PayeeType payeeType,
      @RequestParam(required = false) DisbursementStatus status,
      @RequestParam(required = false) String serviceDescription,
      Pageable pageable) {

    return disbursementService.getAllDisbursements(
        search, payeeName, payeeType, status, serviceDescription, pageable);
  }

  @GetMapping("/{id}")
  public DisbursementResponse getDisbursementById(@PathVariable Long id) {
    return disbursementService.getDisbursementById(id);
  }

  @PostMapping("/{id}/payments")
  @ResponseStatus(HttpStatus.CREATED)
  public DisbursementResponse addPayment(
      @PathVariable Long id, @Valid @RequestBody CreateDisbursementPaymentRequest request) {
    return disbursementService.addPayment(id, request);
  }

  @PutMapping("/{id}")
  public DisbursementResponse updateDisbursement(
      @PathVariable Long id, @Valid @RequestBody CreateDisbursementRequest request) {
    return disbursementService.updateDisbursement(id, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteDisbursement(@PathVariable Long id) {
    disbursementService.deleteDisbursement(id);
  }

  @PostMapping("/{id}/cancel")
  public DisbursementResponse cancelDisbursement(@PathVariable Long id) {
    return disbursementService.cancelDisbursement(id);
  }
}
