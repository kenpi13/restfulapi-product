package com.example.restfulapiproduct.service;

import com.example.restfulapiproduct.setting.ImageSetting;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * 画像サービス
 *
 * @author kawaharakenta
 */
@Service
@RequiredArgsConstructor
public class ImageService {
  private final ImageSetting imageSetting;

  /**
   * ファイルの拡張子を調べる
   *
   * @param extension 画像ファイルの拡張子
   * @return httpHeaders
   */
  HttpHeaders inspectExtension(String extension) {
    HttpHeaders httpHeaders = new HttpHeaders();
    switch (extension) {
      case "gif":
        httpHeaders.setContentType(MediaType.IMAGE_GIF);
        return httpHeaders;
      case "png":
        httpHeaders.setContentType(MediaType.IMAGE_PNG);
        return httpHeaders;
      default:
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        return httpHeaders;
    }
  }

  /**
   * 画像の実体を保存する処理
   *
   * @param id 画像を保存したい商品id
   * @param multipartFile 保存したい画像ファイル
   * @param contentType 保存したい画像ファイルのコンテントタイプ
   * @return ユニークに設定した文字列 + extension(拡張子)
   */
  String saveFile(Long id, MultipartFile multipartFile, String contentType) {

    List<String> allowExtensions = imageSetting.getAllowedContentTypes();
    if (allowExtensions.stream().noneMatch(s -> s.equalsIgnoreCase(contentType))) {
      //            throw new UnsupportedMediaTypeException(contentType + "を拡張子として設定することはできません");
    }
    String extension = contentType;
    File imageContainer = new File(imageSetting.getImageStoreDir());
    imageContainer.mkdirs();
    File directory = new File(imageSetting.getImageStoreDir() + "image" + id);
    String filePath = String.format(imageSetting.getImageStoreDir() + "image/%d", id);
    File dir = new File(filePath);
    if (!dir.exists()) {
      directory.mkdirs();
    }
    try {
      String uniqueFileName = UUID.randomUUID().toString();
      String imagePath = directory.getPath() + '/' + uniqueFileName + extension;
      File file = new File(imagePath);
      multipartFile.transferTo(file.toPath());
      return uniqueFileName + extension;
    } catch (IOException ex) {
      throw new RuntimeException();
    }
  }
}
