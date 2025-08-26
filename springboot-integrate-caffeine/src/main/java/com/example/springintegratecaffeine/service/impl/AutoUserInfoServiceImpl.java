package com.example.springintegratecaffeine.service.impl;

import com.example.springintegratecaffeine.entity.UserInfo;
import com.example.springintegratecaffeine.service.AutoUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 自动用户信息服务实现类（使用 Spring Cache 注解）
 * 演示 Spring Cache 的各种注解使用方式
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "userInfo", cacheManager = "userCacheManager")
public class AutoUserInfoServiceImpl implements AutoUserInfoService {

    // 模拟数据库
    private final Map<Integer, UserInfo> database = new ConcurrentHashMap<>();
    
    {
        // 初始化一些测试数据
        for (int i = 1; i <= 10; i++) {
            database.put(i, createSampleUser(i));
        }
    }

    /**
     * 增加用户信息
     * 使用 @CachePut 注解，执行方法并将结果存入缓存
     */
    @Override
    @CachePut(key = "#userInfo.id", condition = "#userInfo.id != null")
    public void addUserInfo(UserInfo userInfo) {
        log.info("添加用户信息：{}", userInfo.getName());
        database.put(userInfo.getId(), userInfo);
    }

    /**
     * 获取用户信息（接口方法名为 getByName 但参数是 ID）
     * 使用 @Cacheable 注解，如果缓存中有数据则直接返回，否则执行方法并缓存结果
     */
    @Override
    @Cacheable(key = "#id", condition = "#id > 0", unless = "#result == null")
    public UserInfo getByName(Integer id) {
        log.info("从数据库查询用户信息，用户ID：{}", id);
        
        // 模拟数据库查询延迟
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        UserInfo userInfo = database.get(id);
        if (userInfo != null) {
            // 更新最后登录时间
            userInfo.setLastLoginTime(LocalDateTime.now());
        }
        
        log.info("查询结果：{}", userInfo != null ? "找到用户" : "用户不存在");
        return userInfo;
    }

    /**
     * 根据用户名获取用户信息
     * 演示复杂的缓存键生成
     */
    @Cacheable(key = "'name:' + #name", condition = "#name != null and #name.length() > 0")
    public UserInfo getByUserName(String name) {
        log.info("根据用户名查询用户信息：{}", name);
        
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return database.values().stream()
                .filter(user -> name.equals(user.getName()))
                .findFirst()
                .orElse(null);
    }

    /**
     * 获取用户列表
     * 演示集合类型的缓存
     */
    @Cacheable(key = "'list:' + #department + ':' + #status", 
               condition = "#department != null",
               cacheManager = "shortTermCacheManager")
    public List<UserInfo> getUsersByDepartment(String department, UserInfo.UserStatus status) {
        log.info("查询部门用户列表，部门：{}，状态：{}", department, status);
        
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return database.values().stream()
                .filter(user -> department.equals(user.getDepartment()))
                .filter(user -> status == null || status.equals(user.getStatus()))
                .collect(Collectors.toList());
    }

    /**
     * 更新用户信息
     * 使用 @CachePut 注解，执行方法并更新缓存
     */
    @Override
    @CachePut(key = "#userInfo.id", condition = "#userInfo.id != null")
    @CacheEvict(key = "'name:' + #userInfo.name", beforeInvocation = true)
    public UserInfo updateUserInfo(UserInfo userInfo) {
        log.info("更新用户信息：{}", userInfo.getName());
        
        // 模拟数据库更新
        database.put(userInfo.getId(), userInfo);
        
        return userInfo;
    }

    /**
     * 删除用户信息
     * 使用 @CacheEvict 注解，删除缓存中的数据
     */
    @Override
    @CacheEvict(key = "#id")
    public void deleteById(Integer id) {
        log.info("删除用户信息，用户ID：{}", id);
        database.remove(id);
    }

    /**
     * 创建示例用户
     */
    private UserInfo createSampleUser(int id) {
        return UserInfo.builder()
                .id(id)
                .name("用户" + id)
                .sex(id % 2 == 0 ? "女" : "男")
                .age(20 + id % 40)
                .email("user" + id + "@example.com")
                .phone("138" + String.format("%08d", id))
                .department(getDepartment(id % 5))
                .position(getPosition(id % 3))
                .salary(8000.0 + id * 100)
                .hireDate(LocalDateTime.now().minusDays(id * 10))
                .lastLoginTime(LocalDateTime.now().minusHours(id % 24))
                .status(UserInfo.UserStatus.ACTIVE)
                .tags(Arrays.asList("tag" + (id % 3), "tag" + (id % 5)))
                .permissions(Arrays.asList("read", id % 2 == 0 ? "write" : "readonly"))
                .build();
    }

    private String getDepartment(int index) {
        String[] departments = {"技术部", "产品部", "运营部", "市场部", "人事部"};
        return departments[index];
    }

    private String getPosition(int index) {
        String[] positions = {"工程师", "经理", "专员"};
        return positions[index];
    }
}
