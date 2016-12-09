package com.micropoplar.models.resource.controller;

import java.io.IOException;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.micropoplar.models.auth.UserToken;
import com.micropoplar.models.common.response.ApiResponse;
import com.micropoplar.models.common.service.ResponseGenerator;
import com.micropoplar.models.resource.dto.req.ReqUrlDecrypt;
import com.micropoplar.models.resource.dto.req.ReqUrlEncrypt;
import com.micropoplar.models.resource.service.FileCryptoService;
import com.micropoplar.models.resource.service.FileUploadService;

/**
 * 文件加密/解密控制器。
 * 
 * @author ruixiang
 *
 */
@RestController
@RequestMapping("/public/common/crypto")
public class FileCryptoController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private ResponseGenerator resGen;

  @Autowired
  private FileCryptoService fileCryptoService;

  @Autowired
  private FileUploadService fileUploadService;

  /**
   * 将指定的URL资源进行加密并上传。
   * 
   * @param user
   * @param urls
   * @return
   */
  @RequestMapping(value = "/encrypt", method = RequestMethod.POST)
  public ResponseEntity<ApiResponse> encrypt(@ModelAttribute("user") UserToken user,
      @RequestBody ReqUrlEncrypt reqUrlEncrypt) {
    List<String> uploadedUrls = reqUrlEncrypt.getUrls().parallelStream().map(url -> {
      byte[] encryptedBytes = null;
      try {
        logger.info(String.format("[URL - 加密上传] URL: %s", url));
        encryptedBytes = fileCryptoService.encrypt(new URL(url).openStream());
      } catch (Exception e) {
        logger.info(String.format("[URL - 加密上传] 出现异常URL: %s", url));
        e.printStackTrace();
      }
      if (encryptedBytes != null) {
        return fileUploadService.uploadFile(reqUrlEncrypt.getPrefix(), encryptedBytes);
      } else {
        return "";
      }
    }).collect(Collectors.toList());

    return resGen.response(uploadedUrls);
  }

  /**
   * 将指定URL资源进行解密并以本地数据形式返回。
   * 
   * @param user
   * @param url
   * @return
   * @throws IOException
   * @throws InvalidAlgorithmParameterException
   * @throws InvalidKeyException
   */
  @RequestMapping(value = "/decrypt", method = RequestMethod.POST)
  public ResponseEntity<ApiResponse> decrypt(@ModelAttribute("user") UserToken user,
      @RequestBody ReqUrlDecrypt reqUrlDecrypt)
      throws IOException, InvalidKeyException, InvalidAlgorithmParameterException {
    List<String> decryptedUrls = reqUrlDecrypt.getUrls().parallelStream().map(url -> {
      try {
        switch (reqUrlDecrypt.getType().intValue()) {
          case 1:
            return fileCryptoService.decryptToBase64(url);
          case 0:
          default:
            return fileCryptoService.decryptToFile(url);
        }
      } catch (Exception e) {
        e.printStackTrace();
        return "";
      }
    }).collect(Collectors.toList());

    return resGen.response(decryptedUrls);
  }

}
