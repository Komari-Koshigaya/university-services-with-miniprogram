## DOCKER启动本项目

#### 环境

virtualbox centos  sprintboot项目已打包成jar包



#### 安装docker

`yum -y install docker-io` //权限不够则需加上 sudo

`docker version`   //查看是否安装成功，出现版本号则成功

`vi /etc/docker/daemon.json`   //设置docker镜像，若已开启服务修改后重启服务方生效

```json
{
    "registry-mirrors": ["http://hub-mirror.c.163.com","https://pee6w651.mirror.aliyuncs.com","https://docker.mirrors.ustc.edu.cn"]
}

```

`service docker start`  //启动docker服务

#### MySql部署

```shell
# 项目中的配置文件application.yml  使用docker部署时此处为docker中mysql容器名称  另注意mysql密码需与容器里mysql的密码一致
url: jdbc:mysql://mysql-docker:3306/miniprogram?useUnicode=true&characterEncoding=utf-8&useSSL=true
后续运行主应用容器时加上
  --link mysql-docker:mysql-docker 


# linux服务器 进行一下步骤
sudo docker volume create mysql_data  #创建数据卷用来保存mysql的数据，可多个容器共享一个数据卷，当容器被删除时，数据卷不会被删除，mysql的数据依然存在
sudo docker run --name mysql-docker -v mysql_data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 -p 3306:3306 -d mysql:5.7   # 执行此命令时必须先执行上一条命令

# –name：给新创建的容器命名，此处命名为 mysql-docker
# -e：配置信息，此处配置mysql的root用户的登陆密码
# -p：端口映射，此处映射主机3306端口到容器pwc-mysql的3306端口
# -d：后台运行容器，成功启动容器后输出容器的完整ID.
# -v mysql_data:/var/lib/mysql 该参数中/var/lib/mysql是mysql容器数据存储位置
# 最后一个mysql指的是mysql镜像名字
sudo docker run --name mysql-docker -e MYSQL_ROOT_PASSWORD=123456 -p 3306:3306 -d mysql:5.7  # 创建并启动mysql容器  若事先未拉取对应mysql镜像，此处会先拉取镜像后创建容器


# ps  若使用virtual box配置的centos网络连接为 NAT模式，则需再端口转发那里加上 3306 否则Navicat连不上mysql  后面运行jar容器也是这样


# 一般来说下面的命令用不上
sudo docker exec -it mysql-docker /bin/bash   #进入MySQL容器,登陆mysql
mysql -u root -p  # 登陆容器里的mysql

# 设置外部网络访问mysql权限  外部访问权限不够才执行
ALTER user 'root'@'%' IDENTIFIED WITH mysql_native_password BY '123456';  --sql语句
FLUSH PRIVILEGES;    --sql语句
```



#### 将jar应用打包成镜像

##### 配置dockerfile文件

```dockerfile

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
```

##### 打包成镜像

```shell
sudo docker build -t miniserver:0.0.1 .    //将根据当前目录下的dockerfile打包成 名为 miniserver 的镜像
```

##### 查看镜像

```shell
sudo docker images
//////////////可以看到存在 yibai镜像 和dockerfile配置的基础镜像 java
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
miniserver               latest              6fb8bbedf9a0        2 hours ago         200 MB
docker.io/java      8-alpine            3fd9dd82815c        3 years ago         145 MB
```

##### 删除镜像

```shell
docker image rm imageid  //根据镜像id删除镜像，若该镜像在容器内运行则会报错，引用这个镜像的容器若在运行，则需先停止，再删除容器，最后删除镜像
docker image rm -f imageid //根据镜像id强制删除镜像
```

#### 启动镜像

```shell

#  -d 后台运行、
#  最后一个 miniserver 是引用的镜像的名字、
#  --name main 给容器取名为 main （取名参数前面是两短横线）、
#  -p 8089:8089 端口映射，注意是小写 p 
# 前一个 8089 是对外浏览器上访问的端口，后一个 8089 是容器内工程本身的端口，两者可不一样
 
sudo docker run -d -p 8089:8089 --name main miniserver  //后台运行 miniserver 镜像，并将运行的容器命名为 main

# --rm 运行完删除容器
# --link tz_mysql 第一个参数为mysql的docker容器名称，mysql-docker 第二个参数为别名，此处和yml文件中连接mysql的地址保持一致 
# tz-docker-demo:0.0.1 这个为我们刚刚build的镜像名称0.0.1是版本号，不写则是latest
sudo docker run --rm -d -p 8080:8888 --name main --link mysql-docker:mysql-docker miniserver:0.0.1


# ps  若使用virtual box配置的centos网络连接为 NAT模式，则需在端口转发那里加上 8080 否则浏览器访问不到
```

#### 查看当前服务器存在的容器

```shell
docker ps -a //查看容器，包括运行和停止状态的容器
```

##### 停止容器运行

```shell
docker stop containid  //根据容器id停止正在运行的容器
```

##### 删除容器

```shell
docker rm contianid //根据容器id删除
docker rm -f contianid //根据容器id强制删除容器，不管容器是否在运行
```

##### 重启容器

```shell
docker restart containid   //不管容器是否运行，直接重启容器
```

##### 查看容器运行输出的日志信息

```shell
docker logs -f 容器ID //-f：不间断持续输出
```



##### 























