package com.example.mybatis.mapper;

import com.example.mybatis.entity.DeptEntity;
import com.example.mybatis.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 用户Mapper接口 - XML配置版本
 * 展示MyBatis的各种查询特性和最佳实践
 */
@Mapper
public interface UserMapperXml {

    // =========================
    // 基础CRUD操作
    // =========================

    /**
     * 根据ID查询用户 - 基础查询
     * @param id 用户ID
     * @return 用户信息
     */
    UserEntity selectById(@Param("id") Long id);

    /**
     * 根据用户名查询用户 - 唯一查询
     * @param username 用户名
     * @return 用户信息
     */
    UserEntity selectByUsername(@Param("username") String username);

    /**
     * 插入用户 - 返回自增主键
     * @param user 用户信息
     * @return 影响行数
     */
    int insert(UserEntity user);

    /**
     * 更新用户 - 选择性更新
     * @param user 用户信息
     * @return 影响行数
     */
    int updateSelective(UserEntity user);

    /**
     * 根据ID删除用户
     * @param id 用户ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    // =========================
    // 条件查询 - 展示动态SQL
    // =========================

    /**
     * 多条件动态查询用户
     * @param params 查询参数Map
     * @return 用户列表
     */
    List<UserEntity> selectByConditions(Map<String, Object> params);

    /**
     * 根据用户名模糊查询
     * @param username 用户名关键字
     * @return 用户列表
     */
    List<UserEntity> selectByUsernameLike(@Param("username") String username);

    /**
     * 根据年龄范围查询
     * @param minAge 最小年龄
     * @param maxAge 最大年龄
     * @return 用户列表
     */
    List<UserEntity> selectByAgeRange(@Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge);

    /**
     * 根据部门ID查询用户
     * @param departmentId 部门ID
     * @return 用户列表
     */
    List<UserEntity> selectByDepartmentId(@Param("departmentId") Long departmentId);

    /**
     * 根据性别查询用户
     * @param gender 性别
     * @return 用户列表
     */
    List<UserEntity> selectByGender(@Param("gender") String gender);

    // =========================
    // 关联查询 - 展示MyBatis关联映射
    // =========================

    /**
     * 查询用户及其部门信息 - 一对一关联
     * @param id 用户ID
     * @return 用户及部门信息
     */
    UserEntity selectWithDepartment(@Param("id") Long id);

    /**
     * 查询用户及其角色列表 - 一对多关联
     * @param id 用户ID
     * @return 用户及角色信息
     */
    UserEntity selectWithRoles(@Param("id") Long id);

    /**
     * 查询用户及其项目列表 - 多对多关联
     * @param id 用户ID
     * @return 用户及项目信息
     */
    UserEntity selectWithProjects(@Param("id") Long id);

    /**
     * 查询用户完整信息 - 包含所有关联数据
     * @param id 用户ID
     * @return 用户完整信息
     */
    UserEntity selectFullInfo(@Param("id") Long id);

    // =========================
    // 分页查询 - 展示MyBatis分页
    // =========================

    /**
     * 分页查询用户列表
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 用户列表
     */
    List<UserEntity> selectWithPagination(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 查询用户总数 - 配合分页使用
     * @return 用户总数
     */
    long countTotal();

    /**
     * 条件分页查询
     * @param params 查询条件
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 用户列表
     */
    List<UserEntity> selectByConditionsWithPagination(
            @Param("params") Map<String, Object> params,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    // =========================
    // 统计查询 - 展示聚合函数
    // =========================

    /**
     * 按部门统计用户数量
     * @return 部门用户统计Map
     */
    List<Map<String, Object>> countUsersByDepartment();

    /**
     * 按性别统计用户数量
     * @return 性别用户统计Map
     */
    List<Map<String, Object>> countUsersByGender();

    /**
     * 查询年龄分布统计
     * @return 年龄分布统计
     */
    List<Map<String, Object>> getAgeDistribution();

    // =========================
    // 批量操作 - 展示MyBatis批量处理
    // =========================

    /**
     * 批量插入用户
     * @param users 用户列表
     * @return 影响行数
     */
    int batchInsert(@Param("users") List<UserEntity> users);

    /**
     * 批量更新用户
     * @param users 用户列表
     * @return 影响行数
     */
    int batchUpdate(@Param("users") List<UserEntity> users);

    /**
     * 批量删除用户
     * @param ids 用户ID列表
     * @return 影响行数
     */
    int batchDelete(@Param("ids") List<Long> ids);

    // =========================
    // 时间范围查询
    // =========================

    /**
     * 查询指定时间范围内创建的用户
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 用户列表
     */
    List<UserEntity> selectByCreateTimeRange(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    /**
     * 查询最近N天创建的用户
     * @param days 天数
     * @return 用户列表
     */
    List<UserEntity> selectRecentCreated(@Param("days") int days);
}
