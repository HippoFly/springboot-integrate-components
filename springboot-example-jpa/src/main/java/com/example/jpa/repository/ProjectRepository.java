package com.example.jpa.repository;

import com.example.jpa.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 项目数据访问接口
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {
    
    /**
     * 根据项目名称查找项目
     * 
     * @param name 项目名称
     * @return 项目对象
     */
    Project findByName(String name);
    
    /**
     * 根据项目名称模糊查询项目列表
     * 
     * @param name 项目名称
     * @return 项目列表
     */
    List<Project> findByNameContaining(String name);
}