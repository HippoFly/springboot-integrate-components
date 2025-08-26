package com.example.jpa.config;

import com.example.jpa.entity.*;
import com.example.jpa.repository.*;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 数据初始化类，在应用启动时生成初始数据
 */
@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private UserRoleRepository userRoleRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // 检查是否已有数据，如果有则不初始化
        if (departmentRepository.count() > 0) {
            System.out.println("数据已存在，跳过初始化");
            return;
        }
        
        System.out.println("开始初始化数据...");
        
        // 创建5个部门
        List<Department> departments = createDepartments();
        
        // 创建20个用户，并分配到不同部门
        List<User> users = createUsers(departments);
        
        // 创建40个项目
        List<Project> projects = createProjects();
        
        // 为用户和项目建立多对多关系
        assignUsersToProjects(users, projects);
        
        // 创建角色
        List<Role> roles = createRoles();
        
        // 创建员工数据
        createEmployees(departments);
        
        // 为用户分配角色
        assignRolesToUsers(users, roles);
        
        System.out.println("数据初始化完成");
        System.out.println("部门数量: " + departmentRepository.count());
        System.out.println("用户数量: " + userRepository.count());
        System.out.println("项目数量: " + projectRepository.count());
        System.out.println("角色数量: " + roleRepository.count());
        System.out.println("员工数量: " + employeeRepository.count());
        System.out.println("用户角色关联数量: " + userRoleRepository.count());
    }
    
    /**
     * 创建5个部门
     * 
     * @return 部门列表
     */
    private List<Department> createDepartments() {
        List<Department> departments = new ArrayList<>();
        
        String[] deptNames = {"技术部", "产品部", "设计部", "市场部", "人事部"};
        String[] deptDescriptions = {
            "负责技术研发和系统维护",
            "负责产品规划和需求分析",
            "负责界面设计和用户体验",
            "负责市场推广和品牌宣传",
            "负责人力资源和员工关系"
        };
        
        for (int i = 0; i < 5; i++) {
            Department department = new Department();
            department.setName(deptNames[i]);
            department.setDescription(deptDescriptions[i]);
            department.setCreateTime(LocalDateTime.now());
            department.setUpdateTime(LocalDateTime.now());
            departments.add(departmentRepository.save(department));
        }
        
        System.out.println("创建了 " + departments.size() + " 个部门");
        return departments;
    }
    
    /**
     * 创建20个用户，并分配到不同部门
     * 
     * @param departments 部门列表
     * @return 用户列表
     */
    private List<User> createUsers(List<Department> departments) {
        List<User> users = new ArrayList<>();
        Random random = new Random();
        
        String[] firstNames = {"张", "李", "王", "刘", "陈", "杨", "赵", "黄", "周", "吴"};
        String[] lastNames = {"伟", "芳", "娜", "敏", "静", "丽", "强", "磊", "军", "洋"};
        
        String[] emails = {
            "@gmail.com", "@qq.com", "@163.com", "@sina.com", "@outlook.com"
        };
        
        for (int i = 0; i < 20; i++) {
            User user = new User();
            String firstName = firstNames[random.nextInt(firstNames.length)];
            String lastName = lastNames[random.nextInt(lastNames.length)];
            user.setUsername(firstName + lastName + (i + 1));
            
            String emailPrefix = firstName.toLowerCase() + lastName.toLowerCase() + (i + 1);
            user.setEmail(emailPrefix + emails[random.nextInt(emails.length)]);
            
            user.setAge(22 + random.nextInt(15)); // 年龄在22-36之间
            
            // 随机分配部门
            Department department = departments.get(random.nextInt(departments.size()));
            user.setDepartment(department);
            
            user.setCreateTime(LocalDateTime.now());
            user.setUpdateTime(LocalDateTime.now());
            users.add(userRepository.save(user));
        }
        
        System.out.println("创建了 " + users.size() + " 个用户");
        return users;
    }
    
    /**
     * 创建40个项目
     * 
     * @return 项目列表
     */
    private List<Project> createProjects() {
        List<Project> projects = new ArrayList<>();
        Random random = new Random();
        
        String[] projectPrefixes = {
            "系统", "平台", "应用", "项目", "工具", "服务", "管理", "开发"
        };
        
        String[] projectSuffixes = {
            "优化", "升级", "重构", "迁移", "集成", "分析", "监控", "安全"
        };
        
        String[] projectDescriptions = {
            "提高系统性能和稳定性",
            "改善用户体验和界面设计",
            "增强系统功能和扩展性",
            "优化数据处理和存储方案",
            "提升系统安全性和可靠性"
        };
        
        for (int i = 0; i < 40; i++) {
            Project project = new Project();
            String prefix = projectPrefixes[random.nextInt(projectPrefixes.length)];
            String suffix = projectSuffixes[random.nextInt(projectSuffixes.length)];
            project.setName(prefix + suffix + (i + 1));
            
            project.setDescription(projectDescriptions[random.nextInt(projectDescriptions.length)]);
            
            // 设置项目时间范围
            project.setStartDate(LocalDate.now().plusDays(random.nextInt(30)));
            project.setEndDate(project.getStartDate().plusMonths(3 + random.nextInt(9)));
            
            project.setCreateTime(LocalDateTime.now());
            project.setUpdateTime(LocalDateTime.now());
            projects.add(projectRepository.save(project));
        }
        
        System.out.println("创建了 " + projects.size() + " 个项目");
        return projects;
    }
    
    /**
     * 为用户和项目建立多对多关系
     * 
     * @param users 用户列表
     * @param projects 项目列表
     */
    private void assignUsersToProjects(List<User> users, List<Project> projects) {
        Random random = new Random();
        
        // 为每个项目分配3-8个用户
        for (Project project : projects) {
            int userCount = 3 + random.nextInt(6); // 3-8个用户
            List<User> projectUsers = new ArrayList<>();
            
            // 确保不会重复添加用户
            List<User> availableUsers = new ArrayList<>(users);
            
            for (int i = 0; i < userCount && !availableUsers.isEmpty(); i++) {
                int index = random.nextInt(availableUsers.size());
                User user = availableUsers.get(index);
                projectUsers.add(user);
                availableUsers.remove(index);
            }
            
            project.setUsers(projectUsers);
            projectRepository.save(project);
        }
        
        System.out.println("已完成用户和项目的关联分配");
    }
    
    /**
     * 创建角色
     */
    private List<Role> createRoles() {
        List<Role> roles = new ArrayList<>();
        
        String[] roleNames = {"管理员", "开发者", "测试员", "产品经理", "设计师"};
        String[] roleDescriptions = {
            "系统管理员，拥有所有权限",
            "软件开发人员，负责编码实现",
            "质量保证人员，负责测试验证",
            "产品规划人员，负责需求管理",
            "界面设计人员，负责用户体验"
        };
        
        for (int i = 0; i < roleNames.length; i++) {
            Role role = new Role();
            role.setRoleName(roleNames[i]);
            role.setDescription(roleDescriptions[i]);
            role.setStatus(Role.RoleStatus.ACTIVE);
            roles.add(roleRepository.save(role));
        }
        
        System.out.println("创建了 " + roles.size() + " 个角色");
        return roles;
    }
    
    /**
     * 创建员工（全职和兼职）
     */
    private List<Employee> createEmployees(List<Department> departments) {
        List<Employee> employees = new ArrayList<>();
        Random random = new Random();
        
        // 创建10个全职员工
        for (int i = 0; i < 10; i++) {
            FullTimeEmployee employee = new FullTimeEmployee();
            employee.setEmployeeCode("FT" + String.format("%03d", i + 1));
            employee.setName("全职员工" + (i + 1));
            employee.setEmail("fulltime" + (i + 1) + "@company.com");
            employee.setBaseSalary(new BigDecimal(8000 + random.nextInt(7000)));
            employee.setDepartment(departments.get(random.nextInt(departments.size())));
            employee.setAnnualLeaveDays(15 + random.nextInt(6));
            employee.setPerformanceBonus(new BigDecimal(random.nextInt(5000)));
            employee.setSocialSecurityBase(new BigDecimal(5000 + random.nextInt(3000)));
            employees.add(employeeRepository.save(employee));
        }
        
        // 创建5个兼职员工
        for (int i = 0; i < 5; i++) {
            PartTimeEmployee employee = new PartTimeEmployee();
            employee.setEmployeeCode("PT" + String.format("%03d", i + 1));
            employee.setName("兼职员工" + (i + 1));
            employee.setEmail("parttime" + (i + 1) + "@company.com");
            employee.setBaseSalary(new BigDecimal(3000 + random.nextInt(2000)));
            employee.setDepartment(departments.get(random.nextInt(departments.size())));
            employee.setHourlyRate(new BigDecimal(50 + random.nextInt(50)));
            employee.setHoursPerWeek(20 + random.nextInt(20));
            employee.setContractEndDate(java.time.LocalDate.now().plusMonths(6 + random.nextInt(12)));
            employees.add(employeeRepository.save(employee));
        }
        
        System.out.println("创建了 " + employees.size() + " 个员工");
        return employees;
    }
    
    /**
     * 为用户分配角色
     */
    private void assignRolesToUsers(List<User> users, List<Role> roles) {
        Random random = new Random();
        
        for (User user : users) {
            // 每个用户随机分配1-3个角色
            int roleCount = 1 + random.nextInt(3);
            List<Role> availableRoles = new ArrayList<>(roles);
            
            for (int i = 0; i < roleCount && !availableRoles.isEmpty(); i++) {
                Role role = availableRoles.get(random.nextInt(availableRoles.size()));
                availableRoles.remove(role);
                
                UserRole userRole = new UserRole();
                UserRoleId userRoleId = new UserRoleId();
                userRoleId.setUserId(user.getId());
                userRoleId.setRoleId(role.getId());
                userRole.setId(userRoleId);
                userRole.setUser(user);
                userRole.setRole(role);
                userRole.setAssignedDate(LocalDateTime.now());
                userRole.setExpireDate(LocalDateTime.now().plusYears(1));
                userRole.setIsActive(true);
                
                userRoleRepository.save(userRole);
            }
        }
        
        System.out.println("已完成用户角色分配");
    }
}