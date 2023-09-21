FROM gradle:jdk17-alpine AS build
LABEL authors="Timofey Makhankov"

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle --no-daemon bootJar

FROM openjdk:17-alpine
RUN mkdir "/app"
COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar

RUN apk --no-cache add curl

EXPOSE 8080

HEALTHCHECK --interval=3s --timeout=5s \
    CMD curl -f localhost:8080/v3/api-docs || exit 1

CMD ["java", "-jar", "-Xmx4g", "/app/spring-boot-application.jar"]