package com.example.jpa.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 全职员工实体类，继承自 Employee
 * 演示 JPA 继承映射
 */
@Entity
@Table(name = "full_time_employee")
@DiscriminatorValue("FULL_TIME")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class FullTimeEmployee extends Employee {
    
    /**
     * 年假天数
     */
    @Column(name = "annual_leave_days")
    private Integer annualLeaveDays;
    
    /**
     * 绩效奖金
     */
    @Column(name = "performance_bonus", precision = 10, scale = 2)
    private BigDecimal performanceBonus;
    
    /**
     * 社保缴费基数
     */
    @Column(name = "social_security_base", precision = 10, scale = 2)
    private BigDecimal socialSecurityBase;
}
