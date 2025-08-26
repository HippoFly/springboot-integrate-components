package com.example.mybatis.service;

import com.example.mybatis.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 用户服务接口 - 展示MyBatis各种特性
 */
public interface UserService {

    // =========================
    // 基础CRUD操作
    // =========================

    /**
     * 根据ID查询用户
     */
    UserEntity findById(Long id);

    /**
     * 根据用户名查询用户
     */
    UserEntity findByUsername(String username);

    /**
     * 保存用户
     */
    UserEntity save(UserEntity user);

    /**
     * 更新用户
     */
    UserEntity update(UserEntity user);

    /**
     * 删除用户
     */
    void deleteById(Long id);

    // =========================
    // 条件查询 - 动态SQL展示
    // =========================

    /**
     * 多条件动态查询用户
     */
    List<UserEntity> findByConditions(Map<String, Object> conditions);

    /**
     * 根据用户名模糊查询
     */
    List<UserEntity> findByUsernameLike(String username);

    /**
     * 根据年龄范围查询
     */
    List<UserEntity> findByAgeRange(Integer minAge, Integer maxAge);

    /**
     * 根据部门ID查询用户
     */
    List<UserEntity> findByDepartmentId(Long departmentId);

    /**
     * 根据性别查询用户
     */
    List<UserEntity> findByGender(String gender);

    // =========================
    // 关联查询 - MyBatis关联映射展示
    // =========================

    /**
     * 查询用户及其部门信息
     */
    UserEntity findWithDepartment(Long id);

    /**
     * 查询用户及其角色列表
     */
    UserEntity findWithRoles(Long id);

    /**
     * 查询用户及其项目列表
     */
    UserEntity findWithProjects(Long id);

    /**
     * 查询用户完整信息
     */
    UserEntity findFullInfo(Long id);

    // =========================
    // 分页查询 - MyBatis分页展示
    // =========================

    /**
     * 分页查询用户列表
     */
    Page<UserEntity> findWithPagination(Pageable pageable);

    /**
     * 条件分页查询
     */
    Page<UserEntity> findByConditionsWithPagination(Map<String, Object> conditions, Pageable pageable);

    // =========================
    // 统计查询 - 聚合函数展示
    // =========================

    /**
     * 按部门统计用户数量
     */
    List<Map<String, Object>> countUsersByDepartment();

    /**
     * 按性别统计用户数量
     */
    List<Map<String, Object>> countUsersByGender();

    /**
     * 查询年龄分布统计
     */
    List<Map<String, Object>> getAgeDistribution();

    // =========================
    // 批量操作 - MyBatis批量处理展示
    // =========================

    /**
     * 批量保存用户
     */
    List<UserEntity> batchSave(List<UserEntity> users);

    /**
     * 批量更新用户
     */
    int batchUpdate(List<UserEntity> users);

    /**
     * 批量删除用户
     */
    int batchDelete(List<Long> ids);

    // =========================
    // 时间范围查询
    // =========================

    /**
     * 查询指定时间范围内创建的用户
     */
    List<UserEntity> findByCreateTimeRange(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 查询最近N天创建的用户
     */
    List<UserEntity> findRecentCreated(int days);

    // =========================
    // 业务方法 - 展示事务和复杂业务逻辑
    // =========================

    /**
     * 用户转部门 - 展示事务处理
     */
    void transferUserToDepartment(Long userId, Long newDepartmentId);

    /**
     * 批量分配角色给用户 - 展示批量操作和事务
     */
    void assignRolesToUsers(List<Long> userIds, List<Long> roleIds);

    /**
     * 获取用户详细统计信息
     */
    Map<String, Object> getUserStatistics();
}
