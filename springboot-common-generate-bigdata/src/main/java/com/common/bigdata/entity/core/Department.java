package com.common.bigdata.entity.core;

import com.common.bigdata.entity.base.BaseAuditEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 部门实体类，与User形成一对多关系
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "departments")
public class Department extends BaseAuditEntity {
    
    /**
     * 主键字段
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 部门名称，不能为空且唯一
     */
    @Column(nullable = false, unique = true, length = 100)
    private String name;
    
    /**
     * 部门描述
     */
    @Column(length = 500)
    private String description;
    
    /**
     * 与User的一对多关系
     */
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();
    
    /**
     * 构造函数，用于预制数据
     */
    public Department(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
