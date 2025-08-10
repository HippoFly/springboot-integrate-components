package com.example.jpa.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户实体类，演示JPA基本注解和CRUD操作
 */
@Entity
@Table(name = "user")
@Data
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
    @Column(nullable = false, unique = true)
    private String username;
    
    /**
     * 用户邮箱
     */
    @Column
    private String email;
    
    /**
     * 用户年龄
     */
    @Column
    private Integer age;
    
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
     * 所属部门 - 多对一关系
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
    
    /**
     * 参与的项目 - 多对多关系
     */
    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private List<Project> projects = new ArrayList<>(); // 初始化为空列表，避免null值
    
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