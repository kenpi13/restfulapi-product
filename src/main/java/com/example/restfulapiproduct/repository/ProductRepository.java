package com.example.restfulapiproduct.repository;

import com.example.restfulapiproduct.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/** 商品リポジトリ */
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
  List<ProductEntity> findAllByOrderByUpdatedAtDesc();

  List<ProductEntity> findByTitleContainingOrderByUpdatedAtDesc(String title);

  Optional<ProductEntity> findByTitleEquals(String title);

  Optional<ProductEntity> findByTitleEqualsAndIdNot(String title, Long id);
}
