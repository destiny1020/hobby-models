package com.micropoplar.models.common.response;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 审计字段。
 * 
 * @author ruixiang
 *
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditingEntity {

  @Column(name = "created_time")
  @CreatedDate
  private Date createdTime;

  @Column(name = "modified_time")
  @LastModifiedDate
  private Date modifiedTime;

  @Column(name = "created_by")
  @CreatedBy
  private String createdBy;

  @Column(name = "modified_by")
  @LastModifiedBy
  private String modifiedBy;

}
