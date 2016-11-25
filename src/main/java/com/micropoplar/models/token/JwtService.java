package com.micropoplar.models.token;

import java.util.Date;

import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * JWT相关服务。
 * 
 * @author ruixiang
 *
 */
@Service
public class JwtService {

  @Autowired
  private JwtSetting jwtConfig;

  /**
   * Tries to parse specified String as a JWT token. If successful, returns JwtPayLoad object with
   * name, id prefilled (extracted from token).
   * 
   * @param token the JWT token to parse
   * @return the JwtPayLoad object extracted from specified token or null if a token is invalid.
   * @throws ServletException
   */
  public Claims parseToken(String token) {
    try {
      Claims body =
          Jwts.parser().setSigningKey(jwtConfig.getSecretKey()).parseClaimsJws(token).getBody();
      return body;
    } catch (JwtException e) {
      throw new JwtException("认证信息无法识别");
    }
  }

  /**
   * Generates a JWT token containing username as subject, and userId and others as additional
   * claims. These properties are taken from the specified data object. Tokens validity is infinite.
   * 
   * @param data the login user'info for which the token will be generated
   * @return the JWT token
   */
  public String generateToken(JwtPayLoad data) {

    Date iat = new Date();
    long now = iat.getTime();
    Date exp = new Date(now + jwtConfig.getExpiration() * 60 * 1000);

    Claims claims =
        Jwts.claims().setSubject(assembleSubject(data)).setIssuedAt(iat).setExpiration(exp);
    claims.put("id", data.getId());

    return Jwts.builder().setClaims(claims)
        .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecretKey()).compact();
  }

  /**
   * 判断JWT Token类型是否正确。
   * 
   * @param claims
   * @param jwtType
   * @return
   */
  public boolean isJwtType(Claims claims, JwtType jwtType) {
    if (claims == null || jwtType == null) {
      return false;
    }

    JwtType role = JwtType.valueOf(claims.getSubject().split(":::")[0]);
    return jwtType == role;
  }

  private String assembleSubject(JwtPayLoad data) {
    return String.format("%s:::%s", data.getType().name(), data.getName());
  }

  /**
   * 判断JWT Token类型是否正确，同时判断JWT中的ID信息和待检查的ID是否匹配。
   * 
   * @param claims
   * @param jwtType
   * @param checkId
   * @return
   */
  public boolean isRightUser(Claims claims, JwtType jwtType, String checkId) {
    if (claims == null || jwtType == null || checkId == null) {
      return false;
    }

    JwtType role = JwtType.valueOf(claims.getSubject().split(":::")[0]);
    return jwtType == role && StringUtils.equals((String) claims.get("id"), checkId);
  }
}
