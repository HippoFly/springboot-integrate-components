package com.example.mybatis.mapper;

import com.example.mybatis.entity.UserEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserMapperAnnot {


    /**
     * 查询所有的信息
     * @return
     */
    @Select("SELECT * FROM user")
    @Results({
            @Result(property = "id",  column = "id"),
            @Result(property = "email", column = "email"),
            @Result(property = "name", column = "name"),
            @Result(property = "sex", column = "sex"),
            @Result(property = "tel", column = "tel"),
            @Result(property = "address", column = "address")
    })
    List<UserEntity> findAll();


    /**
     * 查询用户信息
     * @param id
     * @return
     */
    @Select("SELECT * FROM user WHERE id = #{id}")
    @Results({
            @Result(property = "id",  column = "id"),
            @Result(property = "email", column = "email"),
            @Result(property = "name", column = "name"),
            @Result(property = "sex", column = "sex"),
            @Result(property = "tel", column = "tel"),
            @Result(property = "address", column = "address")
    })
    UserEntity getInfoById(Long id);


    /**
     * 新增数据
     * @param user
     */
    @Insert("INSERT INTO user(id,email,name,sex,tel,address) VALUES(#{id},#{email},#{name},#{sex},#{tel},#{address})")
    void insert(UserEntity user);

    /**
     * 修改数据
     * @param user
     */
    @Update("UPDATE user SET name=#{name},address=#{address} WHERE id =#{id}")
    void update(UserEntity user);

    /**
     * 删除数据
     * @param id
     */
    @Delete("DELETE FROM user WHERE id =#{id}")
    void delete(Long id);

}
