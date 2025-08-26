package com.common.bigdata.repository;

import com.common.bigdata.entity.core.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 项目数据访问层
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    
    /**
     * 根据项目名称查找项目
     */
    Optional<Project> findByName(String name);
    
    /**
     * 根据项目状态查找项目
     */
    List<Project> findByStatus(Project.ProjectStatus status);
    
    /**
     * 根据开始日期范围查找项目
     */
    List<Project> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * 查找正在进行的项目
     */
    @Query("SELECT p FROM Project p WHERE p.status = 'IN_PROGRESS'")
    List<Project> findActiveProjects();
    
    /**
     * 统计用户参与的项目数量
     */
    @Query("SELECT COUNT(p) FROM Project p JOIN p.users u WHERE u.id = :userId")
    Long countProjectsByUserId(@Param("userId") Long userId);
    
    /**
     * 查找参与人数最多的项目
     */
    @Query("SELECT p FROM Project p LEFT JOIN p.users u GROUP BY p ORDER BY COUNT(u) DESC")
    List<Project> findProjectsOrderByUserCount();
}
