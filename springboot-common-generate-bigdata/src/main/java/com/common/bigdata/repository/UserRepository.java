package com.common.bigdata.repository;

import com.common.bigdata.entity.core.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户数据访问层
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * 根据用户名查找用户
     */
    Optional<User> findByUsername(String username);
    
    /**
     * 根据邮箱查找用户
     */
    Optional<User> findByEmail(String email);
    
    /**
     * 根据部门ID查找用户
     */
    List<User> findByDepartmentId(Long departmentId);
    
    /**
     * 根据年龄范围查找用户
     */
    List<User> findByAgeBetween(Integer minAge, Integer maxAge);
    
    /**
     * 根据真实姓名模糊查询
     */
    List<User> findByRealNameContaining(String realName);
    
    /**
     * 统计部门用户数量
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.department.id = :departmentId")
    Long countByDepartmentId(@Param("departmentId") Long departmentId);
    
    /**
     * 查找参与项目数量最多的用户
     */
    @Query("SELECT u FROM User u LEFT JOIN u.projects p GROUP BY u ORDER BY COUNT(p) DESC")
    List<User> findUsersOrderByProjectCount();
}
