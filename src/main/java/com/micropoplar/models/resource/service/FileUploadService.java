package com.micropoplar.models.resource.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micropoplar.infra.imagesdk.service.IImageManager;
import com.micropoplar.infra.imagesdk.service.IResponse;

/**
 * 文件上传服务。
 * 
 * @author ruixiang
 *
 */
@Service
public class FileUploadService {

  private static final String BUCKET_NAME = "models-biz";

  @Autowired
  private IImageManager imageManager;

  /**
   * 上传文件。
   * 
   * @param prefix
   * @param imageBytes
   * @return
   */
  public String uploadFile(String prefix, byte[] imageBytes) {
    String key = prefix + "/" + new SimpleDateFormat("yyMMdd").format(new Date()) + "/"
        + UUID.randomUUID().toString();
    IResponse response = imageManager.simpleUpload(BUCKET_NAME, key, imageBytes);
    String imageUrl = response.getDownloadUrl();
    return imageUrl;
  }

}
