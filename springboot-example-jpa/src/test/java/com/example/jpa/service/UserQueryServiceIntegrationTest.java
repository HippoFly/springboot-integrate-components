package com.example.jpa.service;

import com.example.jpa.entity.User;
import com.example.jpa.entity.Department;
import com.example.jpa.repository.UserRepository;
import com.example.jpa.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户查询服务集成测试
 * 演示 JPA Criteria API 和 Specification 的集成测试
 */
@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.properties.hibernate.cache.use_second_level_cache=false",
    "spring.jpa.properties.hibernate.cache.use_query_cache=false"
})
@Transactional
class UserQueryServiceIntegrationTest {
    
    @Autowired
    private UserQueryService userQueryService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    private Department testDepartment;
    private User testUser1;
    private User testUser2;
    
    @BeforeEach
    void setUp() {
        // 清理数据
        userRepository.deleteAll();
        departmentRepository.deleteAll();
        
        // 创建测试部门
        testDepartment = new Department();
        testDepartment.setName("测试部门");
        testDepartment.setDescription("用于测试的部门");
        testDepartment = departmentRepository.save(testDepartment);
        
        // 创建测试用户1
        testUser1 = new User();
        testUser1.setUsername("testuser1");
        testUser1.setEmail("test1@example.com");
        testUser1.setAge(25);
        testUser1.setDepartment(testDepartment);
        testUser1 = userRepository.save(testUser1);
        
        // 创建测试用户2
        testUser2 = new User();
        testUser2.setUsername("testuser2");
        testUser2.setEmail("test2@example.com");
        testUser2.setAge(30);
        testUser2.setDepartment(testDepartment);
        testUser2 = userRepository.save(testUser2);
    }
    
    @Test
    void testFindUsersBySpecification() {
        // When
        Page<User> result = userQueryService.findUsersBySpecification(
            "test", null, 20, 35, testDepartment.getId(), 
            null, null, PageRequest.of(0, 10));
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().stream()
            .allMatch(user -> user.getUsername().contains("test")));
    }
    
    @Test
    void testFindUsersBySpecification_WithAgeFilter() {
        // When
        Page<User> result = userQueryService.findUsersBySpecification(
            null, null, 28, 35, null, 
            null, null, PageRequest.of(0, 10));
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("testuser2", result.getContent().get(0).getUsername());
    }
    
    @Test
    void testFindUsersWithStatistics() {
        // When
        List<Object[]> result = userQueryService.findUsersWithStatistics("test", 20, testDepartment.getId());
        
        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    
    @Test
    void testFindUsersByDepartmentAndAge() {
        // When
        List<User> result = userQueryService.findUsersByDepartmentAndAge("测试", 20);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream()
            .allMatch(user -> user.getAge() >= 20));
    }
    
    @Test
    void testGetDepartmentStatistics() {
        // When
        List<Object[]> result = userQueryService.getDepartmentStatistics();
        
        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        
        // 验证统计结果包含部门名称和用户数量
        Object[] firstResult = result.get(0);
        assertEquals("测试部门", firstResult[0]); // 部门名称
        assertEquals(2L, firstResult[1]); // 用户数量
    }
}
