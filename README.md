
### mall 商城系统，包含小程序端和后台CRM管理控制台
### 项目说明
-------------
1. mall商城是以SpringBoot+Shiro+Mybatis plus为核心开发的精简商城后台基础系统。
2. 包含用户管理,角色管理,部门管理,权限管理,菜单管理等系统业务模块，及商城管理系统模块。
3. FreeMarker模板,页面拆分,封装公共部分,第三方Mybatis-plus作为ORM框架。
4. 整合redis、Encache缓存切换, 集成阿里云oss文件管理。
5. 使用AdminLTE作为前端UI框架, 集成诸多前端相关插件完善简而美的功能设计。
6. 使用AdminLTE作为前端UI框架, 前端参考:https://gitee.com/liyucc/adminlte-tab


### 技术选型
-------------
AdminLTE、Spring MVC、Shiro、Mybatis、Mybatis-Plus、Mysql、Maven、knife4j

### 快速开始
-------------
1. 创建数据库mall,导入resource/db/mall.sql
2. 导入mall后台源码到Eclispe或者IDEA
3. 修改相关配置参数，mysql、redis等相关配置,修改workDir文件存储位置
4. 运行 MallApplication 启动类
5. http://localhost:8080,账号/密码:admin/admin123

### 实例截图
-------------
<table>
    <tr>
        <td><img src="https://images.gitee.com/uploads/images/2021/0907/141859_cfaa1254_947463.jpeg"/></td>
        <td><img src="https://images.gitee.com/uploads/images/2021/0907/141232_73910055_947463.jpeg"/></td>
    </tr>
    <tr>
        <td><img src="https://images.gitee.com/uploads/images/2021/0907/140504_5c25e8d0_947463.jpeg"/></td>
        <td><img src="https://images.gitee.com/uploads/images/2021/0907/140547_3b709ebc_947463.jpeg"/></td>
    </tr>
    <tr>
        <td><img src="https://images.gitee.com/uploads/images/2021/0907/140627_a10e46a3_947463.jpeg"/></td>
        <td><img src="https://images.gitee.com/uploads/images/2021/0907/140610_681f6b56_947463.jpeg"/></td>
    </tr>
	<tr>
        <td><img src="https://images.gitee.com/uploads/images/2021/0907/140600_bcda7638_947463.jpeg"/></td>
        <td><img src="https://images.gitee.com/uploads/images/2021/0907/141106_d2ec0ba2_947463.jpeg"/></td>
    </tr>	 
    <tr>
        <td><img src="https://images.gitee.com/uploads/images/2021/0907/141119_ddbbe6fb_947463.jpeg"/></td>
        <td><img src="https://images.gitee.com/uploads/images/2021/0907/142617_15342fdd_947463.jpeg"/></td>
    </tr>
</table>