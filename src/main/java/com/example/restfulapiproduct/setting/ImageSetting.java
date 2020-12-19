package com.example.restfulapiproduct.setting;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@ConfigurationProperties(prefix = "setting.image")
@Data
@Component
public class ImageSetting {
    public List<String> allowedContentTypes;

    public String imageStoreDir;

}
