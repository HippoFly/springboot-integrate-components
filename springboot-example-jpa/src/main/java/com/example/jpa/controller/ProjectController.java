package com.example.jpa.controller;

import com.example.jpa.dto.Mapper;
import com.example.jpa.dto.ProjectDTO;
import com.example.jpa.dto.UserDTO;
import com.example.jpa.entity.Project;
import com.example.jpa.entity.User;
import com.example.jpa.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 项目控制器类，演示JPA多对多关系的REST API接口
 */
@RestController
@RequestMapping("/projects")
@Tag(name = "项目管理接口", description = "项目相关的CRUD操作接口")
public class ProjectController {
    
    @Autowired
    private ProjectService projectService;
    
    /**
     * 创建项目
     * 
     * @param project 项目对象
     * @return 创建后的项目对象
     */
    @PostMapping
    @Operation(summary = "创建项目", description = "创建一个新项目")
    public ProjectDTO createProject(@RequestBody Project project) {
        Project savedProject = projectService.saveProject(project);
        return Mapper.toProjectDTO(savedProject);
    }
    
    /**
     * 批量创建项目
     * 
     * @param projects 项目列表
     * @return 创建后的项目列表
     */
    @PostMapping("/batch")
    @Operation(summary = "批量创建项目", description = "批量创建多个项目")
    public List<ProjectDTO> createProjects(@RequestBody List<Project> projects) {
        List<Project> savedProjects = projectService.saveProjects(projects);
        return savedProjects.stream()
                .map(Mapper::toProjectDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据ID获取项目
     * 
     * @param id 项目ID
     * @return 项目对象
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取项目", description = "通过项目ID获取项目信息")
    public ProjectDTO getProjectById(@Parameter(description = "项目ID") @PathVariable Long id) {
        Project project = projectService.findProjectById(id);
        return Mapper.toProjectDTO(project);
    }
    
    /**
     * 获取所有项目
     * 
     * @return 项目列表
     */
    @GetMapping
    @Operation(summary = "获取所有项目", description = "获取系统中所有项目列表")
    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = projectService.findAllProjects();
        return projects.stream()
                .map(Mapper::toProjectDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 分页获取项目
     * 
     * @param page 页码（从0开始）
     * @param size 每页大小
     * @return 项目分页数据
     */
    @GetMapping("/page")
    @Operation(summary = "分页获取项目", description = "分页查询项目列表")
    public Page<ProjectDTO> getProjectsWithPagination(
            @Parameter(description = "页码（从0开始）") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        Page<Project> projects = projectService.findProjectsWithPagination(page, size);
        return projects.map(Mapper::toProjectDTO);
    }
    
    /**
     * 根据项目名称获取项目
     * 
     * @param name 项目名称
     * @return 项目对象
     */
    @GetMapping("/name/{name}")
    @Operation(summary = "根据项目名称获取项目", description = "通过项目名称获取项目信息")
    public ProjectDTO getProjectByName(@Parameter(description = "项目名称") @PathVariable String name) {
        Project project = projectService.findProjectByName(name);
        return Mapper.toProjectDTO(project);
    }
    
    /**
     * 根据项目名称模糊查询项目
     * 
     * @param name 项目名称
     * @return 项目列表
     */
    @GetMapping("/search")
    @Operation(summary = "根据项目名称模糊查询项目", description = "根据项目名称进行模糊搜索")
    public List<ProjectDTO> searchProjects(@Parameter(description = "项目名称") @RequestParam String name) {
        List<Project> projects = projectService.findProjectsByNameContaining(name);
        return projects.stream()
                .map(Mapper::toProjectDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 为项目分配用户
     * 
     * @param projectId 项目ID
     * @param userId 用户ID
     * @return 更新后的项目对象
     */
    @PutMapping("/{projectId}/users/{userId}")
    @Operation(summary = "为项目分配用户", description = "将指定用户分配到指定项目")
    public ProjectDTO assignUserToProject(
            @Parameter(description = "项目ID") @PathVariable Long projectId,
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        Project project = projectService.assignUserToProject(projectId, userId);
        return Mapper.toProjectDTO(project);
    }
    
    /**
     * 从项目中移除用户
     * 
     * @param projectId 项目ID
     * @param userId 用户ID
     * @return 更新后的项目对象
     */
    @DeleteMapping("/{projectId}/users/{userId}")
    @Operation(summary = "从项目中移除用户", description = "将指定用户从指定项目中移除")
    public ProjectDTO removeUserFromProject(
            @Parameter(description = "项目ID") @PathVariable Long projectId,
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        Project project = projectService.removeUserFromProject(projectId, userId);
        return Mapper.toProjectDTO(project);
    }
    
    /**
     * 获取指定项目下的所有用户
     * 
     * @param id 项目ID
     * @return 用户列表
     */
    @GetMapping("/{id}/users")
    @Operation(summary = "获取指定项目下的所有用户", description = "获取指定项目下的所有用户列表")
    public List<UserDTO> getUsersByProjectId(@Parameter(description = "项目ID") @PathVariable Long id) {
        List<User> users = projectService.findUsersByProjectId(id);
        return users.stream()
                .map(Mapper::toUserDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据ID删除项目
     * 
     * @param id 项目ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "根据ID删除项目", description = "通过项目ID删除项目")
    public String deleteProject(@Parameter(description = "项目ID") @PathVariable Long id) {
        projectService.deleteProjectById(id);
        return "Project deleted successfully";
    }
    
    /**
     * 删除所有项目
     * 
     * @return 删除结果
     */
    @DeleteMapping
    @Operation(summary = "删除所有项目", description = "删除系统中所有项目")
    public String deleteAllProjects() {
        projectService.deleteAllProjects();
        return "All projects deleted successfully";
    }
    
    /**
     * 获取项目总数
     * 
     * @return 项目总数
     */
    @GetMapping("/count")
    @Operation(summary = "获取项目总数", description = "获取系统中项目的总数量")
    public long getProjectCount() {
        return projectService.countProjects();
    }
}