package com.example.jpa.service;

import com.example.jpa.entity.User;
import com.example.jpa.repository.UserRepository;
import com.example.jpa.specification.UserSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户查询服务类
 * 演示 JPA Criteria API 和 Specification 的使用
 */
@Service
public class UserQueryService {
    
    @Autowired
    private UserRepository userRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    /**
     * 使用 Specification 进行动态查询
     */
    public Page<User> findUsersBySpecification(String username, String email, 
                                             Integer minAge, Integer maxAge, 
                                             Long departmentId,
                                             LocalDateTime startTime, LocalDateTime endTime,
                                             Pageable pageable) {
        Specification<User> spec = UserSpecification.buildSpecification(
            username, email, minAge, maxAge, departmentId, startTime, endTime);
        return userRepository.findAll(spec, pageable);
    }
    
    /**
     * 使用 Criteria API 进行复杂查询
     * 查询指定条件的用户，并统计相关信息
     */
    public List<Object[]> findUsersWithStatistics(String username, Integer minAge, Long departmentId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<User> root = query.from(User.class);
        
        // 构建查询条件
        List<Predicate> predicates = new ArrayList<>();
        
        if (username != null && !username.trim().isEmpty()) {
            predicates.add(cb.like(root.get("username"), "%" + username + "%"));
        }
        
        if (minAge != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("age"), minAge));
        }
        
        if (departmentId != null) {
            predicates.add(cb.equal(root.get("department").get("id"), departmentId));
        }
        
        // 应用查询条件
        if (!predicates.isEmpty()) {
            query.where(cb.and(predicates.toArray(new Predicate[0])));
        }
        
        // 选择字段和聚合函数
        query.multiselect(
            root.get("username"),
            root.get("email"),
            root.get("age"),
            root.get("department").get("name"),
            cb.count(root)
        );
        
        // 分组
        query.groupBy(root.get("username"), root.get("email"), root.get("age"), root.get("department").get("name"));
        
        // 排序
        query.orderBy(cb.asc(root.get("username")));
        
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }
    
    /**
     * 使用 Criteria API 进行子查询
     * 查找参与项目数量最多的用户
     */
    public List<User> findUsersWithMostProjects() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);
        
        // 子查询：计算每个用户的项目数量
        Subquery<Long> subquery = query.subquery(Long.class);
        Root<User> subRoot = subquery.from(User.class);
        subquery.select(cb.max(cb.size(subRoot.get("projects")).as(Long.class)));
        
        // 主查询：找到项目数量等于最大值的用户
        query.select(root);
        query.where(cb.equal(cb.size(root.get("projects")), subquery));
        
        TypedQuery<User> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }
    
    /**
     * 使用 Criteria API 进行连接查询
     * 查找指定部门中年龄大于指定值的用户
     */
    public List<User> findUsersByDepartmentAndAge(String departmentName, Integer minAge) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);
        
        // 内连接部门表
        Join<Object, Object> departmentJoin = root.join("department", JoinType.INNER);
        
        // 构建查询条件
        List<Predicate> predicates = new ArrayList<>();
        
        if (departmentName != null && !departmentName.trim().isEmpty()) {
            predicates.add(cb.like(departmentJoin.get("name"), "%" + departmentName + "%"));
        }
        
        if (minAge != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("age"), minAge));
        }
        
        if (!predicates.isEmpty()) {
            query.where(cb.and(predicates.toArray(new Predicate[0])));
        }
        
        // 排序
        query.orderBy(cb.asc(root.get("age")), cb.asc(root.get("username")));
        
        TypedQuery<User> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }
    
    /**
     * 使用 Criteria API 进行聚合查询
     * 统计各部门的用户数量和平均年龄
     */
    public List<Object[]> getDepartmentStatistics() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<User> root = query.from(User.class);
        
        // 连接部门表
        Join<Object, Object> departmentJoin = root.join("department", JoinType.LEFT);
        
        // 选择字段和聚合函数
        query.multiselect(
            departmentJoin.get("name"),
            cb.count(root),
            cb.avg(root.get("age")),
            cb.min(root.get("age")),
            cb.max(root.get("age"))
        );
        
        // 分组
        query.groupBy(departmentJoin.get("name"));
        
        // 排序
        query.orderBy(cb.desc(cb.count(root)));
        
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }
}
