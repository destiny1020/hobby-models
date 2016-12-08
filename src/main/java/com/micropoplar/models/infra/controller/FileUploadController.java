package com.micropoplar.models.infra.controller;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.micropoplar.infra.imagesdk.service.IImageManager;
import com.micropoplar.infra.imagesdk.service.IResponse;
import com.micropoplar.models.common.response.ApiResponse;
import com.micropoplar.models.common.service.ResponseGenerator;
import com.micropoplar.models.crypto.service.FileCryptoService;
import com.micropoplar.models.infra.auth.UserToken;
import com.micropoplar.models.infra.exception.CommonApiExceptions;

/**
 * 文件上传控制器。
 * 
 * @author ruixiang
 *
 */
@RestController
@RequestMapping("/public/common/upload")
public class FileUploadController {

  private static final String BUCKET_NAME = "models-biz";

  @Autowired
  private ResponseGenerator resGen;

  @Autowired
  private IImageManager imageManager;

  @Autowired
  private FileCryptoService fileCryptoService;

  /**
   * 上传多个文件(普通上传)。
   * 
   * @param user
   * @param prefix
   * @param files
   * @return
   * @throws InvalidAlgorithmParameterException
   * @throws InvalidKeyException
   */
  @RequestMapping(value = "/file/{prefix}", method = RequestMethod.POST)
  public ResponseEntity<ApiResponse> handfilesupload(@ModelAttribute("user") UserToken user,
      @PathVariable("prefix") String prefix, @RequestParam("files") MultipartFile[] files,
      @RequestParam(name = "encrypt", required = false) Boolean encrypt)
      throws InvalidKeyException, InvalidAlgorithmParameterException {
    if (files == null || files.length == 0) {
      return resGen.errorResponse(CommonApiExceptions.ERROR_MODELS_COMMON_UPLOAD_PIC_BAD_REQUEST);
    }

    // 是否需要加密
    boolean shouldEncrypt =
        (encrypt != null && encrypt.booleanValue()) ? Boolean.TRUE : Boolean.FALSE;

    List<String> imgUrls = new ArrayList<String>();
    for (MultipartFile file : files) {
      imgUrls.add(uploadCore(prefix, file, shouldEncrypt) + " -- " + file.getOriginalFilename());
    }
    return resGen.response(imgUrls);
  }

  private String uploadCore(String prefix, MultipartFile file, boolean encrypt)
      throws InvalidKeyException, InvalidAlgorithmParameterException {
    if (StringUtils.isBlank(prefix)) {
      prefix = "default";
    }
    prefix = prefix.replaceAll("\\/", "");

    // 上传文件到七牛
    byte[] imageBytes = null;
    try {
      if (encrypt) {
        imageBytes = fileCryptoService.encrypt(file.getInputStream());
      } else {
        imageBytes = file.getBytes();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    String key = prefix + "/" + new SimpleDateFormat("yyMMdd").format(new Date()) + "/"
        + UUID.randomUUID().toString();
    IResponse response = imageManager.simpleUpload(BUCKET_NAME, key, imageBytes);
    String imageUrl = response.getDownloadUrl();
    return imageUrl;
  }

}
