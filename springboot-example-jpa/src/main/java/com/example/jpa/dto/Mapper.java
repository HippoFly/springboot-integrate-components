package com.example.jpa.dto;

import com.example.jpa.entity.Department;
import com.example.jpa.entity.Project;
import com.example.jpa.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class Mapper {
    
    public static UserDTO toUserDTO(User user) {
        if (user == null) {
            return null;
        }
        
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setAge(user.getAge());
        dto.setCreateTime(user.getCreateTime());
        dto.setUpdateTime(user.getUpdateTime());
        
        // 转换部门信息（不包含用户列表以避免循环）
        if (user.getDepartment() != null) {
            DepartmentDTO deptDTO = new DepartmentDTO();
            deptDTO.setId(user.getDepartment().getId());
            deptDTO.setName(user.getDepartment().getName());
            deptDTO.setDescription(user.getDepartment().getDescription());
            deptDTO.setCreateTime(user.getDepartment().getCreateTime());
            deptDTO.setUpdateTime(user.getDepartment().getUpdateTime());
            dto.setDepartment(deptDTO);
        }
        
        // 转换项目信息（不包含用户列表以避免循环）
        if (user.getProjects() != null) {
            List<ProjectDTO> projectDTOs = user.getProjects().stream()
                .map(project -> {
                    ProjectDTO projectDTO = new ProjectDTO();
                    projectDTO.setId(project.getId());
                    projectDTO.setName(project.getName());
                    projectDTO.setDescription(project.getDescription());
                    projectDTO.setStartDate(project.getStartDate());
                    projectDTO.setEndDate(project.getEndDate());
                    projectDTO.setCreateTime(project.getCreateTime());
                    projectDTO.setUpdateTime(project.getUpdateTime());
                    return projectDTO;
                })
                .collect(Collectors.toList());
            dto.setProjects(projectDTOs);
        }
        
        return dto;
    }
    
    public static DepartmentDTO toDepartmentDTO(Department department) {
        if (department == null) {
            return null;
        }
        
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setDescription(department.getDescription());
        dto.setCreateTime(department.getCreateTime());
        dto.setUpdateTime(department.getUpdateTime());
        
        // 转换用户信息（不包含部门信息以避免循环）
        if (department.getUsers() != null) {
            List<UserDTO> userDTOs = department.getUsers().stream()
                .map(user -> {
                    UserDTO userDTO = new UserDTO();
                    userDTO.setId(user.getId());
                    userDTO.setUsername(user.getUsername());
                    userDTO.setEmail(user.getEmail());
                    userDTO.setAge(user.getAge());
                    userDTO.setCreateTime(user.getCreateTime());
                    userDTO.setUpdateTime(user.getUpdateTime());
                    return userDTO;
                })
                .collect(Collectors.toList());
            dto.setUsers(userDTOs);
        }
        
        return dto;
    }
    
    public static ProjectDTO toProjectDTO(Project project) {
        if (project == null) {
            return null;
        }
        
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        dto.setCreateTime(project.getCreateTime());
        dto.setUpdateTime(project.getUpdateTime());
        
        // 转换用户信息（不包含项目信息以避免循环）
        if (project.getUsers() != null) {
            List<UserDTO> userDTOs = project.getUsers().stream()
                .map(user -> {
                    UserDTO userDTO = new UserDTO();
                    userDTO.setId(user.getId());
                    userDTO.setUsername(user.getUsername());
                    userDTO.setEmail(user.getEmail());
                    userDTO.setAge(user.getAge());
                    userDTO.setCreateTime(user.getCreateTime());
                    userDTO.setUpdateTime(user.getUpdateTime());
                    return userDTO;
                })
                .collect(Collectors.toList());
            dto.setUsers(userDTOs);
        }
        
        return dto;
    }
}