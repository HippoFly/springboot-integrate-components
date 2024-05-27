package com.example.mybatis.mapper;

import com.example.mybatis.entity.DeptEntity;
import com.example.mybatis.entity.PositionEntity;
import com.example.mybatis.entity.UserEntity;
import com.example.mybatis.mapper.UserMapperXml;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserMapperTest {

    @Autowired
    private UserMapperXml userMapperXml;

    @Test
    public void testGetStudentById() {

        List<UserEntity> all = userMapperXml.findAll();
        all.forEach(System.out::println);
    }

    @Test
    public void testGetOne2One() {
        PositionEntity position = userMapperXml.getPosition(1L);
        System.out.println(position);
    }
    @Test
    public void testGetOne2Many() {
        DeptEntity dept = userMapperXml.getDept(1L);
        System.out.println(dept);
    }
}