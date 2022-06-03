[中文](codeGen_zh_CN.md)

# Code generator

## [Spring Boot][Spring Boot] + [MyBatis-Plus][MyBatis-Plus] Style
![](img/code_gen_preview_1.gif)

Note that the following dependencies and annotations are added manually.
- project dependency
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

## [Spring Boot][Spring Boot] + JPA Style

TODO

## [JHipster][JHipster] Style

TODO

## Generator refactoring enhancements to support more useful features.

TODO

[Spring Boot]: https://spring.io/projects/spring-boot

[MyBatis-Plus]: https://github.com/baomidou/mybatis-plus

[JHipster]: https://www.jhipster.tech/
