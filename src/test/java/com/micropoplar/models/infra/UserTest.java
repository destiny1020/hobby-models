package com.micropoplar.models.infra;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import com.micropoplar.models.infra.domain.User;
import com.micropoplar.models.infra.repository.UserRepository;
import com.micropoplar.models.token.JwtType;

/**
 * 和系统用户相关的集成测试。
 * 
 * @author ruixiang
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest extends AbstractTransactionalJUnit4SpringContextTests {

  @Autowired
  private EntityManager em;

  @Autowired
  private UserRepository userRepo;

  /**
   * Flush和Native SQL Query之间的关系。 执行Native Query并不会导致Flush操作。
   * 但是对MySQL不起作用，因为MySQL的IDENTITY生成规则下，persist会立即执行插入:
   * https://vladmihalcea.com/2016/07/19/jpa-persist-and-merge/
   */
  @SuppressWarnings("unchecked")
  @Test
  @Rollback
  public void whenNativeSQLSelectThenNotFlushed() {
    User user = new User();
    user.setName("Test");
    user.setRole(JwtType.MEMBER);
    em.persist(user);
    List<Object[]> userIds = em
        .createNativeQuery("select id from models_infra_user where name = 'Test'").getResultList();
    assertThat(userIds.size(), is(0));
  }

  /**
   * Flush和JPQL Query之间的关系。 执行JPQL Query会导致Flush操作。
   */
  @Test
  @Rollback
  public void whenJPQLSelectThenFlushed() {
    User user = new User();
    user.setName("Test");
    user.setRole(JwtType.MEMBER);
    em.persist(user);
    List<User> users =
        em.createQuery("select u from User u where u.name = 'Test'", User.class).getResultList();
    assertThat(users.size(), is(1));
    assertThat(users.get(0).getName(), is("Test"));
  }

}
