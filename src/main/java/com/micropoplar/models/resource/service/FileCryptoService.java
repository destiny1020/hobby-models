package com.micropoplar.models.resource.service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.micropoplar.models.resource.CryptoSettings;

/**
 * 文件加密/解密服务。
 * 
 * @author ruixiang
 *
 */
@Service
public class FileCryptoService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private CryptoSettings cryptoConfig;

  private byte[] salt = {(byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c, (byte) 0x7e,
      (byte) 0xc8, (byte) 0xee, (byte) 0x99};

  private SecretKeyFactory keyFac;
  private PBEKeySpec pbeKeySpec;
  private PBEParameterSpec pbeParamSpec;
  private SecretKey pbeKey;

  @PostConstruct
  public void init()
      throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException {
    keyFac = SecretKeyFactory.getInstance("PBEwithSHA1andDESede");
    pbeKeySpec = new PBEKeySpec(cryptoConfig.getSecret().toCharArray());
    pbeParamSpec = new PBEParameterSpec(salt, 20);
    pbeKey = keyFac.generateSecret(pbeKeySpec);
  }

  private String decryptToFileCore(InputStream in)
      throws IOException, InvalidKeyException, InvalidAlgorithmParameterException {
    Cipher localCipher = localDigest.get();
    localCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);
    CipherInputStream cis = new CipherInputStream(in, localCipher);
    String localPath = String.format("public/temp/%s", UUID.randomUUID().toString());
    FileOutputStream out = new FileOutputStream(localPath);
    IOUtils.copy(cis, out);
    return localPath;
  }

  private String decryptToBase64Core(InputStream in)
      throws IOException, InvalidKeyException, InvalidAlgorithmParameterException {
    Cipher localCipher = localDigest.get();
    localCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);
    CipherInputStream cis = new CipherInputStream(in, localCipher);
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    OutputStream b64 = new Base64OutputStream(os);
    IOUtils.copy(cis, b64);
    return String.format("data:image/png;base64,%s", os.toString("UTF-8").replaceAll("\\s", ""));
  }

  /**
   * 对指定的URL资源进行解密处理并保存到本地。
   * 
   * @param url
   * @return
   * @throws IOException
   * @throws InvalidAlgorithmParameterException
   * @throws InvalidKeyException
   */
  @Cacheable("decryptedFiles")
  public String decryptToFile(String url)
      throws IOException, InvalidKeyException, InvalidAlgorithmParameterException {
    logger.info("[文件 - 解密到本地文件] URL: " + url);
    return this.decryptToFileCore(new URL(url).openStream());
  }


  /**
   * 对指定的URL资源进行解密处理并保存到本地。
   * 
   * @param url
   * @return
   * @throws IOException
   * @throws InvalidAlgorithmParameterException
   * @throws InvalidKeyException
   */
  @Cacheable("decryptedBase64s")
  public String decryptToBase64(String url)
      throws IOException, InvalidKeyException, InvalidAlgorithmParameterException {
    logger.info("[文件 - 解密到Base64] URL: " + url);
    return this.decryptToBase64Core(new URL(url).openStream());
  }

  /**
   * 对输入流进行加密处理。
   * 
   * @param in
   * @return
   * @throws InvalidAlgorithmParameterException
   * @throws InvalidKeyException
   * @throws IOException
   * @throws NoSuchPaddingException
   * @throws NoSuchAlgorithmException
   */
  public byte[] encrypt(InputStream in)
      throws InvalidKeyException, InvalidAlgorithmParameterException, IOException,
      NoSuchAlgorithmException, NoSuchPaddingException {
    Cipher localCipher = localDigest.get();
    localCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
    CipherInputStream cis = new CipherInputStream(in, localCipher);
    return IOUtils.toByteArray(IOUtils.toBufferedInputStream(cis));
  }

  private static final ThreadLocal<Cipher> localDigest = new ThreadLocal<Cipher>() {
    @Override
    protected Cipher initialValue() {
      try {
        return Cipher.getInstance("PBEwithSHA1andDESede");
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  };

}
