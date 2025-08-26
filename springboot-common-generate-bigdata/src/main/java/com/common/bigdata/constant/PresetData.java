package com.common.bigdata.constant;

import com.common.bigdata.entity.core.Department;
import com.common.bigdata.entity.core.Role;

import java.util.Arrays;
import java.util.List;

/**
 * 预制固定数据常量类
 * 包含固定的部门和角色数据
 */
public class PresetData {
    
    /**
     * 固定部门列表（8个）
     */
    public static final List<Department> DEPARTMENTS = Arrays.asList(
        new Department("技术研发部", "负责产品技术研发和架构设计"),
        new Department("产品设计部", "负责产品设计规划和用户体验"),
        new Department("市场运营部", "负责市场推广运营和品牌建设"),
        new Department("人力资源部", "负责人员招聘管理和企业文化"),
        new Department("财务管理部", "负责财务预算管理和成本控制"),
        new Department("质量保证部", "负责产品质量保证和测试管理"),
        new Department("客户服务部", "负责客户服务支持和售后管理"),
        new Department("行政管理部", "负责行政事务管理和后勤保障")
    );
    
    /**
     * 固定角色列表（6个）
     */
    public static final List<Role> ROLES = Arrays.asList(
        new Role("系统管理员", "系统最高权限管理员，负责系统维护"),
        new Role("部门经理", "部门管理和决策，负责部门运营"),
        new Role("项目经理", "项目计划和执行管理，负责项目推进"),
        new Role("高级工程师", "技术架构和指导，负责技术方案"),
        new Role("工程师", "具体开发和实现，负责功能开发"),
        new Role("专员", "专业领域执行，负责专项工作")
    );
    
    /**
     * 常用中文姓氏
     */
    public static final String[] SURNAMES = {
        "张", "王", "李", "赵", "陈", "刘", "杨", "黄", "周", "吴",
        "徐", "孙", "马", "朱", "胡", "郭", "何", "高", "林", "罗",
        "郑", "梁", "谢", "宋", "唐", "许", "韩", "冯", "邓", "曹"
    };
    
    /**
     * 常用中文名字
     */
    public static final String[] GIVEN_NAMES = {
        "伟", "芳", "娜", "敏", "静", "丽", "强", "磊", "军", "洋",
        "勇", "艳", "杰", "娟", "涛", "明", "超", "秀英", "霞", "平",
        "刚", "桂英", "建华", "文", "华", "金凤", "丽娟", "秀兰", "红", "玲"
    };
    
    /**
     * 项目名称前缀
     */
    public static final String[] PROJECT_PREFIXES = {
        "智能", "云端", "移动", "数字化", "AI", "大数据", "区块链", "物联网",
        "微服务", "分布式", "实时", "智慧", "新一代", "企业级", "高性能", "安全"
    };
    
    /**
     * 项目名称后缀
     */
    public static final String[] PROJECT_SUFFIXES = {
        "平台", "系统", "应用", "服务", "解决方案", "管理系统", "监控平台",
        "数据中心", "分析平台", "运营系统", "门户网站", "移动端", "小程序", "API网关"
    };
}
