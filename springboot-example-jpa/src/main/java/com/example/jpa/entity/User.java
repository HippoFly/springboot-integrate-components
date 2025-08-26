package com.example.jpa.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户实体类，演示JPA基本注解和CRUD操作
 * 与大数据生成模块保持一致的表结构
 */
@Entity
@Table(name = "users")  // 统一表名为users
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    /**
     * 主键字段，自动生成
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
     * 真实姓名
     */
    @Column(name = "real_name", length = 100)
    private String realName;
    
    /**
     * 用户邮箱
     */
    @Column(length = 100)
    private String email;
    
    /**
     * 用户年龄
     */
    @Column
    private Integer age;
    
    /**
     * 性别
     */
    @Column(length = 10)
    private String gender;
    
    /**
     * 手机号码
     */
    @Column(length = 20)
    private String phone;
    
    /**
     * 创建时间
     */
    @Column(name = "create_time")
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
     * 版本号
     */
    @Version
    private Long version;
    
    /**
     * 所属部门 - 多对一关系
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
    
    /**
     * 参与的项目 - 多对多关系
     */
    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<Project> projects = new ArrayList<>();
    
    @PreUpdate
    public void preUpdate() {
        this.updateTime = LocalDateTime.now();
    }
    
    @PrePersist
    public void prePersist() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }
}