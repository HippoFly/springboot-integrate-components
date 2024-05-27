package com.example.mybatis.entity;

import lombok.Data;

import java.util.List;

@Data
public class DeptEntity {

    private Long id;

    private String name;

    private String code;

    private List<UserEntity> userList;

}
