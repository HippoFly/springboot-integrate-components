## 目前整合组件

- mongodb  

### springboot-integrate-mongodb
安装部署好Mongo Db后 切换数据库，创建用户'admin'否则必定访问报错
```shell
# 切换数据库
use javadb

# 创建角色
db.createUser({
  user: "admin",
  pwd: "123456",
  roles: [{ role: "readWrite", db: "admin" }]
})


```
