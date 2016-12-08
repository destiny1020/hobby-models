package com.micropoplar.models.crypto.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micropoplar.models.crypto.CryptoSettings;

/**
 * 文件加密/解密服务。
 * 
 * @author ruixiang
 *
 */
@Service
public class FileCryptoService {

  @Autowired
  private CryptoSettings cryptoConfig;

  private byte[] salt = {(byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c, (byte) 0x7e,
      (byte) 0xc8, (byte) 0xee, (byte) 0x99};

  private SecretKeyFactory keyFac;
  private PBEKeySpec pbeKeySpec;
  private PBEParameterSpec pbeParamSpec;
  private SecretKey pbeKey;
  private Cipher pbeCipher;

  @PostConstruct
  public void init()
      throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException {
    keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
    pbeKeySpec = new PBEKeySpec(cryptoConfig.getSecret().toCharArray());
    pbeParamSpec = new PBEParameterSpec(salt, 20);
    pbeKey = keyFac.generateSecret(pbeKeySpec);
    pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
  }

  /**
   * 对输入流进行加密处理。
   * 
   * @param in
   * @return
   * @throws InvalidAlgorithmParameterException
   * @throws InvalidKeyException
   * @throws IOException
   */
  public byte[] encrypt(InputStream in)
      throws InvalidKeyException, InvalidAlgorithmParameterException, IOException {
    pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
    CipherInputStream cis = new CipherInputStream(in, pbeCipher);
    return IOUtils.toByteArray(cis);
  }

  /**
   * 对输入流进行解密处理并返回Base64格式数据。
   * 
   * @param in
   * @return
   * @throws IOException
   */
  public String decrypt(InputStream in) throws IOException {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    OutputStream b64 = new Base64OutputStream(os);
    IOUtils.copy(in, b64);
    return os.toString("UTF-8").replaceAll("\\s", "");
  }

}
