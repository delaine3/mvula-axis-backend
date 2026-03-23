package com.mvula.axis.vendor.controller;

import com.mvula.axis.vendor.dto.VendorRequest;
import com.mvula.axis.vendor.dto.VendorResponse;
import com.mvula.axis.vendor.service.VendorService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vendors")
@RequiredArgsConstructor
public class VendorController {

  private final VendorService vendorService;

  @PostMapping
  public VendorResponse createVendor(@Valid @RequestBody VendorRequest request) {
    return vendorService.createVendor(request);
  }

  @GetMapping
  public List<VendorResponse> getAllVendors() {
    return vendorService.getAllVendors();
  }

  @GetMapping("/{id}")
  public VendorResponse getVendorById(@PathVariable Long id) {
    return vendorService.getVendorById(id);
  }
}