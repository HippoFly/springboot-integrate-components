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
 * 用户实体类，演示基本用户信息和关联关系
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User extends BaseAuditEntity {
    
    /**
     * 主键字段
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 用户名，不能为空且唯一
     */
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    
    /**
     * 用户邮箱
     */
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    /**
     * 用户年龄
     */
    @Column(nullable = false)
    private Integer age;
    
    /**
     * 用户真实姓名
     */
    @Column(nullable = false, length = 50)
    private String realName;
    
    /**
     * 性别
     */
    @Column(length = 10)
    private String gender;
    
    /**
     * 手机号
     */
    @Column(length = 20)
    private String phone;
    
    /**
     * 所属部门 - 多对一关系
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
    
    /**
     * 参与的项目 - 多对多关系
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_projects",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private List<Project> projects = new ArrayList<>();
    
    /**
     * 用户角色 - 多对多关系
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();
}
