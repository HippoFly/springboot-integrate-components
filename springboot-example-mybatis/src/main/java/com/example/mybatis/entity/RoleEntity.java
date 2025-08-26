package com.example.mybatis.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色实体类 - MyBatis版本
 * 对应数据库表：roles
 * 展示MyBatis多对多关联查询
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntity {

    /**
     * 主键ID - 自增长
     */
    private Long id;

    /**
     * 角色名称 - 唯一索引
     */
    private String name;

    /**
     * 角色描述
     */
    private String description;

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
     * 拥有该角色的用户列表 - 多对多关联
     */
    private List<UserEntity> users;

    /**
     * 角色用户数量 - 统计字段
     */
    private Integer userCount;
}
