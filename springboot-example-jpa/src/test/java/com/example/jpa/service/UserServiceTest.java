package com.example.jpa.service;

import com.example.jpa.entity.User;
import com.example.jpa.entity.Department;
import com.example.jpa.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * 用户服务单元测试
 * 演示 JPA 服务层的单元测试
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserService userService;
    
    private User testUser;
    private Department testDepartment;
    
    @BeforeEach
    void setUp() {
        testDepartment = new Department();
        testDepartment.setId(1L);
        testDepartment.setName("技术部");
        testDepartment.setDescription("技术研发部门");
        
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setAge(25);
        testUser.setDepartment(testDepartment);
        testUser.setCreateTime(LocalDateTime.now());
        testUser.setUpdateTime(LocalDateTime.now());
    }
    
    @Test
    void testSaveUser() {
        // Given
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        
        // When
        User savedUser = userService.saveUser(testUser);
        
        // Then
        assertNotNull(savedUser);
        assertEquals("testuser", savedUser.getUsername());
        assertEquals("test@example.com", savedUser.getEmail());
        verify(userRepository, times(1)).save(testUser);
    }
    
    @Test
    void testFindUserById() {
        // Given
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        
        // When
        User foundUser = userService.findUserById(1L);
        
        // Then
        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
        assertEquals("testuser", foundUser.getUsername());
        verify(userRepository, times(1)).findById(1L);
    }
    
    @Test
    void testFindUserById_NotFound() {
        // Given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(RuntimeException.class, () -> userService.findUserById(999L));
        verify(userRepository, times(1)).findById(999L);
    }
    
    @Test
    void testFindAllUsers() {
        // Given
        List<User> users = Arrays.asList(testUser, new User());
        when(userRepository.findAll()).thenReturn(users);
        
        // When
        List<User> foundUsers = userService.findAllUsers();
        
        // Then
        assertNotNull(foundUsers);
        assertEquals(2, foundUsers.size());
        verify(userRepository, times(1)).findAll();
    }
    
    @Test
    void testFindUsersWithPagination() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(Arrays.asList(testUser));
        when(userRepository.findAll(pageable)).thenReturn(userPage);
        
        // When
        Page<User> result = userService.findUsersWithPagination(0, 10);
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        verify(userRepository, times(1)).findAll(pageable);
    }
    
    @Test
    void testFindUserByUsername() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(testUser);
        
        // When
        User foundUser = userService.findUserByUsername("testuser");
        
        // Then
        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }
    
    @Test
    void testFindUsersByUsernameContaining() {
        // Given
        List<User> users = Arrays.asList(testUser);
        when(userRepository.findByUsernameContaining("test")).thenReturn(users);
        
        // When
        List<User> foundUsers = userService.findUsersByUsernameContaining("test");
        
        // Then
        assertNotNull(foundUsers);
        assertEquals(1, foundUsers.size());
        assertEquals("testuser", foundUsers.get(0).getUsername());
        verify(userRepository, times(1)).findByUsernameContaining("test");
    }
    
    @Test
    void testFindUsersByAgeRange() {
        // Given
        List<User> users = Arrays.asList(testUser);
        when(userRepository.findByAgeBetween(20, 30)).thenReturn(users);
        
        // When
        List<User> foundUsers = userService.findUsersByAgeRange(20, 30);
        
        // Then
        assertNotNull(foundUsers);
        assertEquals(1, foundUsers.size());
        assertEquals(25, foundUsers.get(0).getAge());
        verify(userRepository, times(1)).findByAgeBetween(20, 30);
    }
    
    @Test
    void testUpdateUserEmail() {
        // Given
        when(userRepository.updateEmailByUsername("testuser", "newemail@example.com")).thenReturn(1);
        
        // When
        int updatedCount = userService.updateUserEmail("testuser", "newemail@example.com");
        
        // Then
        assertEquals(1, updatedCount);
        verify(userRepository, times(1)).updateEmailByUsername("testuser", "newemail@example.com");
    }
    
    @Test
    void testDeleteUserById() {
        // Given
        doNothing().when(userRepository).deleteById(anyLong());
        
        // When
        userService.deleteUserById(1L);
        
        // Then
        verify(userRepository, times(1)).deleteById(1L);
    }
    
    @Test
    void testCountUsers() {
        // Given
        when(userRepository.count()).thenReturn(5L);
        
        // When
        long count = userService.countUsers();
        
        // Then
        assertEquals(5L, count);
        verify(userRepository, times(1)).count();
    }
}
