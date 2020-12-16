![build](https://github.com/equidis/spring-boot-users-service/workflows/build/badge.svg)
[![codecov](https://codecov.io/gh/equidis/spring-boot-users-service/branch/master/graph/badge.svg?token=OB1F66EA4A)](https://app.codecov.io/gh/equidis/spring-boot-users-service)
![release](https://img.shields.io/github/v/tag/equidis/spring-boot-users-service)
![license](https://img.shields.io/github/license/equidis/spring-boot-users-service)

# Users service

Sample [Spring Boot](https://spring.io/projects/spring-boot) microservice that mimics
[Micronaut GRPC users service](https://github.com/equidis/micronaut-grpc-users-service) to compare performance.

## Usage

### Running application

###### Using Gradle

`./gradlew bootRun`

###### Using Java archive

`./gradlew build`
`java -jar build/libs/sb-users-{APP_VERSION}.jar`

###### Using Docker

`./gradlew jibDockerBuild`
