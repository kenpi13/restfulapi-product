package com.example.restfulapiproduct.setting;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/** FileSizeの設定クラス */
@ConfigurationProperties(prefix = "spring.servlet.multipart")
@Data
@Component
public class FileSizeSetting {
  public String maxFileSize;
}
