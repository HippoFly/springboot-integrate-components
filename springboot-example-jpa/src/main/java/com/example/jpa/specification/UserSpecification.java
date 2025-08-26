package com.example.jpa.specification;

import com.example.jpa.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户查询规范类
 * 演示 JPA Specification 动态查询
 */
public class UserSpecification {
    
    /**
     * 根据用户名模糊查询
     */
    public static Specification<User> usernameLike(String username) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(username)) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("username"), "%" + username + "%");
        };
    }
    
    /**
     * 根据邮箱模糊查询
     */
    public static Specification<User> emailLike(String email) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(email)) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("email"), "%" + email + "%");
        };
    }
    
    /**
     * 根据年龄范围查询
     */
    public static Specification<User> ageBetween(Integer minAge, Integer maxAge) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (minAge != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("age"), minAge));
            }
            if (maxAge != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("age"), maxAge));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
    
    /**
     * 根据部门ID查询
     */
    public static Specification<User> departmentIdEquals(Long departmentId) {
        return (root, query, criteriaBuilder) -> {
            if (departmentId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("department").get("id"), departmentId);
        };
    }
    
    /**
     * 根据创建时间范围查询
     */
    public static Specification<User> createTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (startTime != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createTime"), startTime));
            }
            if (endTime != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createTime"), endTime));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
    
    /**
     * 组合查询条件
     */
    public static Specification<User> buildSpecification(String username, String email, 
                                                       Integer minAge, Integer maxAge, 
                                                       Long departmentId,
                                                       LocalDateTime startTime, LocalDateTime endTime) {
        return Specification.where(usernameLike(username))
                .and(emailLike(email))
                .and(ageBetween(minAge, maxAge))
                .and(departmentIdEquals(departmentId))
                .and(createTimeBetween(startTime, endTime));
    }
}
