# Xây dựng chức năng login, logout

```text
# Authentication service
.
├── Dockerfile
├── mvnw
├── mvnw.cmd
├── pom.xml
└── src
    ├── main
    │ ├── java
    │ │ └── vn
    │ │     └── tayjava
    │ │         ├── AuthenticationServiceApplication.java
    │ │         ├── common
    │ │         │ ├── Gender.java
    │ │         │ ├── Platform.java
    │ │         │ ├── TokenType.java
    │ │         │ ├── UserStatus.java
    │ │         │ └── UserType.java
    │ │         ├── config
    │ │         │ ├── AppConfig.java
    │ │         │ ├── OpenAPIConfig.java
    │ │         │ └── RedisConfig.java
    │ │         ├── controller
    │ │         │ ├── AuthenticationController.java
    │ │         │ ├── request
    │ │         │ │ └── LoginRequest.java
    │ │         │ └── response
    │ │         │     └── TokenResponse.java
    │ │         ├── exception
    │ │         │ ├── GlobalException.java
    │ │         │ └── InvalidDataException.java
    │ │         ├── model
    │ │         │ ├── BaseEntity.java
    │ │         │ ├── Group.java
    │ │         │ ├── GroupHasUser.java
    │ │         │ ├── Permission.java
    │ │         │ ├── RedisToken.java
    │ │         │ ├── Role.java
    │ │         │ ├── RoleHasPermission.java
    │ │         │ ├── User.java
    │ │         │ └── UserHasRole.java
    │ │         ├── repository
    │ │         │ ├── TokenRepository.java
    │ │         │ └── UserRepository.java
    │ │         └── service
    │ │             ├── AuthenticationService.java
    │ │             ├── JwtService.java
    │ │             ├── UserService.java
    │ │             └── impl
    │ │                 ├── AuthenticationServiceImp.java
    │ │                 ├── JwtServiceImpl.java
    │ │                 └── UserServiceImpl.java
    │ └── resources
    │     ├── application-dev.yml
    │     ├── application-prod.yml
    │     ├── application-test.yml
    │     ├── application.properties
    │     ├── application.yml
    │     └── banner.txt
    └── test
        └── java
            └── vn
                └── tayjava
                    └── AuthenticationServiceApplicationTests.java
```

1. Add dependency
2. Config datasource and JPA
3. Create model
4. Create repository
5. Create UserService
6. Create JwtService
7. Create AuthenticationService
8. Create AuthenticationController


