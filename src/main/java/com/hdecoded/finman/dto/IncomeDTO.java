package com.hdecoded.finman.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncomeDTO {

  private Long id;
  private String name;
  private String memo;
  private String imageURL;
  private LocalDate date;
  private BigDecimal amount;
  private String categoryName;
  private Long categoryId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

}
