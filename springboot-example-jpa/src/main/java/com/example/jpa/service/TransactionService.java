package com.example.jpa.service;

import com.example.jpa.entity.User;
import com.example.jpa.entity.Department;
import com.example.jpa.entity.Project;
import com.example.jpa.repository.UserRepository;
import com.example.jpa.repository.DepartmentRepository;
import com.example.jpa.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 事务管理服务类
 * 演示 JPA 事务管理的各种特性
 */
@Service
public class TransactionService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private ProjectRepository projectRepository;
    
    /**
     * 演示基本事务管理 - 默认传播行为和隔离级别
     */
    @Transactional
    public User createUserWithDepartment(User user, Department department) {
        // 先保存部门
        Department savedDepartment = departmentRepository.save(department);
        
        // 设置用户所属部门
        user.setDepartment(savedDepartment);
        
        // 保存用户
        return userRepository.save(user);
    }
    
    /**
     * 演示事务回滚 - 当发生异常时自动回滚
     */
    @Transactional(rollbackFor = Exception.class)
    public void createUserWithRollback(User user, boolean shouldFail) throws Exception {
        userRepository.save(user);
        
        if (shouldFail) {
            throw new RuntimeException("模拟业务异常，触发事务回滚");
        }
    }
    
    /**
     * 演示只读事务 - 优化性能，防止意外修改
     */
    @Transactional(readOnly = true)
    public List<User> findAllUsersReadOnly() {
        return userRepository.findAll();
    }
    
    /**
     * 演示事务传播行为 - REQUIRES_NEW
     * 总是创建新事务，挂起当前事务
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Department createDepartmentInNewTransaction(Department department) {
        return departmentRepository.save(department);
    }
    
    /**
     * 演示事务传播行为 - NESTED
     * 如果当前存在事务，则在嵌套事务内执行
     */
    @Transactional(propagation = Propagation.NESTED)
    public Project createProjectNested(Project project) {
        return projectRepository.save(project);
    }
    
    /**
     * 演示事务隔离级别 - READ_COMMITTED
     * 防止脏读，但允许不可重复读和幻读
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public User findUserWithReadCommitted(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
    
    /**
     * 演示事务隔离级别 - SERIALIZABLE
     * 最高隔离级别，防止脏读、不可重复读和幻读
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<User> findUsersWithSerializable() {
        return userRepository.findAll();
    }
    
    /**
     * 演示复杂事务场景 - 批量操作
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchCreateUsersWithDepartments(List<User> users, List<Department> departments) {
        // 批量保存部门
        List<Department> savedDepartments = departmentRepository.saveAll(departments);
        
        // 为每个用户分配部门
        for (int i = 0; i < users.size() && i < savedDepartments.size(); i++) {
            users.get(i).setDepartment(savedDepartments.get(i));
        }
        
        // 批量保存用户
        userRepository.saveAll(users);
    }
    
    /**
     * 演示编程式事务管理
     */
    public void programmaticTransaction() {
        // 这里可以使用 TransactionTemplate 或 PlatformTransactionManager
        // 进行编程式事务管理，但通常推荐使用声明式事务
    }
    
    /**
     * 演示事务超时设置
     */
    @Transactional(timeout = 30) // 30秒超时
    public User createUserWithTimeout(User user) {
        // 模拟长时间操作
        try {
            Thread.sleep(1000); // 休眠1秒
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return userRepository.save(user);
    }
    
    /**
     * 演示多数据源事务管理（如果配置了多数据源）
     */
    @Transactional("transactionManager") // 指定事务管理器
    public User createUserWithSpecificTransactionManager(User user) {
        return userRepository.save(user);
    }
}
