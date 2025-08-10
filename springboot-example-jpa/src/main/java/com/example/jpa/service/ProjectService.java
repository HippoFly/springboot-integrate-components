package com.example.jpa.service;

import com.example.jpa.entity.Project;
import com.example.jpa.entity.User;
import com.example.jpa.repository.ProjectRepository;
import com.example.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 项目服务类，演示JPA多对多关系操作
 */
@Service
public class ProjectService {
    
    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * 保存项目
     * 
     * @param project 项目对象
     * @return 保存后的项目对象
     */
    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }
    
    /**
     * 批量保存项目
     * 
     * @param projects 项目列表
     * @return 保存后的项目列表
     */
    public List<Project> saveProjects(List<Project> projects) {
        return projectRepository.saveAll(projects);
    }
    
    /**
     * 根据ID查找项目
     * 
     * @param id 项目ID
     * @return 项目对象
     */
    public Project findProjectById(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        return project.orElse(null);
    }
    
    /**
     * 查找所有项目
     * 
     * @return 项目列表
     */
    public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }
    
    /**
     * 分页查找项目
     * 
     * @param page 页码（从0开始）
     * @param size 每页大小
     * @return 项目分页数据
     */
    public Page<Project> findProjectsWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
        return projectRepository.findAll(pageable);
    }
    
    /**
     * 根据项目名称查找项目
     * 
     * @param name 项目名称
     * @return 项目对象
     */
    public Project findProjectByName(String name) {
        return projectRepository.findByName(name);
    }
    
    /**
     * 根据项目名称模糊查询项目
     * 
     * @param name 项目名称
     * @return 项目列表
     */
    public List<Project> findProjectsByNameContaining(String name) {
        return projectRepository.findByNameContaining(name);
    }
    
    /**
     * 为项目分配用户
     * 
     * @param projectId 项目ID
     * @param userId 用户ID
     * @return 更新后的项目对象
     */
    public Project assignUserToProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        
        if (project != null && user != null) {
            project.getUsers().add(user);
            return projectRepository.save(project);
        }
        return null;
    }
    
    /**
     * 从项目中移除用户
     * 
     * @param projectId 项目ID
     * @param userId 用户ID
     * @return 更新后的项目对象
     */
    public Project removeUserFromProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        
        if (project != null && user != null) {
            project.getUsers().remove(user);
            return projectRepository.save(project);
        }
        return null;
    }
    
    /**
     * 获取指定项目下的所有用户
     * 
     * @param projectId 项目ID
     * @return 用户列表
     */
    public List<User> findUsersByProjectId(Long projectId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project != null) {
            return project.getUsers();
        }
        return null;
    }
    
    /**
     * 根据ID删除项目
     * 
     * @param id 项目ID
     */
    public void deleteProjectById(Long id) {
        projectRepository.deleteById(id);
    }
    
    /**
     * 删除所有项目
     */
    public void deleteAllProjects() {
        projectRepository.deleteAll();
    }
    
    /**
     * 统计项目总数
     * 
     * @return 项目总数
     */
    public long countProjects() {
        return projectRepository.count();
    }
}