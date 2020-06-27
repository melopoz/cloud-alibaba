Nacos 作为注册中心、配置中心、总线。

Nacos内置负载均衡。

###配置中心
> 8001 controller,8002启动类

命名空间 > 分组 > DataId

###nacos集群可以使用nginx集群做虚拟ip，使用mysql数据库做持久化

使用nacos的sql文件初始化数据库。

nacos/conf/application.properties
```
spring.datasorce.platfrom=mysql

db.num=1
db.url.0=jdbc:mysql://localhost:3306/nacos_config?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true
db.user=melopoz
db.password=mp567890
```

##集群配置
###cluster.conf
> 最少三个nacos，ip不能是127.0.0.1，linux可以使用```hostname -i``` 查看
```
ip:port1
ip:port2
ip:port3
```
###修改startup.sh
> 启动脚本默认三个参数 -m -f -s，需要添加一个 -p 来传入port。
####修改 类似java switch的地方
添加p
```
while getopts ":m:f:s:p:" opt
do
    ...
```
在s后 添加
```
p)
    PORT=$OPTARG;;
```
####在sh文件底部 nohup 行修改为
添加 -Dserver.port=${PORT}
```
nohup $JAVA -Dserver.port=${PORT} ${JAVA_OPT} nacos.nacos >> ${BASE_DIR}/logs/start.out 2>&1 &
```
###启动
启动时使用命令```startup.sh -p 6666``` ```startup.sh -p 7777``` ```startup.sh -p 8888```

## nginx配置
将8848端口的请求转发到6666、7777、8888。
```
upstream cluster{
    server 127.0.0.1：6666;
    server 127.0.0.1：7777;
    server 127.0.0.1：8888;
}
...
server{
    listen 8848;
    server_name localhost;
    location / {
        proxy_pass http://cluster;
    }
}
```
然后访问ip:8848/nacos,就会连接到nacos cluster