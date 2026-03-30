package com.mvula.axis.disbursement.controller;

import com.mvula.axis.disbursement.dto.CreateDisbursementPaymentRequest;
import com.mvula.axis.disbursement.dto.CreateDisbursementRequest;
import com.mvula.axis.disbursement.dto.DisbursementResponse;
import com.mvula.axis.disbursement.service.DisbursementService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
      @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
    return disbursementService.getAllDisbursements(pageable);
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
}
