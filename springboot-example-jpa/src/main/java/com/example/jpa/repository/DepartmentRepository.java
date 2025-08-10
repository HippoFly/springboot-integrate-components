package com.example.jpa.repository;

import com.example.jpa.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 部门数据访问接口
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {
    
    /**
     * 根据部门名称查找部门
     * 
     * @param name 部门名称
     * @return 部门对象
     */
    Department findByName(String name);
    
    /**
     * 根据部门名称模糊查询部门列表
     * 
     * @param name 部门名称
     * @return 部门列表
     */
    List<Department> findByNameContaining(String name);
}