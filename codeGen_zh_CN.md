[English](codeGen.md)

# 代码生成器

## [Spring Boot][Spring Boot] + [MyBatis-Plus][MyBatis-Plus] 风格
![](img/code_gen_preview_1.gif)

注意，手动添加以下依赖和注解
- 项目依赖
```xml
 <dependencies>
    <!--Spring Boot -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
     </dependency>
    <!-- MyBatis-Plugin-->
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
        <version>3.5.1</version>
    </dependency>
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus</artifactId>
        <version>3.5.1</version>
    </dependency>
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-extension</artifactId>
        <version>3.3.2</version>
    </dependency>
    <!--lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.8</version>
    </dependency>
</dependencies>
```
- MapperScan
```java
@SpringBootApplication
//Add MapperScan
@MapperScan("com.example.demo.mapper")
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
```

## [Spring Boot][Spring Boot] + JPA 风格

TODO,预计4月25日完成

## [JHipster][JHipster] 风格

TODO,预计5月25日完成

[Spring Boot]: https://spring.io/projects/spring-boot

[MyBatis-Plus]: https://github.com/baomidou/mybatis-plus

[JHipster]: https://www.jhipster.tech/
