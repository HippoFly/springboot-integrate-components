package com.example.jpa.repository;

import com.example.jpa.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 员工数据访问接口
 * 演示继承映射的查询操作
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    
    /**
     * 根据员工编号查找员工
     */
    Employee findByEmployeeCode(String employeeCode);
    
    /**
     * 根据姓名模糊查询员工
     */
    List<Employee> findByNameContaining(String name);
    
    /**
     * 根据部门ID查找员工
     */
    List<Employee> findByDepartmentId(Long departmentId);
    
    /**
     * 查找基本工资大于指定值的员工
     */
    List<Employee> findByBaseSalaryGreaterThan(BigDecimal baseSalary);
    
    /**
     * 使用 JPQL 查询特定类型的员工
     */
    @Query("SELECT e FROM Employee e WHERE TYPE(e) = :employeeType")
    List<Employee> findByEmployeeType(@Param("employeeType") Class<? extends Employee> employeeType);
    
    /**
     * 统计各类型员工数量
     */
    @Query("SELECT TYPE(e), COUNT(e) FROM Employee e GROUP BY TYPE(e)")
    List<Object[]> countByEmployeeType();
}
