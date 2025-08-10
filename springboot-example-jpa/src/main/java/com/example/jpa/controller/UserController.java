package com.example.jpa.controller;

import com.alibaba.fastjson.JSON;
import com.example.jpa.entity.User;
import com.example.jpa.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器类，演示JPA的REST API接口
 */
@RestController
@RequestMapping("/users")
@Tag(name = "用户管理接口", description = "用户相关的CRUD操作接口")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * 创建用户
     * 
     * @param user 用户对象
     * @return 创建后的用户对象
     */
    @PostMapping
    @Operation(summary = "创建用户", description = "创建一个新用户")
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }
    
    /**
     * 批量创建用户
     * 
     * @param users 用户列表
     * @return 创建后的用户列表
     */
    @PostMapping("/batch")
    @Operation(summary = "批量创建用户", description = "批量创建多个用户")
    public List<User> createUsers(@RequestBody List<User> users) {
        return userService.saveUsers(users);
    }
    
    /**
     * 根据ID获取用户
     * 
     * @param id 用户ID
     * @return 用户对象
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取用户", description = "通过用户ID获取用户信息")
    public User getUserById(@Parameter(description = "用户ID") @PathVariable Long id) {
        return userService.findUserById(id);
    }
    
    /**
     * 获取所有用户
     * 
     * @return 用户列表
     */
    @GetMapping
    @Operation(summary = "获取所有用户", description = "获取系统中所有用户列表")
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }
    
    /**
     * 分页获取用户
     * 
     * @param page 页码（从0开始）
     * @param size 每页大小
     * @return 用户分页数据
     */
    @GetMapping("/page")
    @Operation(summary = "分页获取用户", description = "分页查询用户列表")
    public Page<User> getUsersWithPagination(
            @Parameter(description = "页码（从0开始）") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        return userService.findUsersWithPagination(page, size);
    }
    
    /**
     * 根据用户名获取用户
     * 
     * @param username 用户名
     * @return 用户对象
     */
    @GetMapping("/username/{username}")
    @Operation(summary = "根据用户名获取用户", description = "通过用户名获取用户信息")
    public User getUserByUsername(@Parameter(description = "用户名") @PathVariable String username) {
        return userService.findUserByUsername(username);
    }
    
    /**
     * 根据用户名模糊查询用户
     * 
     * @param username 用户名
     * @return 用户列表
     */
    @GetMapping("/search")
    @Operation(summary = "根据用户名模糊查询用户", description = "根据用户名进行模糊搜索")
    public List<User> searchUsers(@Parameter(description = "用户名") @RequestParam String username) {
        return userService.findUsersByUsernameContaining(username);
    }
    
    /**
     * 根据年龄范围查询用户
     * 
     * @param minAge 最小年龄
     * @param maxAge 最大年龄
     * @return 用户列表
     */
    @GetMapping("/age")
    @Operation(summary = "根据年龄范围查询用户", description = "查询指定年龄范围内的用户")
    public List<User> getUsersByAgeRange(
            @Parameter(description = "最小年龄") @RequestParam Integer minAge,
            @Parameter(description = "最大年龄") @RequestParam Integer maxAge) {
        return userService.findUsersByAgeRange(minAge, maxAge);
    }
    
    /**
     * 更新用户邮箱
     * 
     * @param username 用户名
     * @param email 新邮箱
     * @return 更新结果
     */
    @PutMapping("/{username}/email")
    @Operation(summary = "更新用户邮箱", description = "更新指定用户的邮箱地址")
    public String updateUserEmail(
            @Parameter(description = "用户名") @PathVariable String username,
            @Parameter(description = "新邮箱") @RequestParam String email) {
        int result = userService.updateUserEmail(username, email);
        return "Updated " + result + " user(s)";
    }
    
    /**
     * 根据ID删除用户
     * 
     * @param id 用户ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "根据ID删除用户", description = "通过用户ID删除用户")
    public String deleteUser(@Parameter(description = "用户ID") @PathVariable Long id) {
        userService.deleteUserById(id);
        return "User deleted successfully";
    }
    
    /**
     * 删除所有用户
     * 
     * @return 删除结果
     */
    @DeleteMapping
    @Operation(summary = "删除所有用户", description = "删除系统中所有用户")
    public String deleteAllUsers() {
        userService.deleteAllUsers();
        return "All users deleted successfully";
    }
    
    /**
     * 获取用户总数
     * 
     * @return 用户总数
     */
    @GetMapping("/count")
    @Operation(summary = "获取用户总数", description = "获取系统中用户的总数量")
    public long getUserCount() {
        return userService.countUsers();
    }
}