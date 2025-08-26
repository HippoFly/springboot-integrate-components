package com.common.bigdata.entity.core;

import com.common.bigdata.entity.base.BaseAuditEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "roles")
public class Role extends BaseAuditEntity {
    
    /**
     * 主键字段
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 角色名称，不能为空且唯一
     */
    @Column(nullable = false, unique = true, length = 50)
    private String roleName;
    
    /**
     * 角色描述
     */
    @Column(length = 500)
    private String description;
    
    /**
     * 角色状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private RoleStatus status = RoleStatus.ACTIVE;
    
    /**
     * 拥有该角色的用户 - 多对多关系
     */
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();
    
    /**
     * 角色状态枚举
     */
    public enum RoleStatus {
        ACTIVE, INACTIVE
    }
    
    /**
     * 构造函数，用于预制数据
     */
    public Role(String roleName, String description) {
        this.roleName = roleName;
        this.description = description;
        this.status = RoleStatus.ACTIVE;
    }
}
