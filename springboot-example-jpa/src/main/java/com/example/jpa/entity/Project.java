package com.example.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目实体类，与User形成多对多关系
 * 与大数据生成模块保持一致的表结构
 */
@Entity
@Table(name = "projects")  // 统一表名为projects
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
@EqualsAndHashCode(callSuper = false)
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
     * 项目名称，不能为空
     */
    @Column(nullable = false, length = 200)
    private String name;
    
    /**
     * 项目描述
     */
    @Column(length = 1000)
    private String description;
    
    /**
     * 项目开始日期
     */
    @Column(name = "start_date")
    private LocalDate startDate;
    
    /**
     * 项目结束日期
     */
    @Column(name = "end_date")
    private LocalDate endDate;
    
    /**
     * 项目状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ProjectStatus status = ProjectStatus.PLANNING;
    
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
     * 参与项目的用户 - 多对多关系
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_project",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<User> users = new ArrayList<>();
    
    @PreUpdate
    public void preUpdate() {
        this.updateTime = LocalDateTime.now();
    }
    
    @PrePersist
    public void prePersist() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 项目状态枚举
     */
    public enum ProjectStatus {
        PLANNING("规划中"),
        IN_PROGRESS("进行中"),
        COMPLETED("已完成"),
        SUSPENDED("已暂停"),
        CANCELLED("已取消");
        
        private final String description;
        
        ProjectStatus(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
}