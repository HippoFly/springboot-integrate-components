package com.example.springintegratecaffeine.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户信息实体
 * 演示缓存对象的设计
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户信息实体")
public class UserInfo {
    
    @Schema(description = "用户ID", example = "1")
    private Integer id;
    
    @Schema(description = "用户名", example = "张三")
    private String name;
    
    @Schema(description = "性别", example = "男")
    private String sex;
    
    @Schema(description = "年龄", example = "25")
    private Integer age;
    
    @Schema(description = "邮箱", example = "zhangsan@example.com")
    private String email;
    
    @Schema(description = "手机号", example = "13800138000")
    private String phone;
    
    @Schema(description = "部门", example = "技术部")
    private String department;
    
    @Schema(description = "职位", example = "软件工程师")
    private String position;
    
    @Schema(description = "薪资", example = "15000.00")
    private Double salary;
    
    @Schema(description = "入职时间")
    private LocalDateTime hireDate;
    
    @Schema(description = "最后登录时间")
    private LocalDateTime lastLoginTime;
    
    @Schema(description = "用户状态", example = "ACTIVE")
    private UserStatus status;
    
    @Schema(description = "用户标签")
    private List<String> tags;
    
    @Schema(description = "用户权限")
    private List<String> permissions;
    
    /**
     * 用户状态枚举
     */
    public enum UserStatus {
        ACTIVE("激活"),
        INACTIVE("未激活"),
        SUSPENDED("暂停"),
        DELETED("已删除");
        
        private final String description;
        
        UserStatus(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    /**
     * 计算对象大小（用于权重缓存演示）
     */
    public int calculateSize() {
        int size = 0;
        if (name != null) size += name.length() * 2; // Unicode字符
        if (email != null) size += email.length();
        if (phone != null) size += phone.length();
        if (department != null) size += department.length() * 2;
        if (position != null) size += position.length() * 2;
        if (tags != null) size += tags.stream().mapToInt(String::length).sum() * 2;
        if (permissions != null) size += permissions.stream().mapToInt(String::length).sum();
        size += 100; // 基础对象大小
        return size;
    }
}
