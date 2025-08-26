package com.example.mybatis.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 项目实体类 - MyBatis版本
 * 对应数据库表：projects
 * 展示MyBatis复杂关联查询和枚举映射
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectEntity {

    /**
     * 主键ID - 自增长
     */
    private Long id;

    /**
     * 项目名称 - 唯一索引
     */
    private String name;

    /**
     * 项目描述
     */
    private String description;

    /**
     * 项目开始日期
     */
    private LocalDate startDate;

    /**
     * 项目结束日期
     */
    private LocalDate endDate;

    /**
     * 项目状态枚举
     */
    private ProjectStatus status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 版本号 - 乐观锁
     */
    private Long version;

    // =========================
    // MyBatis关联查询结果映射
    // =========================

    /**
     * 项目参与的用户列表 - 多对多关联
     */
    private List<UserEntity> users;

    /**
     * 项目用户数量 - 统计字段
     */
    private Integer userCount;

    /**
     * 项目状态枚举定义
     */
    public enum ProjectStatus {
        PLANNING("规划中"),
        IN_PROGRESS("进行中"),
        COMPLETED("已完成"),
        CANCELLED("已取消"),
        ON_HOLD("暂停");

        private final String description;

        ProjectStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
