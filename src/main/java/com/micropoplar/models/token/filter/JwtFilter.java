package com.micropoplar.models.token.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.micropoplar.models.auth.UserContainer;
import com.micropoplar.models.auth.UserToken;
import com.micropoplar.models.token.JwtPayLoad;
import com.micropoplar.models.token.JwtService;
import com.micropoplar.models.token.JwtSetting;
import com.micropoplar.models.token.JwtType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

/**
 * 完成Token的验证，当前操作用户的提取等操作。
 * 
 * @author ruixiang
 *
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private JwtSetting jwtConfig;
  private JwtService jwtService;
  private UserContainer userContainer;

  @Autowired
  public JwtFilter(JwtSetting jwtConfig, JwtService jwtService, UserContainer userContainer) {
    this.jwtConfig = jwtConfig;
    this.jwtService = jwtService;
    this.userContainer = userContainer;
  }

  @Override
  protected void doFilterInternal(final HttpServletRequest request,
      final HttpServletResponse response, final FilterChain chain)
      throws ServletException, IOException {
    String requestedURL = request.getRequestURL().toString().toLowerCase();
    if (jwtConfig.isEnabled()) {
      try {
        final String authHeader = request.getHeader(jwtConfig.getTokenHeader());
        boolean isSecured = !requestedURL.contains("api/public");
        if (StringUtils.isNoneBlank(authHeader)) {
          if (authHeader == null) {
            if (isSecured) {
              throw new JwtException("缺少认证信息");
            }
          }

          if (!authHeader.startsWith(jwtConfig.getAuthSalt())) {
            if (isSecured) {
              throw new JwtException("认证信息不合法");
            } else {
              logger.warn(String.format("[Token Salt Invalid] When visiting %s", requestedURL));
            }
          } else {
            // salt正确开始尝试解析token
            final String token = authHeader.substring(7); // The part after "Bearer "
            try {
              final Claims claims = jwtService.parseToken(token);

              // 设置原始的认证凭证
              request.setAttribute("claims", claims);

              String userId = (String) claims.get("id");
              if (StringUtils.isBlank(userId)) {
                throw new JwtException("认证信息不正确，无法从中提取出用户ID");
              }

              UserToken userToken = userContainer.setCurrentUser(userId);
              if (userToken == null) {
                throw new JwtException("认证信息不正确，无法通过用户ID得到用户信息");
              }

              // 设置当前操作的用户对象
              request.setAttribute("user", userToken);

              // 重新生成token并设置头部
              String[] parts = claims.getSubject().split(":::");
              String updatedToken = jwtService
                  .generateToken(new JwtPayLoad(userId, parts[1], JwtType.valueOf(parts[0])));
              response.setHeader(jwtConfig.getTokenHeader(), updatedToken);
            } catch (JwtException e) {
              // 如果访问资源必须是安全的，则抛出异常，否则不必抛出
              if (isSecured) {
                throw e;
              } else {
                // 记录一条warning日志
                logger.warn(String.format("[Token Invalid] When visiting %s", requestedURL));
              }
            }
          }
        } else {
          if (isSecured) {
            throw new JwtException("缺少认证信息");
          }
        }
      } catch (final JwtException e) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        return;
      }
    }

    chain.doFilter(request, response);
  }

}
