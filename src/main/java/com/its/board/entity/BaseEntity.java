package com.its.board.entity;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {
    @CreationTimestamp // 인서트 발생 시
    @Column(updatable = false) // 업데이트가 발생했을 때는 가만히
    private LocalDateTime createdTime;

    @UpdateTimestamp // 업데이트 발생 시
    @Column(insertable = false) // 인서트가 발생했을 때는 가만히
    private LocalDateTime updatedTime;
}
