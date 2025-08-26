package com.common.bigdata.repository;

import com.common.bigdata.entity.core.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 部门数据访问层
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    /**
     * 根据部门名称查找部门
     */
    Optional<Department> findByName(String name);
    
    /**
     * 根据部门名称模糊查询
     */
    List<Department> findByNameContaining(String name);
    
    /**
     * 查询有用户的部门
     */
    @Query("SELECT DISTINCT d FROM Department d WHERE SIZE(d.users) > 0")
    List<Department> findDepartmentsWithUsers();
    
    /**
     * 按用户数量排序查询部门
     */
    @Query("SELECT d FROM Department d LEFT JOIN d.users u GROUP BY d ORDER BY COUNT(u) DESC")
    List<Department> findDepartmentsOrderByUserCount();
}
