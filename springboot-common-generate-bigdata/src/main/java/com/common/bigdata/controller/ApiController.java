package com.common.bigdata.controller;

import com.common.bigdata.generator.BigDataGenerator;
import com.common.bigdata.generator.GenerationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST API控制器
 * 提供数据生成的API接口
 */
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {
    
    private final BigDataGenerator bigDataGenerator;
    
    /**
     * 生成数据API
     */
    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateData(@RequestParam("userCount") int userCount) {
        try {
            if (userCount <= 0 || userCount > 100000) {
                return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "用户数量必须在1-100000之间"));
            }
            
            GenerationResult result = bigDataGenerator.generateData(userCount);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "数据生成成功");
            response.put("statistics", result.getStatistics());
            response.put("data", Map.of(
                "userCount", result.getUsers().size(),
                "projectCount", result.getProjects().size(),
                "departmentCount", result.getDepartments().size(),
                "roleCount", result.getRoles().size()
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("API数据生成失败", e);
            return ResponseEntity.internalServerError()
                .body(Map.of("success", false, "message", "数据生成失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取完整数据（JSON格式）
     */
    @PostMapping("/generate/full")
    public ResponseEntity<Map<String, Object>> generateFullData(@RequestParam("userCount") int userCount) {
        try {
            GenerationResult result = bigDataGenerator.generateData(userCount);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("users", result.getUsers());
            response.put("departments", result.getDepartments());
            response.put("projects", result.getProjects());
            response.put("roles", result.getRoles());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("完整数据生成失败", e);
            return ResponseEntity.internalServerError()
                .body(Map.of("success", false, "message", "完整数据生成失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取用户数据
     */
    @PostMapping("/generate/users")
    public ResponseEntity<Map<String, Object>> generateUsers(@RequestParam("userCount") int userCount) {
        try {
            GenerationResult result = bigDataGenerator.generateData(userCount);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("users", result.getUsers());
            response.put("count", result.getUsers().size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("用户数据生成失败", e);
            return ResponseEntity.internalServerError()
                .body(Map.of("success", false, "message", "用户数据生成失败: " + e.getMessage()));
        }
    }
    
    /**
     * 健康检查
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "Big Data Generator",
            "timestamp", System.currentTimeMillis()
        ));
    }
}
