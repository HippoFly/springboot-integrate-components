package com.example.jpa.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;

/**
 * 角色实体类
 * 演示基本实体映射
 */
@Entity
@Table(name = "role")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseAuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 角色名称
     */
    @Column(name = "role_name", nullable = false, unique = true)
    private String roleName;
    
    /**
     * 角色描述
     */
    @Column(name = "description")
    private String description;
    
    /**
     * 角色状态：ACTIVE, INACTIVE
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RoleStatus status = RoleStatus.ACTIVE;
    
    /**
     * 角色状态枚举
     */
    public enum RoleStatus {
        ACTIVE, INACTIVE
    }
}
