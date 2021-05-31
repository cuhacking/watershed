FROM gradle:jdk15 AS builder

COPY . /home/gradle/src
WORKDIR /home/gradle/src

RUN gradle :server:shadowJar

FROM openjdk:15

COPY --from=builder /home/gradle/src/server/build/libs /usr/src/app
WORKDIR /usr/src/app
EXPOSE 8080
CMD ["java", "-jar", "server-all.jar"]

LABEL org.opencontainers.image.source https://github.com/cuhacking/watershed