package com.example.restfulapiproduct.dto;

import lombok.Data;

import java.time.LocalDateTime;

/** ProductのDtoクラス */
@Data
public class ProductDto {
  private Long id;

  private String title;

  private String description;

  private int price;

  private String imagePath;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
