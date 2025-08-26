package com.example.mybatis.controller;

import com.example.mybatis.entity.UserEntity;
import com.example.mybatis.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器 - 展示MyBatis各种查询模式的REST API
 * 
 * 学习要点：
 * 1. 基础CRUD操作的API设计
 * 2. 动态SQL查询的参数处理
 * 3. 分页查询的实现
 * 4. 批量操作的API设计
 * 5. 统计查询的数据返回
 * 6. 关联查询的结果处理
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // =========================
    // 基础CRUD操作 API
    // =========================

    /**
     * 根据ID查询用户
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long id) {
        log.info("API调用：根据ID查询用户，ID: {}", id);
        UserEntity user = userService.findById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    /**
     * 根据用户名查询用户
     * GET /api/users/username/{username}
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<UserEntity> getUserByUsername(@PathVariable String username) {
        log.info("API调用：根据用户名查询用户，用户名: {}", username);
        UserEntity user = userService.findByUsername(username);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    /**
     * 创建用户
     * POST /api/users
     */
    @PostMapping
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity user) {
        log.info("API调用：创建用户，用户名: {}", user.getUsername());
        UserEntity savedUser = userService.save(user);
        return ResponseEntity.ok(savedUser);
    }

    /**
     * 更新用户
     * PUT /api/users/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserEntity> updateUser(@PathVariable Long id, @RequestBody UserEntity user) {
        log.info("API调用：更新用户，ID: {}", id);
        user.setId(id);
        UserEntity updatedUser = userService.update(user);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * 删除用户
     * DELETE /api/users/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("API调用：删除用户，ID: {}", id);
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // =========================
    // 条件查询 API - 展示动态SQL
    // =========================

    /**
     * 多条件动态查询用户
     * GET /api/users/search?username=xxx&minAge=20&maxAge=30&gender=男&departmentId=1
     */
    @GetMapping("/search")
    public ResponseEntity<List<UserEntity>> searchUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Long departmentId) {
        
        log.info("API调用：多条件动态查询用户");
        
        Map<String, Object> conditions = new HashMap<>();
        if (username != null) conditions.put("username", username);
        if (minAge != null) conditions.put("minAge", minAge);
        if (maxAge != null) conditions.put("maxAge", maxAge);
        if (gender != null) conditions.put("gender", gender);
        if (departmentId != null) conditions.put("departmentId", departmentId);
        
        List<UserEntity> users = userService.findByConditions(conditions);
        return ResponseEntity.ok(users);
    }

    /**
     * 用户名模糊查询
     * GET /api/users/search/username?keyword=张
     */
    @GetMapping("/search/username")
    public ResponseEntity<List<UserEntity>> searchUsersByUsername(@RequestParam String keyword) {
        log.info("API调用：用户名模糊查询，关键字: {}", keyword);
        List<UserEntity> users = userService.findByUsernameLike(keyword);
        return ResponseEntity.ok(users);
    }

    /**
     * 年龄范围查询
     * GET /api/users/search/age?min=20&max=30
     */
    @GetMapping("/search/age")
    public ResponseEntity<List<UserEntity>> searchUsersByAge(
            @RequestParam(required = false) Integer min,
            @RequestParam(required = false) Integer max) {
        log.info("API调用：年龄范围查询，范围: {} - {}", min, max);
        List<UserEntity> users = userService.findByAgeRange(min, max);
        return ResponseEntity.ok(users);
    }

    /**
     * 根据部门查询用户
     * GET /api/users/department/{departmentId}
     */
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<UserEntity>> getUsersByDepartment(@PathVariable Long departmentId) {
        log.info("API调用：根据部门查询用户，部门ID: {}", departmentId);
        List<UserEntity> users = userService.findByDepartmentId(departmentId);
        return ResponseEntity.ok(users);
    }

    /**
     * 根据性别查询用户
     * GET /api/users/gender/{gender}
     */
    @GetMapping("/gender/{gender}")
    public ResponseEntity<List<UserEntity>> getUsersByGender(@PathVariable String gender) {
        log.info("API调用：根据性别查询用户，性别: {}", gender);
        List<UserEntity> users = userService.findByGender(gender);
        return ResponseEntity.ok(users);
    }

    // =========================
    // 关联查询 API - 展示MyBatis关联映射
    // =========================

    /**
     * 查询用户及其部门信息
     * GET /api/users/{id}/with-department
     */
    @GetMapping("/{id}/with-department")
    public ResponseEntity<UserEntity> getUserWithDepartment(@PathVariable Long id) {
        log.info("API调用：查询用户及部门信息，用户ID: {}", id);
        UserEntity user = userService.findWithDepartment(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    /**
     * 查询用户及其角色列表
     * GET /api/users/{id}/with-roles
     */
    @GetMapping("/{id}/with-roles")
    public ResponseEntity<UserEntity> getUserWithRoles(@PathVariable Long id) {
        log.info("API调用：查询用户及角色信息，用户ID: {}", id);
        UserEntity user = userService.findWithRoles(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    /**
     * 查询用户及其项目列表
     * GET /api/users/{id}/with-projects
     */
    @GetMapping("/{id}/with-projects")
    public ResponseEntity<UserEntity> getUserWithProjects(@PathVariable Long id) {
        log.info("API调用：查询用户及项目信息，用户ID: {}", id);
        UserEntity user = userService.findWithProjects(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    /**
     * 查询用户完整信息
     * GET /api/users/{id}/full-info
     */
    @GetMapping("/{id}/full-info")
    public ResponseEntity<UserEntity> getUserFullInfo(@PathVariable Long id) {
        log.info("API调用：查询用户完整信息，用户ID: {}", id);
        UserEntity user = userService.findFullInfo(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    // =========================
    // 分页查询 API - 展示MyBatis分页
    // =========================

    /**
     * 分页查询用户列表
     * GET /api/users/page?page=0&size=10
     */
    @GetMapping("/page")
    public ResponseEntity<Page<UserEntity>> getUsersWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("API调用：分页查询用户，页码: {}, 大小: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<UserEntity> users = userService.findWithPagination(pageable);
        return ResponseEntity.ok(users);
    }

    /**
     * 条件分页查询
     * GET /api/users/search/page?username=xxx&page=0&size=10
     */
    @GetMapping("/search/page")
    public ResponseEntity<Page<UserEntity>> searchUsersWithPagination(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("API调用：条件分页查询用户");
        
        Map<String, Object> conditions = new HashMap<>();
        if (username != null) conditions.put("username", username);
        if (minAge != null) conditions.put("minAge", minAge);
        if (maxAge != null) conditions.put("maxAge", maxAge);
        if (gender != null) conditions.put("gender", gender);
        if (departmentId != null) conditions.put("departmentId", departmentId);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<UserEntity> users = userService.findByConditionsWithPagination(conditions, pageable);
        return ResponseEntity.ok(users);
    }

    // =========================
    // 统计查询 API - 展示聚合函数
    // =========================

    /**
     * 按部门统计用户数量
     * GET /api/users/stats/department
     */
    @GetMapping("/stats/department")
    public ResponseEntity<List<Map<String, Object>>> getUserStatsByDepartment() {
        log.info("API调用：按部门统计用户数量");
        List<Map<String, Object>> stats = userService.countUsersByDepartment();
        return ResponseEntity.ok(stats);
    }

    /**
     * 按性别统计用户数量
     * GET /api/users/stats/gender
     */
    @GetMapping("/stats/gender")
    public ResponseEntity<List<Map<String, Object>>> getUserStatsByGender() {
        log.info("API调用：按性别统计用户数量");
        List<Map<String, Object>> stats = userService.countUsersByGender();
        return ResponseEntity.ok(stats);
    }

    /**
     * 获取用户详细统计信息
     * GET /api/users/stats/overview
     */
    @GetMapping("/stats/overview")
    public ResponseEntity<Map<String, Object>> getUserStatisticsOverview() {
        log.info("API调用：获取用户统计概览");
        Map<String, Object> stats = userService.getUserStatistics();
        return ResponseEntity.ok(stats);
    }

    // =========================
    // 批量操作 API - 展示MyBatis批量处理
    // =========================

    /**
     * 批量创建用户
     * POST /api/users/batch
     */
    @PostMapping("/batch")
    public ResponseEntity<List<UserEntity>> batchCreateUsers(@RequestBody List<UserEntity> users) {
        log.info("API调用：批量创建用户，数量: {}", users.size());
        List<UserEntity> savedUsers = userService.batchSave(users);
        return ResponseEntity.ok(savedUsers);
    }

    /**
     * 批量更新用户
     * PUT /api/users/batch
     */
    @PutMapping("/batch")
    public ResponseEntity<Map<String, Object>> batchUpdateUsers(@RequestBody List<UserEntity> users) {
        log.info("API调用：批量更新用户，数量: {}", users.size());
        int updatedCount = userService.batchUpdate(users);
        
        Map<String, Object> result = new HashMap<>();
        result.put("updatedCount", updatedCount);
        result.put("message", "批量更新完成");
        
        return ResponseEntity.ok(result);
    }

    /**
     * 批量删除用户
     * DELETE /api/users/batch
     */
    @DeleteMapping("/batch")
    public ResponseEntity<Map<String, Object>> batchDeleteUsers(@RequestBody List<Long> ids) {
        log.info("API调用：批量删除用户，ID列表: {}", ids);
        int deletedCount = userService.batchDelete(ids);
        
        Map<String, Object> result = new HashMap<>();
        result.put("deletedCount", deletedCount);
        result.put("message", "批量删除完成");
        
        return ResponseEntity.ok(result);
    }

    // =========================
    // 时间范围查询 API
    // =========================

    /**
     * 查询最近N天创建的用户
     * GET /api/users/recent?days=7
     */
    @GetMapping("/recent")
    public ResponseEntity<List<UserEntity>> getRecentUsers(@RequestParam(defaultValue = "7") int days) {
        log.info("API调用：查询最近{}天创建的用户", days);
        List<UserEntity> users = userService.findRecentCreated(days);
        return ResponseEntity.ok(users);
    }

    // =========================
    // 业务操作 API - 展示事务处理
    // =========================

    /**
     * 用户转部门
     * PUT /api/users/{userId}/transfer-department
     */
    @PutMapping("/{userId}/transfer-department")
    public ResponseEntity<Map<String, Object>> transferUserToDepartment(
            @PathVariable Long userId,
            @RequestBody Map<String, Long> request) {
        
        Long newDepartmentId = request.get("departmentId");
        log.info("API调用：用户转部门，用户ID: {}, 新部门ID: {}", userId, newDepartmentId);
        
        userService.transferUserToDepartment(userId, newDepartmentId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("message", "用户转部门成功");
        result.put("userId", userId);
        result.put("newDepartmentId", newDepartmentId);
        
        return ResponseEntity.ok(result);
    }

    /**
     * 批量分配角色给用户
     * POST /api/users/assign-roles
     */
    @PostMapping("/assign-roles")
    public ResponseEntity<Map<String, Object>> assignRolesToUsers(@RequestBody Map<String, List<Long>> request) {
        List<Long> userIds = request.get("userIds");
        List<Long> roleIds = request.get("roleIds");
        
        log.info("API调用：批量分配角色，用户ID列表: {}, 角色ID列表: {}", userIds, roleIds);
        
        userService.assignRolesToUsers(userIds, roleIds);
        
        Map<String, Object> result = new HashMap<>();
        result.put("message", "角色分配成功");
        result.put("userCount", userIds.size());
        result.put("roleCount", roleIds.size());
        
        return ResponseEntity.ok(result);
    }
}
