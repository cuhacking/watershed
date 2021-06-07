FROM gradle:jdk16 AS builder

COPY . /home/gradle/src
WORKDIR /home/gradle/src

RUN gradle org.gradle.jvmargs=--illegal-access=permit :server:shadowJar --stacktrace

FROM openjdk:16

COPY --from=builder /home/gradle/src/server/build/libs /usr/src/app
WORKDIR /usr/src/app
EXPOSE 8080
CMD ["java", "-jar", "server-all.jar"]

LABEL org.opencontainers.image.source https://github.com/cuhacking/watershed
