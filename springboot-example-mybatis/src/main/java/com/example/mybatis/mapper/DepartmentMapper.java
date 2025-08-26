package com.example.mybatis.mapper;

import com.example.mybatis.entity.DeptEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 部门Mapper接口 - 展示一对多关联查询
 */
@Mapper
public interface DepartmentMapper {

    /**
     * 根据ID查询部门
     */
    DeptEntity selectById(@Param("id") Long id);

    /**
     * 查询部门及其用户列表 - 一对多关联
     */
    DeptEntity selectWithUsers(@Param("id") Long id);

    /**
     * 查询所有部门
     */
    List<DeptEntity> selectAll();

    /**
     * 插入部门
     */
    int insert(DeptEntity department);

    /**
     * 更新部门
     */
    int update(DeptEntity department);

    /**
     * 删除部门
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据名称查询部门
     */
    DeptEntity selectByName(@Param("name") String name);

    /**
     * 查询部门用户统计
     */
    List<Map<String, Object>> selectDepartmentUserStats();
}
