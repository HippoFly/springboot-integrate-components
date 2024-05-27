package com.example.mybatis.mapper;

import com.example.mybatis.entity.DeptEntity;
import com.example.mybatis.entity.PositionEntity;
import com.example.mybatis.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface UserMapperXml {


    /**
     * 查询所有的信息
     * @return
     */
    List<UserEntity> findAll();

    /**
     * 查询用户信息
     * @param id
     * @return
     */
    UserEntity getInfoById(Long id);

    /**
     * 新增数据
     * @param user
     */
    void insert(UserEntity user);

    /**
     * 修改数据
     * @param user
     */
    void update(UserEntity user);

    /**
     * 删除数据
     * @param id
     */
    void delete(Long id);

    /**
     * 一对一查询
     * 一个位置和一个User对应
     * @param pid
     * @return
     */
    PositionEntity getPosition(Long pid);

    /**
     * 一对多
     * 一个部门，多个User
     * @param id
     * @return
     */
    DeptEntity getDept(Long id);

}
