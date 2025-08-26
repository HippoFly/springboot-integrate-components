package com.common.bigdata.generator;

import com.common.bigdata.entity.core.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 数据生成结果类
 * 包含生成的所有实体数据
 */
@Data
@Builder
public class GenerationResult {
    
    /**
     * 生成的部门列表
     */
    private List<Department> departments;
    
    /**
     * 生成的角色列表
     */
    private List<Role> roles;
    
    /**
     * 生成的用户列表
     */
    private List<User> users;
    
    /**
     * 生成的项目列表
     */
    private List<Project> projects;
    
    /**
     * 生成耗时（毫秒）
     */
    private Long generationTime;
    
    /**
     * 获取统计信息
     */
    public String getStatistics() {
        return String.format("部门: %d个, 角色: %d个, 用户: %d个, 项目: %d个", 
                departments != null ? departments.size() : 0,
                roles != null ? roles.size() : 0,
                users != null ? users.size() : 0,
                projects != null ? projects.size() : 0);
    }
}
