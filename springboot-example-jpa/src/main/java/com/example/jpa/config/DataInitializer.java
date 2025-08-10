package com.example.jpa.config;

import com.example.jpa.entity.Department;
import com.example.jpa.entity.Project;
import com.example.jpa.entity.User;
import com.example.jpa.repository.DepartmentRepository;
import com.example.jpa.repository.ProjectRepository;
import com.example.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
        
        System.out.println("数据初始化完成");
        System.out.println("部门数量: " + departmentRepository.count());
        System.out.println("用户数量: " + userRepository.count());
        System.out.println("项目数量: " + projectRepository.count());
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
            project.setStartDate(LocalDateTime.now().plusDays(random.nextInt(30)));
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
}