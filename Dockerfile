# 경량화버젼
FROM amazoncorretto:11-alpine-jdk

# ARG로 프로필 변수 정의
ARG JAR_FILE=./build/libs/hel-gather-0.0.1-SNAPSHOT.jar
ARG PROFILES
ARG ENV

# JAR 파일 메인 디렉토리에 복사
COPY ${JAR_FILE} app.jar

# 시스템 진입점 정의
ENTRYPOINT ["java","-jar", "/app.jar", "-Dspring.profiles.active=${PROFILES}", "Dserver.env=${ENV}"]