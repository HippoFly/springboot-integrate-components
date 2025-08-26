package com.common.bigdata.entity.base;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 审计基类，提供创建时间、更新时间、创建人、更新人等审计字段
 * 基于JPA模型，去除JPA注解，用于通用数据生成
 */
@Data
@MappedSuperclass
public abstract class BaseAuditEntity {
    
    /**
     * 创建时间
     */
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;
    
    /**
     * 创建人
     */
    @Column(name = "created_by", length = 50)
    private String createdBy;
    
    /**
     * 更新人
     */
    @Column(name = "updated_by", length = 50)
    private String updatedBy;
    
    /**
     * 版本号，用于乐观锁
     */
    @Version
    private Long version;
}
