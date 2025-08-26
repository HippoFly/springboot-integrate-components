package com.common.bigdata.controller;

import com.common.bigdata.generator.BigDataGenerator;
import com.common.bigdata.generator.GenerationResult;
import com.common.bigdata.repository.DepartmentRepository;
import com.common.bigdata.repository.ProjectRepository;
import com.common.bigdata.repository.RoleRepository;
import com.common.bigdata.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 数据生成器控制器
 * 提供Thymeleaf可视化界面
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class DataGeneratorController {
    
    private final BigDataGenerator bigDataGenerator;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final ProjectRepository projectRepository;
    private final RoleRepository roleRepository;
    
    /**
     * 首页 - 数据生成器界面
     */
    @GetMapping("/")
    public String index(Model model) {
        // 获取当前数据统计
        long userCount = userRepository.count();
        long departmentCount = departmentRepository.count();
        long projectCount = projectRepository.count();
        long roleCount = roleRepository.count();
        
        model.addAttribute("userCount", userCount);
        model.addAttribute("departmentCount", departmentCount);
        model.addAttribute("projectCount", projectCount);
        model.addAttribute("roleCount", roleCount);
        
        return "index";
    }
    
    /**
     * 生成数据
     */
    @PostMapping("/generate")
    public String generateData(@RequestParam("userCount") int userCount, 
                              @RequestParam(value = "generationMode", defaultValue = "overwrite") String generationMode,
                              Model model) {
        try {
            boolean incremental = "incremental".equals(generationMode);
            log.info("开始生成数据，用户数量: {}，模式: {}", userCount, incremental ? "增量" : "覆盖");
            
            long startTime = System.currentTimeMillis();
            GenerationResult result = bigDataGenerator.generateData(userCount, incremental);
            long endTime = System.currentTimeMillis();
            
            model.addAttribute("result", result);
            model.addAttribute("userCount", userCount);
            model.addAttribute("generationMode", generationMode);
            model.addAttribute("generationTime", endTime - startTime);
            
            log.info("数据生成完成，耗时: {} ms", endTime - startTime);
            return "result";
            
        } catch (Exception e) {
            log.error("数据生成失败", e);
            model.addAttribute("error", "数据生成失败: " + e.getMessage());
            model.addAttribute("userCount", userCount);
            model.addAttribute("generationMode", generationMode);
            return "index";
        }
    }
    
    /**
     * 预览数据结构
     */
    @GetMapping("/preview")
    @ResponseBody
    public String previewStructure(@RequestParam("userCount") int userCount) {
        int projectCount = userCount / 4;
        return String.format("将生成：用户 %d 个，项目 %d 个，部门 8 个（固定），角色 6 个（固定）", 
                userCount, projectCount);
    }
}
