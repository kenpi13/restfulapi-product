package com.example.restfulapiproduct.service;

import com.example.restfulapiproduct.dto.ProductDto;
import com.example.restfulapiproduct.entity.ProductEntity;
import com.example.restfulapiproduct.form.ProductForm;
import com.example.restfulapiproduct.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/** ProductのServiceクラス */
@Service
@RequiredArgsConstructor
public class ProductService {
  private final ProductRepository productRepository;

  /**
   * 商品登録
   *
   * @param productForm
   * @return
   */
  public ProductEntity saveProduct(ProductForm productForm) {
    ProductEntity productEntity = new ProductEntity();
    productEntity.setTitle(productForm.getTitle());
    productEntity.setDescription(productForm.getDescription());
    productEntity.setPrice(productForm.getPrice());
    productEntity.setCreatedAt(LocalDateTime.now());
    productEntity.setUpdatedAt(LocalDateTime.now());
    return productRepository.save(productEntity);
  }

  /**
   * 商品検索
   *
   * @param id
   * @return
   */
  public ProductDto findProduct(Long id) {
    ProductEntity productEntity = productRepository.findById(id).orElseThrow();
    return convertToProductDto(productEntity);
  }

  /**
   * 商品全件取得
   *
   * @return
   */
  public List<ProductDto> findAll() {
    List<ProductEntity> products = productRepository.findAllByOrderByUpdatedAtDesc();
    return products.stream().map(this::convertToProductDto).collect(Collectors.toList());
  }

  /**
   * 商品検索
   *
   * @param title
   * @return
   */
  public List<ProductDto> searchProductByTitle(String title) {
    List<ProductEntity> products =
        productRepository.findByTitleContainingOrderByUpdatedAtDesc(title);
    return products.stream().map(this::convertToProductDto).collect(Collectors.toList());
  }

  /**
   * コンバーター
   *
   * @param productEntity
   * @return
   */
  private ProductDto convertToProductDto(ProductEntity productEntity) {
    ProductDto productDto = new ProductDto();
    productDto.setId(productEntity.getId());
    productDto.setTitle(productEntity.getTitle());
    productDto.setDescription(productEntity.getDescription());
    productDto.setPrice(productEntity.getPrice());
    productDto.setCreatedAt(productEntity.getCreatedAt());
    productDto.setUpdatedAt(productEntity.getUpdatedAt());
    return productDto;
  }

  /**
   * 商品更新
   *
   * @param id
   * @param productForm
   * @return
   */
  public ProductEntity update(Long id, ProductForm productForm) {
    ProductEntity productEntity = productRepository.findById(id).orElseThrow();
    productEntity.setTitle(productForm.getTitle());
    productEntity.setDescription(productForm.getDescription());
    productEntity.setPrice(productForm.getPrice());
    return productRepository.save(productEntity);
  }

  /**
   * 商品削除
   *
   * @param id
   */
  public void deleteProduct(Long id) {
    ProductEntity productEntity = productRepository.findById(id).orElseThrow();
    productRepository.delete(productEntity);
  }
}
