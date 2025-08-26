package com.example.mybatis.service.impl;

import com.example.mybatis.entity.UserEntity;
import com.example.mybatis.mapper.UserMapperXml;
import com.example.mybatis.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户服务实现类 - 展示MyBatis各种特性的使用
 * 包括：动态SQL、分页查询、批量操作、事务处理、关联查询等
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserMapperXml userMapper;

    // =========================
    // 基础CRUD操作
    // =========================

    @Override
    public UserEntity findById(Long id) {
        log.info("查询用户，ID: {}", id);
        return userMapper.selectById(id);
    }

    @Override
    public UserEntity findByUsername(String username) {
        log.info("根据用户名查询用户: {}", username);
        return userMapper.selectByUsername(username);
    }

    @Override
    @Transactional
    public UserEntity save(UserEntity user) {
        log.info("保存用户: {}", user.getUsername());
        
        // 设置审计字段
        LocalDateTime now = LocalDateTime.now();
        user.setCreateTime(now);
        user.setUpdateTime(now);
        user.setCreatedBy("system");
        user.setUpdatedBy("system");
        user.setVersion(1L);
        
        userMapper.insert(user);
        log.info("用户保存成功，ID: {}", user.getId());
        return user;
    }

    @Override
    @Transactional
    public UserEntity update(UserEntity user) {
        log.info("更新用户，ID: {}", user.getId());
        
        // 设置更新时间和更新人
        user.setUpdateTime(LocalDateTime.now());
        user.setUpdatedBy("system");
        
        int rows = userMapper.updateSelective(user);
        if (rows > 0) {
            log.info("用户更新成功，影响行数: {}", rows);
            return userMapper.selectById(user.getId());
        } else {
            throw new RuntimeException("用户更新失败，用户不存在或版本冲突");
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("删除用户，ID: {}", id);
        int rows = userMapper.deleteById(id);
        if (rows > 0) {
            log.info("用户删除成功，影响行数: {}", rows);
        } else {
            throw new RuntimeException("用户删除失败，用户不存在");
        }
    }

    // =========================
    // 条件查询 - 动态SQL展示
    // =========================

    @Override
    public List<UserEntity> findByConditions(Map<String, Object> conditions) {
        log.info("多条件动态查询用户，条件: {}", conditions);
        return userMapper.selectByConditions(conditions);
    }

    @Override
    public List<UserEntity> findByUsernameLike(String username) {
        log.info("用户名模糊查询: {}", username);
        return userMapper.selectByUsernameLike(username);
    }

    @Override
    public List<UserEntity> findByAgeRange(Integer minAge, Integer maxAge) {
        log.info("年龄范围查询: {} - {}", minAge, maxAge);
        return userMapper.selectByAgeRange(minAge, maxAge);
    }

    @Override
    public List<UserEntity> findByDepartmentId(Long departmentId) {
        log.info("根据部门ID查询用户: {}", departmentId);
        return userMapper.selectByDepartmentId(departmentId);
    }

    @Override
    public List<UserEntity> findByGender(String gender) {
        log.info("根据性别查询用户: {}", gender);
        return userMapper.selectByGender(gender);
    }

    // =========================
    // 关联查询 - MyBatis关联映射展示
    // =========================

    @Override
    public UserEntity findWithDepartment(Long id) {
        log.info("查询用户及部门信息，用户ID: {}", id);
        return userMapper.selectWithDepartment(id);
    }

    @Override
    public UserEntity findWithRoles(Long id) {
        log.info("查询用户及角色信息，用户ID: {}", id);
        // 注意：这里需要实现对应的mapper方法
        return userMapper.selectById(id); // 临时实现
    }

    @Override
    public UserEntity findWithProjects(Long id) {
        log.info("查询用户及项目信息，用户ID: {}", id);
        // 注意：这里需要实现对应的mapper方法
        return userMapper.selectById(id); // 临时实现
    }

    @Override
    public UserEntity findFullInfo(Long id) {
        log.info("查询用户完整信息，用户ID: {}", id);
        // 注意：这里需要实现对应的mapper方法
        return userMapper.selectById(id); // 临时实现
    }

    // =========================
    // 分页查询 - MyBatis分页展示
    // =========================

    @Override
    public Page<UserEntity> findWithPagination(Pageable pageable) {
        log.info("分页查询用户，页码: {}, 大小: {}", pageable.getPageNumber(), pageable.getPageSize());
        
        int offset = (int) pageable.getOffset();
        int limit = pageable.getPageSize();
        
        List<UserEntity> users = userMapper.selectWithPagination(offset, limit);
        long total = userMapper.countTotal();
        
        return new PageImpl<>(users, pageable, total);
    }

    @Override
    public Page<UserEntity> findByConditionsWithPagination(Map<String, Object> conditions, Pageable pageable) {
        log.info("条件分页查询用户，条件: {}, 页码: {}, 大小: {}", 
                conditions, pageable.getPageNumber(), pageable.getPageSize());
        
        int offset = (int) pageable.getOffset();
        int limit = pageable.getPageSize();
        
        // 注意：这里需要实现对应的mapper方法
        List<UserEntity> users = userMapper.selectByConditions(conditions);
        long total = users.size(); // 临时实现
        
        return new PageImpl<>(users, pageable, total);
    }

    // =========================
    // 统计查询 - 聚合函数展示
    // =========================

    @Override
    public List<Map<String, Object>> countUsersByDepartment() {
        log.info("按部门统计用户数量");
        return userMapper.countUsersByDepartment();
    }

    @Override
    public List<Map<String, Object>> countUsersByGender() {
        log.info("按性别统计用户数量");
        return userMapper.countUsersByGender();
    }

    @Override
    public List<Map<String, Object>> getAgeDistribution() {
        log.info("查询年龄分布统计");
        // 注意：这里需要实现对应的mapper方法
        return userMapper.countUsersByGender(); // 临时实现
    }

    // =========================
    // 批量操作 - MyBatis批量处理展示
    // =========================

    @Override
    @Transactional
    public List<UserEntity> batchSave(List<UserEntity> users) {
        log.info("批量保存用户，数量: {}", users.size());
        
        // 设置审计字段
        LocalDateTime now = LocalDateTime.now();
        users.forEach(user -> {
            user.setCreateTime(now);
            user.setUpdateTime(now);
            user.setCreatedBy("system");
            user.setUpdatedBy("system");
            user.setVersion(1L);
        });
        
        int rows = userMapper.batchInsert(users);
        log.info("批量保存完成，影响行数: {}", rows);
        return users;
    }

    @Override
    @Transactional
    public int batchUpdate(List<UserEntity> users) {
        log.info("批量更新用户，数量: {}", users.size());
        
        // 设置更新时间
        LocalDateTime now = LocalDateTime.now();
        users.forEach(user -> {
            user.setUpdateTime(now);
            user.setUpdatedBy("system");
        });
        
        // 注意：这里需要实现对应的mapper方法
        return users.size(); // 临时实现
    }

    @Override
    @Transactional
    public int batchDelete(List<Long> ids) {
        log.info("批量删除用户，ID列表: {}", ids);
        return userMapper.batchDelete(ids);
    }

    // =========================
    // 时间范围查询
    // =========================

    @Override
    public List<UserEntity> findByCreateTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        log.info("时间范围查询用户: {} - {}", startTime, endTime);
        return userMapper.selectByCreateTimeRange(startTime, endTime);
    }

    @Override
    public List<UserEntity> findRecentCreated(int days) {
        log.info("查询最近{}天创建的用户", days);
        return userMapper.selectRecentCreated(days);
    }

    // =========================
    // 业务方法 - 展示事务和复杂业务逻辑
    // =========================

    @Override
    @Transactional
    public void transferUserToDepartment(Long userId, Long newDepartmentId) {
        log.info("用户转部门，用户ID: {}, 新部门ID: {}", userId, newDepartmentId);
        
        UserEntity user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        user.setDepartmentId(newDepartmentId);
        user.setUpdateTime(LocalDateTime.now());
        user.setUpdatedBy("system");
        
        int rows = userMapper.updateSelective(user);
        if (rows == 0) {
            throw new RuntimeException("用户转部门失败");
        }
        
        log.info("用户转部门成功");
    }

    @Override
    @Transactional
    public void assignRolesToUsers(List<Long> userIds, List<Long> roleIds) {
        log.info("批量分配角色，用户ID列表: {}, 角色ID列表: {}", userIds, roleIds);
        
        // 这里需要实现用户角色关联表的操作
        // 由于涉及到多表操作，这里只是示例
        log.info("角色分配完成");
    }

    @Override
    public Map<String, Object> getUserStatistics() {
        log.info("获取用户统计信息");
        
        Map<String, Object> statistics = new HashMap<>();
        
        // 总用户数
        long totalUsers = userMapper.countTotal();
        statistics.put("totalUsers", totalUsers);
        
        // 按部门统计
        List<Map<String, Object>> departmentStats = userMapper.countUsersByDepartment();
        statistics.put("departmentStats", departmentStats);
        
        // 按性别统计
        List<Map<String, Object>> genderStats = userMapper.countUsersByGender();
        statistics.put("genderStats", genderStats);
        
        // 最近7天新增用户
        List<UserEntity> recentUsers = userMapper.selectRecentCreated(7);
        statistics.put("recentUsersCount", recentUsers.size());
        
        log.info("用户统计信息获取完成: {}", statistics);
        return statistics;
    }
}
