# blog-server
搭建一个个人的云存储，博客文章，技术文档，技术日志，知识库，一切都是为了提升自己，希望自己可以坚持到底。

### 注意事项	
- 本项目代码托管在[github](https://github.com/zhanghaijun666/blog-server)


### 用到的工具/技术栈
- [spring boot官方脚手架](https://start.spring.io/ "spring boot官方脚手架")
- [liquibase](http://www.liquibase.org/ "liquibase")
- [MyBatis](http://www.mybatis.org/mybatis-3/configuration.html "mybatis")
- [MyBatis Generator](http://www.mybatis.org/generator/configreference/xmlconfig.html "MyBatis Generator")
- [protobuf](https://github.com/protocolbuffers/protobuf/releases)
- [cordova](http://cordova.axuer.com/docs/zh-cn/latest/)
- [gitlab](https://gitlab.com/gitlab-org/gitlab-ce/tree/master)

### springboot启动命令
mvn spring-boot:run -Dspring-boot.run.profiles=test
mvn spring-boot:run -Dspring-boot.run.profiles=prod
MAVEN指定pom文件
mvn -f pom-test.xml spring-boot:run -Dspring.config.name=application-test
MAVEN指定端口
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8989

###  问题反馈
1. Email: zhanghaijun_java@163.com
