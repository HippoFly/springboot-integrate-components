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
        // 先清理可能存在的测试用户
        User existingUser = userService.findUserByUsername("testuser");
        if (existingUser != null) {
            userService.deleteUserById(existingUser.getId());
        }
        
        // 测试用户CRUD操作
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setAge(25);
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
        // 先清理可能存在的测试用户
        User existingUser1 = userService.findUserByUsername("alice_test");
        if (existingUser1 != null) {
            userService.deleteUserById(existingUser1.getId());
        }
        
        User existingUser2 = userService.findUserByUsername("bob_test");
        if (existingUser2 != null) {
            userService.deleteUserById(existingUser2.getId());
        }
        
        User existingUser3 = userService.findUserByUsername("charlie_test");
        if (existingUser3 != null) {
            userService.deleteUserById(existingUser3.getId());
        }
        
        // 测试各种查询方法
        User user1 = new User();
        user1.setUsername("alice_test");
        user1.setEmail("alice@example.com");
        user1.setAge(25);
        
        User user2 = new User();
        user2.setUsername("bob_test");
        user2.setEmail("bob@example.com");
        user2.setAge(30);
        
        User user3 = new User();
        user3.setUsername("charlie_test");
        user3.setEmail("charlie@example.com");
        user3.setAge(35);
        
        userService.saveUser(user1);
        userService.saveUser(user2);
        userService.saveUser(user3);
        
        // 测试根据用户名查找
        User foundUser = userService.findUserByUsername("alice_test");
        assertNotNull(foundUser);
        assertEquals("alice_test", foundUser.getUsername());
        
        // 测试年龄范围查询（只查询我们刚创建的测试用户）
        List<User> usersInAgeRange = userService.findUsersByAgeRange(25, 30);
        // 过滤出测试用户
        long testUsersInRange = usersInAgeRange.stream()
            .filter(u -> u.getUsername().endsWith("_test"))
            .count();
        assertEquals(2, testUsersInRange);
        
        // 清理测试数据
        User toDelete1 = userService.findUserByUsername("alice_test");
        if (toDelete1 != null) userService.deleteUserById(toDelete1.getId());
        
        User toDelete2 = userService.findUserByUsername("bob_test");
        if (toDelete2 != null) userService.deleteUserById(toDelete2.getId());
        
        User toDelete3 = userService.findUserByUsername("charlie_test");
        if (toDelete3 != null) userService.deleteUserById(toDelete3.getId());
    }
}