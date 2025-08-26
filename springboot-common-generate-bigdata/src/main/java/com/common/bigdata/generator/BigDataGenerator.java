package com.common.bigdata.generator;

import com.common.bigdata.constant.PresetData;
import com.common.bigdata.entity.core.*;
import com.common.bigdata.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 大数据生成器核心类
 * 根据用户输入的数量生成完整的企业数据结构
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BigDataGenerator {
    
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final ProjectRepository projectRepository;
    private final RoleRepository roleRepository;
    
    private static final Random RANDOM = new Random();
    
    /**
     * 生成完整的企业数据（覆盖模式）
     * @param userCount 用户数量
     * @return 生成结果
     */
    @Transactional
    public GenerationResult generateData(int userCount) {
        return generateData(userCount, false);
    }
    
    /**
     * 生成企业数据
     * @param userCount 用户数量
     * @param incremental 是否增量生成（true=增量，false=覆盖）
     * @return 生成结果
     */
    @Transactional
    public GenerationResult generateData(int userCount, boolean incremental) {
        log.info("开始生成数据，用户数量: {}，模式: {}", userCount, incremental ? "增量" : "覆盖");
        
        long startTime = System.currentTimeMillis();
        
        // 根据模式决定是否清空数据
        if (!incremental) {
            clearExistingData();
        }
        
        // 1. 生成并保存部门数据
        List<Department> departments = generateAndSaveDepartments();
        
        // 2. 生成并保存角色数据
        List<Role> roles = generateAndSaveRoles();
        
        // 3. 生成并保存项目数据
        int projectCount = incremental ? userCount / 8 : userCount / 4; // 增量模式项目数量减半
        List<Project> projects = generateAndSaveProjects(projectCount, incremental);
        
        // 4. 生成并保存用户数据
        List<User> users = generateAndSaveUsers(userCount, departments, roles, projects, incremental);
        
        long endTime = System.currentTimeMillis();
        log.info("数据生成完成，耗时: {} ms", endTime - startTime);
        
        return GenerationResult.builder()
                .users(users)
                .departments(departments)
                .projects(projects)
                .roles(roles)
                .generationTime(endTime - startTime)
                .build();
    }
    
    /**
     * 清空现有数据
     */
    private void clearExistingData() {
        log.info("清空现有数据...");
        try {
            // 按照外键依赖关系的逆序删除
            userRepository.deleteAll();
            projectRepository.deleteAll();
            roleRepository.deleteAll();
            departmentRepository.deleteAll();
            log.info("数据清空完成");
        } catch (Exception e) {
            log.warn("清空数据时出现异常，继续执行: {}", e.getMessage());
        }
    }
    
    /**
     * 生成并保存部门数据
     */
    private List<Department> generateAndSaveDepartments() {
        log.info("生成部门数据...");
        List<Department> departments = new ArrayList<>();
        
        // 检查是否已存在部门数据，避免重复插入
        if (departmentRepository.count() > 0) {
            log.info("部门数据已存在，直接返回现有数据");
            return departmentRepository.findAll();
        }
        
        for (int i = 0; i < PresetData.DEPARTMENTS.size(); i++) {
            Department preset = PresetData.DEPARTMENTS.get(i);
            Department dept = new Department();
            dept.setName(preset.getName());
            dept.setDescription(preset.getDescription());
            setAuditFields(dept, "system");
            departments.add(dept);
        }
        
        // 批量保存到数据库
        departments = departmentRepository.saveAll(departments);
        log.info("保存了 {} 个部门", departments.size());
        return departments;
    }
    
    /**
     * 生成并保存角色数据
     */
    private List<Role> generateAndSaveRoles() {
        log.info("生成角色数据...");
        List<Role> roles = new ArrayList<>();
        
        // 检查是否已存在角色数据，避免重复插入
        if (roleRepository.count() > 0) {
            log.info("角色数据已存在，直接返回现有数据");
            return roleRepository.findAll();
        }
        
        for (int i = 0; i < PresetData.ROLES.size(); i++) {
            Role preset = PresetData.ROLES.get(i);
            Role role = new Role();
            role.setRoleName(preset.getRoleName());
            role.setDescription(preset.getDescription());
            role.setStatus(Role.RoleStatus.ACTIVE);
            setAuditFields(role, "system");
            roles.add(role);
        }
        
        // 批量保存到数据库
        roles = roleRepository.saveAll(roles);
        log.info("保存了 {} 个角色", roles.size());
        return roles;
    }
    
    /**
     * 生成并保存项目数据
     */
    private List<Project> generateAndSaveProjects(int count) {
        return generateAndSaveProjects(count, false);
    }
    
    /**
     * 生成并保存项目数据
     */
    private List<Project> generateAndSaveProjects(int count, boolean incremental) {
        log.info("生成项目数据，数量: {}，增量模式: {}", count, incremental);
        List<Project> projects = new ArrayList<>();
        
        // 获取起始编号
        int startIndex = 1;
        if (incremental) {
            // 增量模式：从现有项目数量+1开始
            long existingCount = projectRepository.count();
            startIndex = (int) existingCount + 1;
            log.info("增量模式：现有项目 {} 个，从编号 {} 开始生成", existingCount, startIndex);
        }
        
        for (int i = 0; i < count; i++) {
            Project project = new Project();
            int projectNumber = startIndex + i;
            project.setName("项目" + projectNumber);
            project.setDescription("这是第" + projectNumber + "个项目的描述");
            project.setStartDate(generateRandomDate(2023, 2024));
            project.setEndDate(generateRandomDate(2024, 2025));
            project.setStatus(Project.ProjectStatus.values()[RANDOM.nextInt(Project.ProjectStatus.values().length)]);
            setAuditFields(project, "system");
            projects.add(project);
        }
        
        // 批量保存到数据库
        projects = projectRepository.saveAll(projects);
        log.info("保存了 {} 个项目", projects.size());
        return projects;
    }
    
    /**
     * 生成并保存用户数据
     */
    private List<User> generateAndSaveUsers(int count, List<Department> departments, List<Role> roles, List<Project> projects) {
        return generateAndSaveUsers(count, departments, roles, projects, false);
    }
    
    /**
     * 生成并保存用户数据
     */
    private List<User> generateAndSaveUsers(int count, List<Department> departments, List<Role> roles, List<Project> projects, boolean incremental) {
        log.info("生成用户数据，数量: {}，增量模式: {}", count, incremental);
        List<User> users = new ArrayList<>();
        
        // 获取起始编号
        int startIndex = 1;
        if (incremental) {
            // 增量模式：从现有用户数量+1开始
            long existingCount = userRepository.count();
            startIndex = (int) existingCount + 1;
            log.info("增量模式：现有用户 {} 个，从编号 {} 开始生成", existingCount, startIndex);
        }
        
        for (int i = 0; i < count; i++) {
            User user = new User();
            int userNumber = startIndex + i;
            user.setUsername("user" + userNumber);
            user.setRealName(generateChineseName());
            user.setEmail("user" + userNumber + "@company.com");
            user.setAge(ThreadLocalRandom.current().nextInt(22, 60));
            user.setGender(RANDOM.nextBoolean() ? "男" : "女");
            user.setPhone(generatePhoneNumber());
            
            // 分配部门
            Department department = departments.get(i % departments.size());
            user.setDepartment(department);
            
            // 分配角色
            List<Role> userRoles = new ArrayList<>();
            int roleCount = RANDOM.nextInt(3) + 1; // 1-3个角色
            for (int j = 0; j < roleCount; j++) {
                Role role = roles.get(RANDOM.nextInt(roles.size()));
                if (!userRoles.contains(role)) {
                    userRoles.add(role);
                }
            }
            user.setRoles(userRoles);
            
            // 分配项目
            List<Project> userProjects = new ArrayList<>();
            int projectCount = RANDOM.nextInt(6); // 0-5个项目
            for (int j = 0; j < projectCount; j++) {
                Project project = projects.get(RANDOM.nextInt(projects.size()));
                if (!userProjects.contains(project)) {
                    userProjects.add(project);
                }
            }
            user.setProjects(userProjects);
            
            setAuditFields(user, "system");
            users.add(user);
        }
        
        // 批量保存到数据库
        users = userRepository.saveAll(users);
        log.info("保存了 {} 个用户", users.size());
        return users;
    }
    
    /**
     * 设置审计字段
     */
    private void setAuditFields(com.common.bigdata.entity.base.BaseAuditEntity entity, String operator) {
        LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        entity.setCreatedBy(operator);
        entity.setUpdatedBy(operator);
        entity.setVersion(1L);
    }
    
    /**
     * 生成随机日期
     */
    private LocalDate generateRandomDate(int startYear, int endYear) {
        int year = ThreadLocalRandom.current().nextInt(startYear, endYear + 1);
        int month = ThreadLocalRandom.current().nextInt(1, 13);
        int day = ThreadLocalRandom.current().nextInt(1, 29);
        return LocalDate.of(year, month, day);
    }
    
    /**
     * 生成中文姓名
     */
    private String generateChineseName() {
        String[] surnames = {"张", "李", "王", "刘", "陈", "杨", "赵", "黄", "周", "吴", "徐", "孙", "胡", "朱", "高", "林", "何", "郭", "马", "罗"};
        String[] names = {"伟", "芳", "娜", "敏", "静", "丽", "强", "磊", "军", "洋", "勇", "艳", "杰", "娟", "涛", "明", "超", "秀英", "华", "玲"};
        
        String surname = surnames[RANDOM.nextInt(surnames.length)];
        String name = names[RANDOM.nextInt(names.length)];
        return surname + name;
    }
    
    /**
     * 生成手机号
     */
    private String generatePhoneNumber() {
        String[] prefixes = {"130", "131", "132", "133", "134", "135", "136", "137", "138", "139", 
                            "150", "151", "152", "153", "155", "156", "157", "158", "159",
                            "180", "181", "182", "183", "184", "185", "186", "187", "188", "189"};
        String prefix = prefixes[RANDOM.nextInt(prefixes.length)];
        StringBuilder phone = new StringBuilder(prefix);
        for (int i = 0; i < 8; i++) {
            phone.append(RANDOM.nextInt(10));
        }
        return phone.toString();
    }
}
