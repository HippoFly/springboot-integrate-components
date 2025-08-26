package com.example.mybatis.mapper;

import com.example.mybatis.entity.DeptEntity;
import com.example.mybatis.entity.UserEntity;
import com.example.mybatis.mapper.UserMapperXml;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MyBatis用户Mapper测试类
 * 展示各种MyBatis查询功能的测试用例
 */
@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapperXml userMapperXml;

    @Test
    public void testSelectById() {
        System.out.println("=== 测试根据ID查询用户 ===");
        UserEntity user = userMapperXml.selectById(1L);
        System.out.println("查询结果: " + user);
    }

    @Test
    public void testSelectByUsername() {
        System.out.println("=== 测试根据用户名查询用户 ===");
        UserEntity user = userMapperXml.selectByUsername("user001");
        System.out.println("查询结果: " + user);
    }

    @Test
    public void testDynamicQuery() {
        System.out.println("=== 测试动态SQL查询 ===");
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("gender", "男");
        conditions.put("minAge", 25);
        conditions.put("maxAge", 35);
        
        List<UserEntity> users = userMapperXml.selectByConditions(conditions);
        System.out.println("查询结果数量: " + users.size());
        users.forEach(System.out::println);
    }

    @Test
    public void testSelectWithDepartment() {
        System.out.println("=== 测试用户与部门关联查询 ===");
        UserEntity user = userMapperXml.selectWithDepartment(1L);
        System.out.println("用户信息: " + user);
        if (user != null && user.getDepartment() != null) {
            System.out.println("部门信息: " + user.getDepartment());
        }
    }

    @Test
    public void testPagination() {
        System.out.println("=== 测试分页查询 ===");
        List<UserEntity> users = userMapperXml.selectWithPagination(0, 5);
        System.out.println("分页查询结果数量: " + users.size());
        users.forEach(user -> System.out.println("用户: " + user.getUsername()));
        
        long total = userMapperXml.countTotal();
        System.out.println("总用户数: " + total);
    }

    @Test
    public void testStatistics() {
        System.out.println("=== 测试统计查询 ===");
        
        List<Map<String, Object>> deptStats = userMapperXml.countUsersByDepartment();
        System.out.println("按部门统计:");
        deptStats.forEach(stat -> System.out.println(stat));
        
        List<Map<String, Object>> genderStats = userMapperXml.countUsersByGender();
        System.out.println("按性别统计:");
        genderStats.forEach(stat -> System.out.println(stat));
    }

    @Test
    public void testInsertUser() {
        System.out.println("=== 测试插入用户 ===");
        UserEntity user = new UserEntity();
        user.setUsername("testuser" + System.currentTimeMillis());
        user.setRealName("测试用户");
        user.setEmail("test@example.com");
        user.setAge(28);
        user.setGender("男");
        user.setPhone("13800138000");
        user.setDepartmentId(1L);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setCreatedBy("test");
        user.setUpdatedBy("test");
        user.setVersion(1L);
        
        int result = userMapperXml.insert(user);
        System.out.println("插入结果: " + result);
        System.out.println("生成的ID: " + user.getId());
    }
}