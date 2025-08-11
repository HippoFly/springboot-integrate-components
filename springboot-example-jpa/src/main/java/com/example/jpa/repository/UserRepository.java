package com.example.jpa.repository;

import com.example.jpa.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户数据访问接口，演示常用的JPA查询方法
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    
    /**
     * 根据用户名查找用户（方法名查询）
     * 
     * @param username 用户名
     * @return 用户对象
     */
    User findByUsername(String username);
    
    /**
     * 根据邮箱查找用户（方法名查询）
     * 
     * @param email 邮箱
     * @return 用户对象
     */
    User findByEmail(String email);
    
    /**
     * 根据用户名和邮箱查找用户（方法名查询）
     * 
     * @param username 用户名
     * @param email 邮箱
     * @return 用户对象
     */
    User findByUsernameAndEmail(String username, String email);
    
    /**
     * 根据用户名模糊查询用户列表
     * 
     * @param username 用户名
     * @return 用户列表
     */
    List<User> findByUsernameContaining(String username);
    
    /**
     * 根据年龄范围查询用户
     * 
     * @param minAge 最小年龄
     * @param maxAge 最大年龄
     * @return 用户列表
     */
    List<User> findByAgeBetween(Integer minAge, Integer maxAge);
    
    /**
     * 根据年龄大于指定值查询用户
     * 
     * @param age 年龄
     * @return 用户列表
     */
    List<User> findByAgeGreaterThan(Integer age);
    
    /**
     * 根据部门ID查找用户列表
     * 
     * @param departmentId 部门ID
     * @return 用户列表
     */
    List<User> findByDepartmentId(Long departmentId);
    
    /**
     * 查询指定年龄的用户并按用户名排序
     * 
     * @param age 年龄
     * @return 用户列表
     */
    List<User> findByAgeOrderByUsername(Integer age);
    
    /**
     * 使用@Query注解自定义查询 - 根据用户名查询
     * 
     * @param username 用户名
     * @return 用户对象
     */
    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findUserByUsername(@Param("username") String username);
    
    /**
     * 使用@Query注解自定义查询 - 查询年龄大于指定值的用户
     * 
     * @param age 年龄
     * @return 用户列表
     */
    @Query("SELECT u FROM User u WHERE u.age > :age")
    List<User> findUsersByAgeGreaterThan(@Param("age") Integer age);
    
    /**
     * 使用@Query注解自定义查询 - 统计用户数量
     * 
     * @return 用户数量
     */
    @Query("SELECT COUNT(u) FROM User u")
    long countUsers();
    
    /**
     * 使用@Query注解自定义更新操作 - 根据用户名更新邮箱
     * 
     * @param email 新邮箱
     * @param username 用户名
     * @return 更新记录数
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.email = :email WHERE u.username = :username")
    int updateUserEmail(@Param("email") String email, @Param("username") String username);
    
    /**
     * 根据用户名更新用户邮箱
     * 
     * @param username 用户名
     * @param email 新邮箱
     * @return 更新记录数
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.email = :email WHERE u.username = :username")
    int updateEmailByUsername(@Param("username") String username, @Param("email") String email);
    
    /**
     * 使用原生SQL查询 - 根据邮箱查询用户
     * 
     * @param email 邮箱
     * @return 用户对象
     */
    @Query(value = "SELECT * FROM user WHERE email = :email", nativeQuery = true)
    User findUserByEmailNative(@Param("email") String email);
    
    /**
     * 分页查询用户
     * 
     * @param pageable 分页参数
     * @return 用户分页数据
     */
    Page<User> findAll(Pageable pageable);
}