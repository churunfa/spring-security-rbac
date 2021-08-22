# spring-security-rbac
Spring security rbac增强组件，可视化权限管理，增加自定义权限匹配规则，针对传统rbac进行了大量性能优化。

# 安装运行
1.将项目导入idea，将spring-boot-security-rbac-starter-autoconfigure install到本地maven仓库

2.在自己项目中导入坐标
```
<dependency>
    <groupId>org.crf.security.rbac</groupId>
    <artifactId>spring-boot-security-rbac-starter-autoconfigure</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```
3.配置redis、mysql、导入spring-boot-starter-web

4.运行sql文件，创建rbac基础数据表

5.启动项目即可
