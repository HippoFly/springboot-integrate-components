package com.example.mybatis.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户实体类 - MyBatis版本
 * 对应数据库表：users
 * 展示MyBatis实体映射特性
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    /**
     * 主键ID - 自增长
     */
    private Long id;

    /**
     * 用户名 - 唯一索引
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别：男/女
     */
    private String gender;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 部门ID - 外键
     */
    private Long departmentId;

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
     * 关联的部门信息 - 一对一关联
     */
    private DeptEntity department;

    /**
     * 用户的角色列表 - 一对多关联
     */
    private List<RoleEntity> roles;

    /**
     * 参与的项目列表 - 多对多关联
     */
    private List<ProjectEntity> projects;
}
