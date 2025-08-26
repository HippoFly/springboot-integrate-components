package com.example.jpa.controller;

import com.example.jpa.dto.Mapper;
import com.example.jpa.dto.UserDTO;
import com.example.jpa.entity.User;
import com.example.jpa.service.UserQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户高级查询控制器
 * 演示 JPA Criteria API 和 Specification 的使用
 */
@RestController
@RequestMapping("/users/query")
@Tag(name = "用户高级查询接口", description = "演示 JPA 高级查询功能")
public class UserQueryController {
    
    @Autowired
    private UserQueryService userQueryService;
    
    /**
     * 动态查询用户
     */
    @GetMapping("/dynamic")
    @Operation(summary = "动态查询用户", description = "使用 Specification 进行动态查询")
    public Page<UserDTO> findUsersDynamically(
            @Parameter(description = "用户名") @RequestParam(required = false) String username,
            @Parameter(description = "邮箱") @RequestParam(required = false) String email,
            @Parameter(description = "最小年龄") @RequestParam(required = false) Integer minAge,
            @Parameter(description = "最大年龄") @RequestParam(required = false) Integer maxAge,
            @Parameter(description = "部门ID") @RequestParam(required = false) Long departmentId,
            @Parameter(description = "开始时间") @RequestParam(required = false) 
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) 
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "排序字段") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "排序方向") @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<User> users = userQueryService.findUsersBySpecification(
            username, email, minAge, maxAge, departmentId, startTime, endTime, pageable);
        
        return users.map(Mapper::toUserDTO);
    }
    
    /**
     * 查询用户统计信息
     */
    @GetMapping("/statistics")
    @Operation(summary = "查询用户统计信息", description = "使用 Criteria API 进行复杂统计查询")
    public List<Object[]> getUserStatistics(
            @Parameter(description = "用户名") @RequestParam(required = false) String username,
            @Parameter(description = "最小年龄") @RequestParam(required = false) Integer minAge,
            @Parameter(description = "部门ID") @RequestParam(required = false) Long departmentId) {
        
        return userQueryService.findUsersWithStatistics(username, minAge, departmentId);
    }
    
    /**
     * 查找参与项目最多的用户
     */
    @GetMapping("/most-projects")
    @Operation(summary = "查找参与项目最多的用户", description = "使用 Criteria API 子查询")
    public List<UserDTO> getUsersWithMostProjects() {
        List<User> users = userQueryService.findUsersWithMostProjects();
        return users.stream()
                .map(Mapper::toUserDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 按部门和年龄查询用户
     */
    @GetMapping("/by-department-age")
    @Operation(summary = "按部门和年龄查询用户", description = "使用 Criteria API 连接查询")
    public List<UserDTO> getUsersByDepartmentAndAge(
            @Parameter(description = "部门名称") @RequestParam(required = false) String departmentName,
            @Parameter(description = "最小年龄") @RequestParam(required = false) Integer minAge) {
        
        List<User> users = userQueryService.findUsersByDepartmentAndAge(departmentName, minAge);
        return users.stream()
                .map(Mapper::toUserDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取部门统计信息
     */
    @GetMapping("/department-statistics")
    @Operation(summary = "获取部门统计信息", description = "使用 Criteria API 聚合查询")
    public List<Object[]> getDepartmentStatistics() {
        return userQueryService.getDepartmentStatistics();
    }
}
