# 简介

这个项目是方便快速测试SpringBoot和各种组件集成的类DEMO项目，
通过不同module来区分不同整合组件

## IDE环境

Intellij Idea 2021
JDK 1.8
MAVEN 3.6.3

# 整合组件Module

### Spring-base-functions

这是测试Spring自身提供的一些基本功能|APIs，例如LRU缓存实现和ThreadLocal使用示例

### springboot-example-jpa

专门用于演示Spring Data JPA与Spring Boot的集成示例

### springboot-example-mybatis

整合 ORM 框架 Mybatis/Plus，包含实体类、Mapper接口以及XML配置文件，提供了注解和XML两种方式的Mapper实现

### springboot-integrate-caffeine

测试Caffeine缓存与Spring Boot的集成，包含完整的Controller、Service、Entity结构以及缓存配置和使用示例

###  springboot-integrate-mongodb

测试MongoDB与Spring Boot的集成，包含了MongoDB的各种操作示例，如集合操作、文档操作、索引、事务等，并集成了Swagger配置用于API文档展示

- MongoDB [MongoDB安装](https://www.runoob.com/mongodb/mongodb-window-install.html)

目的是测试`mongodb`整合后，有关的一些操作测试