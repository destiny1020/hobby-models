package com.micropoplar.models.resource.controller;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.micropoplar.models.auth.UserToken;
import com.micropoplar.models.common.response.ApiResponse;
import com.micropoplar.models.common.service.ResponseGenerator;
import com.micropoplar.models.infra.exception.CommonApiExceptions;
import com.micropoplar.models.resource.service.FileCryptoService;
import com.micropoplar.models.resource.service.FileUploadService;

/**
 * 文件上传控制器。
 * 
 * @author ruixiang
 *
 */
@RestController
@RequestMapping("/public/common/upload")
public class FileUploadController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private ResponseGenerator resGen;

  @Autowired
  private FileCryptoService fileCryptoService;

  @Autowired
  private FileUploadService fileUploadService;

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

    List<String> imageUrls = Arrays.asList(files).parallelStream().map(file -> {
      String imageUrl;
      try {
        logger.info(String.format("[文件 - 上传] 尝试上传文件: %s, 启用加密: %s", file.getOriginalFilename(),
            shouldEncrypt));
        imageUrl = uploadCore(prefix, file, shouldEncrypt) + " -- " + file.getOriginalFilename();
      } catch (Exception e) {
        e.printStackTrace();
        logger.warn(String.format("[文件 - 上传] 上传文件失败: %s, 启用加密: %s", file.getOriginalFilename(),
            shouldEncrypt));
        imageUrl = "";
      }

      return imageUrl;
    }).collect(Collectors.toList());

    return resGen.response(imageUrls);
  }

  private String uploadCore(String prefix, MultipartFile file, boolean encrypt)
      throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
      NoSuchPaddingException {
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

    return fileUploadService.uploadFile(prefix, imageBytes);
  }

}
