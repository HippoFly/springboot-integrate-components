package com.example.jpa.repository;

import com.example.jpa.entity.UserRole;
import com.example.jpa.entity.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户角色关联数据访问接口
 * 演示复合主键的操作
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId>, JpaSpecificationExecutor<UserRole> {
    
    /**
     * 根据用户ID查找用户角色
     */
    List<UserRole> findByIdUserId(Long userId);
    
    /**
     * 根据角色ID查找用户角色
     */
    List<UserRole> findByIdRoleId(Long roleId);
    
    /**
     * 查找激活的用户角色
     */
    List<UserRole> findByIsActiveTrue();
    
    /**
     * 查找指定时间范围内分配的角色
     */
    List<UserRole> findByAssignedDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * 查找即将过期的角色分配
     */
    List<UserRole> findByExpireDateBefore(LocalDateTime expireDate);
    
    /**
     * 使用 JPQL 查询用户的所有激活角色
     */
    @Query("SELECT ur FROM UserRole ur WHERE ur.id.userId = :userId AND ur.isActive = true")
    List<UserRole> findActiveRolesByUserId(@Param("userId") Long userId);
}
