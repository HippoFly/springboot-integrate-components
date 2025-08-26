package com.example.jpa.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 兼职员工实体类，继承自 Employee
 * 演示 JPA 继承映射
 */
@Entity
@Table(name = "part_time_employee")
@DiscriminatorValue("PART_TIME")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PartTimeEmployee extends Employee {
    
    /**
     * 小时工资
     */
    @Column(name = "hourly_rate", precision = 8, scale = 2)
    private BigDecimal hourlyRate;
    
    /**
     * 每周工作小时数
     */
    @Column(name = "hours_per_week")
    private Integer hoursPerWeek;
    
    /**
     * 合同结束日期
     */
    @Column(name = "contract_end_date")
    private java.time.LocalDate contractEndDate;
}
