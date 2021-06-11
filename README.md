
### mall 商城系统，包含小程序端和后台CRM管理控制台
### 项目说明
-------------
1. AdminLTE-admin是以SpringBoot+Shiro+Mybatis plus为核心开发的精简商城后台基础系统。
2. 包含用户管理,角色管理,部门管理,权限管理,菜单管理等系统业务模块，及商城管理系统模块。
3. 使用AdminLTE作为前端UI框架。
4. 第三方Mybatis-plus作为ORM框架。
5. redis、Encache权限缓存。
6. FreeMarker模板,页面拆分,封装公共部分。
7. Druid数据源,数据库监控。
8. 集成简单的oss文件管理模块

### 技术选型
-------------
AdminLTE、Spring MVC、Shiro、Mybatis、Mybatis-Plus、Mysql、Maven

### 快速开始
-------------
1. 创建数据库AdminLTE-admin,导入resource/sql/AdminLTE-admin.sql
2. cd ~/AdminLTE-admin
3. mvn clean package -Dmaven.test.skip=true
4. mvn jetty:run
5. http://localhost:8080,账号/密码:admin/123456

### 参考项目
-------------
1. https://github.com/almasaeed2010/AdminLTE
2. http://mp.baomidou.com/#/

### 实例截图
-------------
![](https://git.oschina.net/uploads/images/2017/0914/161552_cb781545_89451.png "1.png")

