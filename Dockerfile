FROM gradle:jdk17-alpine AS build
LABEL authors="Timofey Makhankov"

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle --no-daemon bootJar

FROM openjdk:17-alpine
RUN mkdir "/app"
COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar
EXPOSE 8080
HEALTHCHECK --interval=1s --timeout=5s \
    CMD curl --fail http://localhost:8080/user/login || exit 1

CMD ["java", "-jar", "-Xmx4g", "/app/spring-boot-application.jar"]