package com.example.jpa.repository;

import com.example.jpa.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色数据访问接口
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    
    /**
     * 根据角色名称查找角色
     */
    Role findByRoleName(String roleName);
    
    /**
     * 根据状态查找角色
     */
    List<Role> findByStatus(Role.RoleStatus status);
    
    /**
     * 根据角色名称模糊查询
     */
    List<Role> findByRoleNameContaining(String roleName);
}
