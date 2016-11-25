package com.micropoplar.models.infra.auth;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;

import com.micropoplar.models.infra.domain.User;
import com.micropoplar.models.infra.repository.UserRepository;

/**
 * 得到当前操作的用户，用于审计字段的填充。
 * 
 * @author ruixiang
 *
 */
@Service("userContainer")
public class UserContainer implements AuditorAware<String> {

  @Autowired
  private UserRepository userRepo;

  private final ThreadLocal<String> operatingUser = new ThreadLocal<>();

  @Override
  public String getCurrentAuditor() {
    String currentAuditor = operatingUser.get();
    if (StringUtils.isBlank(currentAuditor)) {
      currentAuditor = "System";
    }

    return currentAuditor;
  }

  public UserToken setCurrentUser(String name) {
    User currentUser = userRepo.findByName(name);
    if (currentUser != null) {
      operatingUser.set(currentUser.getName());
    } else {
      operatingUser.set("Unknown");
    }

    return currentUser != null ? new UserToken(currentUser) : null;
  }

}
