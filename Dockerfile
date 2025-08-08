# 使用官方的 OpenJDK 镜像作为基础镜像
FROM openjdk:17-jdk-slim

# 设置工作目录
WORKDIR /app

# 复制 jar 包到容器中
COPY group-work-1.0-SNAPSHOT.jar app.jar

# 暴露应用运行的端口
EXPOSE 8080

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]