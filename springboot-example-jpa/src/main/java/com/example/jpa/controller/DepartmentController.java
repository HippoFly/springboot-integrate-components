package com.example.jpa.controller;

import com.example.jpa.entity.Department;
import com.example.jpa.entity.User;
import com.example.jpa.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门控制器类，演示JPA一对多关系的REST API接口
 */
@RestController
@RequestMapping("/departments")
@Tag(name = "部门管理接口", description = "部门相关的CRUD操作接口")
public class DepartmentController {
    
    @Autowired
    private DepartmentService departmentService;
    
    /**
     * 创建部门
     * 
     * @param department 部门对象
     * @return 创建后的部门对象
     */
    @PostMapping
    @Operation(summary = "创建部门", description = "创建一个新部门")
    public Department createDepartment(@RequestBody Department department) {
        return departmentService.saveDepartment(department);
    }
    
    /**
     * 批量创建部门
     * 
     * @param departments 部门列表
     * @return 创建后的部门列表
     */
    @PostMapping("/batch")
    @Operation(summary = "批量创建部门", description = "批量创建多个部门")
    public List<Department> createDepartments(@RequestBody List<Department> departments) {
        return departmentService.saveDepartments(departments);
    }
    
    /**
     * 根据ID获取部门
     * 
     * @param id 部门ID
     * @return 部门对象
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取部门", description = "通过部门ID获取部门信息")
    public Department getDepartmentById(@Parameter(description = "部门ID") @PathVariable Long id) {
        return departmentService.findDepartmentById(id);
    }
    
    /**
     * 获取所有部门
     * 
     * @return 部门列表
     */
    @GetMapping
    @Operation(summary = "获取所有部门", description = "获取系统中所有部门列表")
    public List<Department> getAllDepartments() {
        return departmentService.findAllDepartments();
    }
    
    /**
     * 分页获取部门
     * 
     * @param page 页码（从0开始）
     * @param size 每页大小
     * @return 部门分页数据
     */
    @GetMapping("/page")
    @Operation(summary = "分页获取部门", description = "分页查询部门列表")
    public Page<Department> getDepartmentsWithPagination(
            @Parameter(description = "页码（从0开始）") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        return departmentService.findDepartmentsWithPagination(page, size);
    }
    
    /**
     * 根据部门名称获取部门
     * 
     * @param name 部门名称
     * @return 部门对象
     */
    @GetMapping("/name/{name}")
    @Operation(summary = "根据部门名称获取部门", description = "通过部门名称获取部门信息")
    public Department getDepartmentByName(@Parameter(description = "部门名称") @PathVariable String name) {
        return departmentService.findDepartmentByName(name);
    }
    
    /**
     * 根据部门名称模糊查询部门
     * 
     * @param name 部门名称
     * @return 部门列表
     */
    @GetMapping("/search")
    @Operation(summary = "根据部门名称模糊查询部门", description = "根据部门名称进行模糊搜索")
    public List<Department> searchDepartments(@Parameter(description = "部门名称") @RequestParam String name) {
        return departmentService.findDepartmentsByNameContaining(name);
    }
    
    /**
     * 为用户分配部门
     * 
     * @param userId 用户ID
     * @param departmentId 部门ID
     * @return 更新后的用户对象
     */
    @PutMapping("/{departmentId}/users/{userId}")
    @Operation(summary = "为用户分配部门", description = "将指定用户分配到指定部门")
    public User assignUserToDepartment(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "部门ID") @PathVariable Long departmentId) {
        return departmentService.assignUserToDepartment(userId, departmentId);
    }
    
    /**
     * 获取指定部门下的所有用户
     * 
     * @param id 部门ID
     * @return 用户列表
     */
    @GetMapping("/{id}/users")
    @Operation(summary = "获取指定部门下的所有用户", description = "获取指定部门下的所有用户列表")
    public List<User> getUsersByDepartmentId(@Parameter(description = "部门ID") @PathVariable Long id) {
        return departmentService.findUsersByDepartmentId(id);
    }
    
    /**
     * 根据ID删除部门
     * 
     * @param id 部门ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "根据ID删除部门", description = "通过部门ID删除部门")
    public String deleteDepartment(@Parameter(description = "部门ID") @PathVariable Long id) {
        departmentService.deleteDepartmentById(id);
        return "Department deleted successfully";
    }
    
    /**
     * 删除所有部门
     * 
     * @return 删除结果
     */
    @DeleteMapping
    @Operation(summary = "删除所有部门", description = "删除系统中所有部门")
    public String deleteAllDepartments() {
        departmentService.deleteAllDepartments();
        return "All departments deleted successfully";
    }
    
    /**
     * 获取部门总数
     * 
     * @return 部门总数
     */
    @GetMapping("/count")
    @Operation(summary = "获取部门总数", description = "获取系统中部门的总数量")
    public long getDepartmentCount() {
        return departmentService.countDepartments();
    }
}