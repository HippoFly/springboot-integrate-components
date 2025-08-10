package com.example.jpa.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目实体类，与User形成多对多关系
 */
@Entity
@Table(name = "project")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    
    /**
     * 主键字段，自动生成
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 项目名称，不能为空且唯一
     */
    @Column(nullable = false, unique = true)
    private String name;
    
    /**
     * 项目描述
     */
    @Column
    private String description;
    
    /**
     * 项目开始时间
     */
    @Column(name = "start_date")
    private LocalDateTime startDate;
    
    /**
     * 项目结束时间
     */
    @Column(name = "end_date")
    private LocalDateTime endDate;
    
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
     * 参与项目的用户 - 多对多关系
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_project",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users = new ArrayList<>(); // 初始化为空列表，避免null值
    
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