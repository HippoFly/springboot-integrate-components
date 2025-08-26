package com.common.bigdata.repository;

import com.common.bigdata.entity.core.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 角色数据访问层
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    /**
     * 根据角色名称查找角色
     */
    Optional<Role> findByRoleName(String roleName);
    
    /**
     * 根据角色状态查找角色
     */
    List<Role> findByStatus(Role.RoleStatus status);
    
    /**
     * 查找活跃角色
     */
    @Query("SELECT r FROM Role r WHERE r.status = 'ACTIVE'")
    List<Role> findActiveRoles();
    
    /**
     * 统计拥有某角色的用户数量
     */
    @Query("SELECT COUNT(u) FROM Role r JOIN r.users u WHERE r.id = :roleId")
    Long countUsersByRoleId(@Param("roleId") Long roleId);
    
    /**
     * 查找用户数量最多的角色
     */
    @Query("SELECT r FROM Role r LEFT JOIN r.users u GROUP BY r ORDER BY COUNT(u) DESC")
    List<Role> findRolesOrderByUserCount();
}
