package com.mvula.axis.vendor.service;

import com.mvula.axis.common.exception.ResourceNotFoundException;
import com.mvula.axis.vendor.dto.AddressResponse;
import com.mvula.axis.vendor.dto.VendorRequest;
import com.mvula.axis.vendor.dto.VendorResponse;
import com.mvula.axis.vendor.entity.Address;
import com.mvula.axis.vendor.entity.Vendor;
import com.mvula.axis.vendor.repository.VendorRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VendorService {

  private final VendorRepository vendorRepository;

  public VendorResponse createVendor(VendorRequest request) {
    Vendor vendor = new Vendor();
    vendor.setName(request.getName());
    vendor.setCategory(request.getCategory());
    vendor.setIsActive(request.getIsActive());
    vendor.setWebsite(request.getWebsite());
    vendor.setTaxNumber(request.getTaxNumber());
    vendor.setOffersDelivery(request.getOffersDelivery());
    vendor.setContactPerson(request.getContactPerson());
    vendor.setContactNumber(request.getContactNumber());
    vendor.setEmail(request.getEmail());
    vendor.setNotes(request.getNotes());
    vendor.setPaymentTerms(request.getPaymentTerms());
    vendor.setPreferredCurrency(request.getPreferredCurrency());

    if (request.getAddress() != null) {
      Address address = new Address();
      address.setAddressLine1(request.getAddress().getAddressLine1());
      address.setAddressLine2(request.getAddress().getAddressLine2());
      address.setCity(request.getAddress().getCity());
      address.setStateOrProvince(request.getAddress().getStateOrProvince());
      address.setPostalCode(request.getAddress().getPostalCode());
      address.setCountry(request.getAddress().getCountry());
      vendor.setAddress(address);
    }

    Vendor savedVendor = vendorRepository.save(vendor);
    return mapToResponse(savedVendor);
  }

  public List<VendorResponse> getAllVendors() {
    return vendorRepository.findAll().stream()
        .map(this::mapToResponse)
        .toList();
  }

  public VendorResponse getVendorById(Long id) {
    Vendor vendor = vendorRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("vendor not found"));

    return mapToResponse(vendor);
  }

  private VendorResponse mapToResponse(Vendor vendor) {
    VendorResponse response = new VendorResponse();
    response.setId(vendor.getId());
    response.setName(vendor.getName());
    response.setCategory(vendor.getCategory());
    response.setIsActive(vendor.getIsActive());
    response.setWebsite(vendor.getWebsite());
    response.setTaxNumber(vendor.getTaxNumber());
    response.setOffersDelivery(vendor.getOffersDelivery());
    response.setContactPerson(vendor.getContactPerson());
    response.setContactNumber(vendor.getContactNumber());
    response.setEmail(vendor.getEmail());
    response.setNotes(vendor.getNotes());
    response.setPaymentTerms(vendor.getPaymentTerms());
    response.setPreferredCurrency(vendor.getPreferredCurrency());
    response.setCreatedAt(vendor.getCreatedAt());
    response.setUpdatedAt(vendor.getUpdatedAt());

    if (vendor.getAddress() != null) {
      AddressResponse addressResponse = new AddressResponse();
      addressResponse.setAddressLine1(vendor.getAddress().getAddressLine1());
      addressResponse.setAddressLine2(vendor.getAddress().getAddressLine2());
      addressResponse.setCity(vendor.getAddress().getCity());
      addressResponse.setStateOrProvince(vendor.getAddress().getStateOrProvince());
      addressResponse.setPostalCode(vendor.getAddress().getPostalCode());
      addressResponse.setCountry(vendor.getAddress().getCountry());
      response.setAddress(addressResponse);
    }

    return response;
  }
}