package com.micropoplar.models.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.micropoplar.models.infra.domain.User;

/**
 * 用户DAO。
 * 
 * @author ruixiang
 *
 */
public interface UserRepository extends JpaRepository<User, Long> {

  User findByName(String name);

}
