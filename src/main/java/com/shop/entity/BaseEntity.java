package com.shop.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
@Getter
public class BaseEntity extends BaseTimeEntity{ //baseEntity는 baseTimeEntity를 상속받고있기때문에 4가지(등록일수정일등록자수정자)를 모두 갖는 엔티티는 이를 상속받으면된다.
    @CreatedBy
    @Column(updatable = false)
    private String createdBy; //등록자

    @LastModifiedBy
    private String modifiedBy; //수정자
}
