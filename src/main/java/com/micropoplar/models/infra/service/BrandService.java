package com.micropoplar.models.infra.service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import com.micropoplar.models.infra.domain.Brand;
import com.micropoplar.models.infra.repository.BrandRepository;

/**
 * 品牌相关服务。
 * 
 * @author ruixiang
 *
 */
@Service
public class BrandService implements CommandLineRunner {

  private Random rand = new Random();
  private Set<String> codeAvailable;

  @Autowired
  private BrandRepository brandRepo;

  /**
   * 保存品牌。
   * 
   * @param brand
   * @return
   */
  public Brand save(Brand brand) {
    return brandRepo.save(brand);
  }

  /**
   * 查看品牌日文名是否存在。
   * 
   * @param nameJapanese
   * @return
   */
  public boolean existsByNameJapanese(String nameJapanese) {
    return brandRepo.countByNameJapanese(nameJapanese) > 0;
  }

  /**
   * 获取随机的二位Code。
   * 
   * @return
   */
  public String getAvailableCode() {
    int index = rand.nextInt(codeAvailable.size());
    Iterator<String> iter = codeAvailable.iterator();
    for (int i = 0; i < index; i++) {
      iter.next();
    }
    String nextAvailableCode = iter.next();
    codeAvailable.remove(nextAvailableCode);

    return nextAvailableCode;
  }

  /**
   * 用于初始化可用的品牌Code
   */
  @Override
  public void run(String... arg0) throws Exception {
    List<Brand> brands = brandRepo.findAll();
    Set<String> usedCodes = brands.stream().map(Brand::getCode).collect(Collectors.toSet());

    Set<String> allCodes = generateAllCodes();
    allCodes.removeAll(usedCodes);

    codeAvailable = allCodes;
  }

  private String candidates = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  private Set<String> generateAllCodes() {
    Set<String> allCodes = new HashSet<>();

    for (int i = 0; i < candidates.toCharArray().length; i++) {
      for (int j = 0; j < candidates.toCharArray().length; j++) {
        allCodes.add(String.valueOf(candidates.charAt(i)) + String.valueOf(candidates.charAt(j)));
      }
    }

    return allCodes;
  }

}
