## 简介
该项目为自己平时工作中所整理的代码仓库，包含一些常用的技术demo和一些工具类

另外做了一个简单的博客后台管理系统，和帮助java开发的小工具

## 技术栈 
##### springmvc

##### spring

#####  springdatajpa

对springdatajpa做了扩展，封装自定义的通用方法

##### hibernate 

对hibernate封装了通用的查询，分页等方法
对未加事务时session绑定到当前线程处理

##### 前端模板
前端模板使用的是hAmdin

##### 前端技术
jQuery + bootstrap + bootstrapTable + vue

### 代码小工具

##### 常用工具
1. 大小写转换
2. 提取代码中的sql
3. 用StringBuilder拼接sql
4. 提取实体类的set方法
5. json 转 java 代码
5. js格式化， html格式化，sql格式化

##### 实体类生成 （需要连接数据库，根据数据库表名）
1. 生成实体类（是否hibernate注释，是否字段注释，是否继承基础类）
2. 获取增删改查相关sql
3. 获取springjdbc Mapper映射类
4. 数据库表markdown格式的文档  

##### 拼接sql
hibernate 或者 springjdbc sql 拼接

##### sql参数赋值

sql 支持命名参数，占位符
参数，支持json，逗号分隔参数，mybatis 日志参数


