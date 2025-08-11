package com.example.jpa.controller;

import com.example.jpa.dto.Mapper;
import com.example.jpa.dto.ProjectDTO;
import com.example.jpa.dto.UserDTO;
import com.example.jpa.entity.User;
import com.example.jpa.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户控制器类，演示JPA基本注解和CRUD操作的REST API接口
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
    public UserDTO createUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return Mapper.toUserDTO(savedUser);
    }
    
    /**
     * 批量创建用户
     * 
     * @param users 用户列表
     * @return 创建后的用户列表
     */
    @PostMapping("/batch")
    @Operation(summary = "批量创建用户", description = "批量创建多个用户")
    public List<UserDTO> createUsers(@RequestBody List<User> users) {
        List<User> savedUsers = userService.saveUsers(users);
        return savedUsers.stream()
                .map(Mapper::toUserDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据ID获取用户
     * 
     * @param id 用户ID
     * @return 用户对象
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取用户", description = "通过用户ID获取用户信息")
    public UserDTO getUserById(@Parameter(description = "用户ID") @PathVariable Long id) {
        User user = userService.findUserById(id);
        return Mapper.toUserDTO(user);
    }
    
    /**
     * 获取所有用户
     * 
     * @return 用户列表
     */
    @GetMapping
    @Operation(summary = "获取所有用户", description = "获取系统中所有用户列表")
    public List<UserDTO> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return users.stream()
                .map(Mapper::toUserDTO)
                .collect(Collectors.toList());
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
    public Page<UserDTO> getUsersWithPagination(
            @Parameter(description = "页码（从0开始）") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        Page<User> users = userService.findUsersWithPagination(page, size);
        return users.map(Mapper::toUserDTO);
    }
    
    /**
     * 根据用户名获取用户
     * 
     * @param username 用户名
     * @return 用户对象
     */
    @GetMapping("/username/{username}")
    @Operation(summary = "根据用户名获取用户", description = "通过用户名获取用户信息")
    public UserDTO getUserByUsername(@Parameter(description = "用户名") @PathVariable String username) {
        User user = userService.findUserByUsername(username);
        return Mapper.toUserDTO(user);
    }
    
    /**
     * 根据用户名模糊查询用户
     * 
     * @param username 用户名
     * @return 用户列表
     */
    @GetMapping("/search")
    @Operation(summary = "根据用户名模糊查询用户", description = "根据用户名进行模糊搜索")
    public List<UserDTO> searchUsers(@Parameter(description = "用户名") @RequestParam String username) {
        List<User> users = userService.findUsersByUsernameContaining(username);
        return users.stream()
                .map(Mapper::toUserDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据年龄范围查询用户
     * 
     * @param minAge 最小年龄
     * @param maxAge 最大年龄
     * @return 用户列表
     */
    @GetMapping("/age")
    @Operation(summary = "根据年龄范围查询用户", description = "根据年龄范围查询用户")
    public List<UserDTO> getUsersByAgeRange(
            @Parameter(description = "最小年龄") @RequestParam int minAge,
            @Parameter(description = "最大年龄") @RequestParam int maxAge) {
        List<User> users = userService.findUsersByAgeRange(minAge, maxAge);
        return users.stream()
                .map(Mapper::toUserDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 为用户分配项目
     * 
     * @param userId 用户ID
     * @param projectId 项目ID
     * @return 更新后的用户对象
     */
    @PutMapping("/{userId}/projects/{projectId}")
    @Operation(summary = "为用户分配项目", description = "将指定用户分配到指定项目")
    public UserDTO assignProjectToUser(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "项目ID") @PathVariable Long projectId) {
        User user = userService.assignProjectToUser(userId, projectId);
        return Mapper.toUserDTO(user);
    }
    
    /**
     * 获取指定用户参与的所有项目
     * 
     * @param userId 用户ID
     * @return 项目列表
     */
    @GetMapping("/{userId}/projects")
    @Operation(summary = "获取指定用户参与的所有项目", description = "获取指定用户参与的所有项目列表")
    public List<ProjectDTO> getProjectsByUserId(@Parameter(description = "用户ID") @PathVariable Long userId) {
        User user = userService.findUserById(userId);
        return user.getProjects().stream()
                .map(Mapper::toProjectDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 更新用户邮箱
     * 
     * @param username 用户名
     * @param email 新邮箱
     * @return 更新记录数
     */
    @PutMapping("/email")
    @Operation(summary = "更新用户邮箱", description = "根据用户名更新用户邮箱")
    public int updateUserEmail(
            @Parameter(description = "用户名") @RequestParam String username,
            @Parameter(description = "新邮箱") @RequestParam String email) {
        return userService.updateUserEmail(username, email);
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