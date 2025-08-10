package com.example.jpa;

import com.example.jpa.entity.User;
import com.example.jpa.repository.UserRepository;
import com.example.jpa.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JPA应用测试类
 */
@SpringBootTest
class JpaApplicationTests {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    
    @Test
    void contextLoads() {
        // 测试Spring上下文是否正确加载
        assertNotNull(userRepository);
        assertNotNull(userService);
    }
    
    @Test
    void testUserCrudOperations() {
        // 测试用户CRUD操作
        User user = new User("testuser", "test@example.com", 25);
        User savedUser = userService.saveUser(user);
        
        // 验证保存成功
        assertNotNull(savedUser.getId());
        assertEquals("testuser", savedUser.getUsername());
        
        // 测试查找
        User foundUser = userService.findUserById(savedUser.getId());
        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUsername());
        
        // 测试更新邮箱
        int updatedCount = userService.updateUserEmail("testuser", "newemail@example.com");
        assertEquals(1, updatedCount);
        
        // 测试删除
        userService.deleteUserById(savedUser.getId());
        User deletedUser = userService.findUserById(savedUser.getId());
        assertNull(deletedUser);
    }
    
    @Test
    void testUserQueries() {
        // 测试各种查询方法
        User user1 = new User("alice", "alice@example.com", 25);
        User user2 = new User("bob", "bob@example.com", 30);
        User user3 = new User("charlie", "charlie@example.com", 35);
        
        userService.saveUser(user1);
        userService.saveUser(user2);
        userService.saveUser(user3);
        
        // 测试根据用户名查找
        User foundUser = userService.findUserByUsername("alice");
        assertNotNull(foundUser);
        assertEquals("alice", foundUser.getUsername());
        
        // 测试年龄范围查询
        List<User> usersInAgeRange = userService.findUsersByAgeRange(25, 30);
        assertEquals(2, usersInAgeRange.size());
        
        // 测试统计
        long userCount = userService.countUsers();
        assertTrue(userCount >= 3);
        
        // 清理测试数据
        userService.deleteAllUsers();
    }
}