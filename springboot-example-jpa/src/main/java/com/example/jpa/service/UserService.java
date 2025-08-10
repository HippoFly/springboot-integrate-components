package com.example.jpa.service;

import com.example.jpa.entity.User;
import com.example.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 用户服务类，演示常用的JPA操作方法
 */
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * 保存用户
     * 
     * @param user 用户对象
     * @return 保存后的用户对象
     */
    public User saveUser(User user) {
        return userRepository.save(user);
    }
    
    /**
     * 批量保存用户
     * 
     * @param users 用户列表
     * @return 保存后的用户列表
     */
    public List<User> saveUsers(List<User> users) {
        return userRepository.saveAll(users);
    }
    
    /**
     * 根据ID查找用户
     * 
     * @param id 用户ID
     * @return 用户对象
     */
    public User findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }
    
    /**
     * 查找所有用户
     * 
     * @return 用户列表
     */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * 分页查找用户
     * 
     * @param page 页码（从0开始）
     * @param size 每页大小
     * @return 用户分页数据
     */
    public Page<User> findUsersWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("username"));
        return userRepository.findAll(pageable);
    }
    
    /**
     * 根据用户名查找用户
     * 
     * @param username 用户名
     * @return 用户对象
     */
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    /**
     * 根据用户名模糊查询用户
     * 
     * @param username 用户名
     * @return 用户列表
     */
    public List<User> findUsersByUsernameContaining(String username) {
        return userRepository.findByUsernameContaining(username);
    }
    
    /**
     * 根据年龄范围查询用户
     * 
     * @param minAge 最小年龄
     * @param maxAge 最大年龄
     * @return 用户列表
     */
    public List<User> findUsersByAgeRange(Integer minAge, Integer maxAge) {
        return userRepository.findByAgeBetween(minAge, maxAge);
    }
    
    /**
     * 更新用户邮箱
     * 
     * @param username 用户名
     * @param email 新邮箱
     * @return 更新记录数
     */
    public int updateUserEmail(String username, String email) {
        return userRepository.updateUserEmail(email, username);
    }
    
    /**
     * 根据ID删除用户
     * 
     * @param id 用户ID
     */
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
    
    /**
     * 删除所有用户
     */
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }
    
    /**
     * 统计用户总数
     * 
     * @return 用户总数
     */
    public long countUsers() {
        return userRepository.count();
    }
}