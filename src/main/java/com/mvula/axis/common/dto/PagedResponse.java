package com.mvula.axis.common.dto;

import java.util.List;
import lombok.Data;

@Data
public class PagedResponse<T> {
  private List<T> items;
  private int page;
  private int size;
  private long totalItems;
  private int totalPages;
}
