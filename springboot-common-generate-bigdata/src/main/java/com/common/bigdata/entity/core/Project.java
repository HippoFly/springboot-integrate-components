package com.common.bigdata.entity.core;

import com.common.bigdata.entity.base.BaseAuditEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目实体类，与User形成多对多关系
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "projects")
public class Project extends BaseAuditEntity {
    
    /**
     * 主键字段
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
     * 项目成员 - 多对多关系
     */
    @ManyToMany(mappedBy = "projects", fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();
    
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
