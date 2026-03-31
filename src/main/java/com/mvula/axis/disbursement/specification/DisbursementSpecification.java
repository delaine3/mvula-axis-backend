package com.mvula.axis.disbursement.specification;

import com.mvula.axis.disbursement.entity.Disbursement;
import com.mvula.axis.disbursement.entity.DisbursementStatus;
import com.mvula.axis.disbursement.entity.PayeeType;
import org.springframework.data.jpa.domain.Specification;

public class DisbursementSpecification {

  private DisbursementSpecification() {}

  public static Specification<Disbursement> hasPayeeName(String payeeName) {
    return (root, query, criteriaBuilder) ->
        payeeName == null || payeeName.isBlank()
            ? null
            : criteriaBuilder.like(
                criteriaBuilder.lower(root.get("payeeName")), "%" + payeeName.toLowerCase() + "%");
  }

  public static Specification<Disbursement> searchAll(String search) {
    return (root, query, cb) -> {
      if (search == null || search.isBlank()) return null;

      String like = "%" + search.toLowerCase() + "%";

      return cb.or(
          cb.like(cb.lower(root.get("payeeName")), like),
          cb.like(cb.lower(root.get("payeeType")), like),
          cb.like(cb.lower(root.get("status")), like),
          cb.like(cb.lower(root.get("serviceDescription")), like),
          cb.like(cb.lower(root.get("currency")), like));
    };
  }

  public static Specification<Disbursement> hasPayeeType(PayeeType payeeType) {
    return (root, query, criteriaBuilder) ->
        payeeType == null ? null : criteriaBuilder.equal(root.get("payeeType"), payeeType);
  }

  public static Specification<Disbursement> hasStatus(DisbursementStatus status) {
    return (root, query, criteriaBuilder) ->
        status == null ? null : criteriaBuilder.equal(root.get("status"), status);
  }

  public static Specification<Disbursement> hasServiceDescription(String serviceDescription) {
    return (root, query, criteriaBuilder) ->
        serviceDescription == null || serviceDescription.isBlank()
            ? null
            : criteriaBuilder.like(
                criteriaBuilder.lower(root.get("serviceDescription")),
                "%" + serviceDescription.toLowerCase() + "%");
  }
}
