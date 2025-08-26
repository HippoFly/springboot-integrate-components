package com.example.jpa.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 员工抽象类，演示 JPA 继承映射 - TABLE_PER_CLASS 策略
 * 每个具体子类都有自己的表
 */
@Entity
@Table(name = "employee")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "employee_type", discriminatorType = DiscriminatorType.STRING)
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public abstract class Employee extends BaseAuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    
    /**
     * 员工编号
     */
    @Column(name = "employee_code", nullable = false, unique = true)
    private String employeeCode;
    
    /**
     * 员工姓名
     */
    @Column(name = "name", nullable = false)
    private String name;
    
    /**
     * 邮箱
     */
    @Column(name = "email")
    private String email;
    
    /**
     * 基本工资
     */
    @Column(name = "base_salary", precision = 10, scale = 2)
    private BigDecimal baseSalary;
    
    /**
     * 所属部门
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
}
