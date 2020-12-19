package com.example.restfulapiproduct.service;

import com.example.restfulapiproduct.dto.ProductDto;
import com.example.restfulapiproduct.entity.ProductEntity;
import com.example.restfulapiproduct.form.ProductForm;
import com.example.restfulapiproduct.repository.ProductRepository;
import com.example.restfulapiproduct.setting.ImageSetting;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/** ProductのServiceクラス */
@Service
@RequiredArgsConstructor
public class ProductService {
  private final ProductRepository productRepository;
  private final ImageService imageService;
  private final ImageSetting imageSetting;
  private final ResourceLoader resourceLoader;

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

  public HttpEntity<byte[]> getImage(Long id, String imagePath) {
    ProductDto productDto = findProduct(id);
    if (productDto.getImagePath().isBlank()) {
      System.out.println("else");
    }
    File file = new File(imageSetting.getImageStoreDir() + "image" + id + '/' + imagePath);
    Resource resource = resourceLoader.getResource("file:" + file.getAbsolutePath());
    String contentType;
    byte[] imageBytes;
    try (InputStream inputStream = resource.getInputStream()) {
      contentType = URLConnection.guessContentTypeFromStream(inputStream);
      imageBytes = inputStream.readAllBytes();
    } catch (Exception e) {
      throw new RuntimeException();
    }
    String extension = FilenameUtils.getExtension(imagePath);
    HttpHeaders headers = imageService.inspectExtension(extension);
    headers.setContentType(MediaType.valueOf(contentType));
    return new HttpEntity<>(imageBytes, headers);
  }

  /**
   * 商品画像更新
   *
   * @param id
   * @param multipartFile
   * @return
   */
  public ProductDto updateImage(Long id, MultipartFile multipartFile) {
    if (multipartFile == null) {
      System.out.println();
    }
    String fileName = multipartFile.getOriginalFilename();
    String contentType = '.' + FilenameUtils.getExtension(fileName);
    List<String> allowExtensions = imageSetting.allowedContentTypes;
    System.out.println(allowExtensions);
    String allowExtensionRegex = String.join("|", allowExtensions);
    if (contentType.equalsIgnoreCase(allowExtensionRegex)) {
      System.out.println("exception");
    }
    ProductEntity productEntity = findProductById(id);
    if (StringUtils.isNotBlank(productEntity.getImagePath())) {
      deleteImage(productEntity.getId());
    }
    productEntity.setImagePath(imageService.saveFile(id, multipartFile, contentType));
    ProductEntity newProductEntity = productRepository.save(productEntity);
    return convertToProductDto(newProductEntity);
  }

  public ProductEntity findProductById(Long id) {
    ProductEntity productEntity = productRepository.findById(id).orElseThrow();
    return productEntity;
  }

  private void deleteImage(Long id) {
    String directoryPath = imageSetting.getImageStoreDir() + "image" + id;
    File directory = new File(directoryPath);
    FileUtils.deleteQuietly(directory);
  }
}
