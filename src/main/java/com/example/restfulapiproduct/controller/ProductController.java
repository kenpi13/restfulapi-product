package com.example.restfulapiproduct.controller;

import com.example.restfulapiproduct.dto.ProductDto;
import com.example.restfulapiproduct.entity.ProductEntity;
import com.example.restfulapiproduct.form.ProductForm;
import com.example.restfulapiproduct.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Apiのコントローラー */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
  private final ProductService productService;

  /**
   * 商品登録する
   *
   * @param productForm
   * @return
   */
  @PostMapping
  public ResponseEntity<ProductEntity> registerProduct(@RequestBody ProductForm productForm) {
    return new ResponseEntity<>(productService.saveProduct(productForm), HttpStatus.CREATED);
  }

  /**
   * 商品1件取得
   *
   * @param id
   * @return
   */
  @GetMapping("/{id}")
  public ResponseEntity<ProductDto> findProduct(@PathVariable(value = "id") Long id) {
    return new ResponseEntity<>(productService.findProduct(id), HttpStatus.OK);
  }

  /**
   * 商品検索
   *
   * @param title
   * @return
   */
  @GetMapping
  public ResponseEntity<List<ProductDto>> searchProduct(
      @RequestParam(name = "title", required = false) String title) {
    if (StringUtils.isBlank(title)) {
      return new ResponseEntity<>(productService.findAll(), HttpStatus.OK);
    }
    return new ResponseEntity<>(productService.searchProductByTitle(title), HttpStatus.OK);
  }

  /**
   * 商品更新
   *
   * @param id
   * @param productForm
   * @return
   */
  @PutMapping("/{id}")
  public ResponseEntity<ProductEntity> updateProduct(
      @PathVariable(value = "id") Long id, @RequestBody ProductForm productForm) {
    return new ResponseEntity<>(productService.update(id, productForm), HttpStatus.OK);
  }

  /**
   * 商品削除
   *
   * @param id
   */
  @DeleteMapping("/{id}")
  public void deleteProduct(@PathVariable(value = "id") Long id) {
    productService.deleteProduct(id);
  }
}
