package com.example.restfulapiproduct.form;

import lombok.Data;
import lombok.NoArgsConstructor;

/** 商品登録フォーム */
@Data
@NoArgsConstructor
public class ProductForm {
  private String title;

  private String description;

  private int price;
}
