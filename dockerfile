# 指定构成镜像的基础镜像源，如这个项目需要依赖jdk环境  镜像跟上版本号
FROM java:8

# VOLUME 指向了一个 /tmp的目录，由于 Spring Boot 使用内置的Tomcat容器，Tomcat 默认使用 /tmp作为工作目录。
# 这个命令的效果是：在宿主机的 /var/lib/docker目录下创建一个临时文件并把它链接到容器中的 /tmp目录
VOLUME /tmp 

# 复制本地文件到目标容器的系统文件中
ADD miniserver-0.0.1.jar /app.jar
 
#  执行 java -jar 命令 （CMD：在启动容器时才执行此行。RUN：构建镜像时就执行此行）
ENTRYPOINT ["java","-jar","/app.jar"]
 
#  设置对外端口为 8089,若运行时使用随即映射则使用该端口
EXPOSE 8089
