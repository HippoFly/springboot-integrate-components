package com.example.jpa.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户角色关联实体类
 * 演示 JPA 复合主键和关联映射
 */
@Entity
@Table(name = "user_role")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserRole extends BaseAuditEntity {
    
    /**
     * 复合主键
     */
    @EmbeddedId
    private UserRoleId id;
    
    /**
     * 用户引用
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;
    
    /**
     * 角色引用
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    private Role role;
    
    /**
     * 分配时间
     */
    @Column(name = "assigned_date")
    private LocalDateTime assignedDate;
    
    /**
     * 过期时间
     */
    @Column(name = "expire_date")
    private LocalDateTime expireDate;
    
    /**
     * 是否激活
     */
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @PrePersist
    public void prePersist() {
        if (assignedDate == null) {
            assignedDate = LocalDateTime.now();
        }
    }
}
