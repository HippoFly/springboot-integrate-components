package com.example.jpa.service;

import com.example.jpa.entity.Department;
import com.example.jpa.entity.User;
import com.example.jpa.repository.DepartmentRepository;
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
 * 部门服务类，演示JPA一对多关系操作
 */
@Service
public class DepartmentService {
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * 保存部门
     * 
     * @param department 部门对象
     * @return 保存后的部门对象
     */
    public Department saveDepartment(Department department) {
        return departmentRepository.save(department);
    }
    
    /**
     * 批量保存部门
     * 
     * @param departments 部门列表
     * @return 保存后的部门列表
     */
    public List<Department> saveDepartments(List<Department> departments) {
        return departmentRepository.saveAll(departments);
    }
    
    /**
     * 根据ID查找部门
     * 
     * @param id 部门ID
     * @return 部门对象
     */
    public Department findDepartmentById(Long id) {
        Optional<Department> department = departmentRepository.findById(id);
        return department.orElse(null);
    }
    
    /**
     * 查找所有部门
     * 
     * @return 部门列表
     */
    public List<Department> findAllDepartments() {
        return departmentRepository.findAll();
    }
    
    /**
     * 分页查找部门
     * 
     * @param page 页码（从0开始）
     * @param size 每页大小
     * @return 部门分页数据
     */
    public Page<Department> findDepartmentsWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
        return departmentRepository.findAll(pageable);
    }
    
    /**
     * 根据部门名称查找部门
     * 
     * @param name 部门名称
     * @return 部门对象
     */
    public Department findDepartmentByName(String name) {
        return departmentRepository.findByName(name);
    }
    
    /**
     * 根据部门名称模糊查询部门
     * 
     * @param name 部门名称
     * @return 部门列表
     */
    public List<Department> findDepartmentsByNameContaining(String name) {
        return departmentRepository.findByNameContaining(name);
    }
    
    /**
     * 为部门分配用户
     * 
     * @param departmentId 部门ID
     * @param userId 用户ID
     * @return 更新后的部门对象
     */
    public Department assignUserToDepartment(Long departmentId, Long userId) {
        Department department = departmentRepository.findById(departmentId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        
        if (department != null && user != null) {
            user.setDepartment(department);
            userRepository.save(user);
        }
        return department;
    }
    
    /**
     * 获取指定部门下的所有用户
     * 
     * @param departmentId 部门ID
     * @return 用户列表
     */
    public List<User> findUsersByDepartmentId(Long departmentId) {
        return userRepository.findByDepartmentId(departmentId);
    }
    
    /**
     * 根据ID删除部门
     * 
     * @param id 部门ID
     */
    public void deleteDepartmentById(Long id) {
        departmentRepository.deleteById(id);
    }
    
    /**
     * 删除所有部门
     */
    public void deleteAllDepartments() {
        departmentRepository.deleteAll();
    }
    
    /**
     * 统计部门总数
     * 
     * @return 部门总数
     */
    public long countDepartments() {
        return departmentRepository.count();
    }
}