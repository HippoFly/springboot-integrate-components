package com.example.mybatis.entity;

import lombok.Data;

/**
 * 位置
 * 一个位置只有一个User（一对一）
 *
 * @author FlyHippo
 * @version 1.0
 * @createDate 2024/5/27 14:37
 **/
@Data
public class PositionEntity {

    private Long id;

    private String name;

    private UserEntity user;

}
