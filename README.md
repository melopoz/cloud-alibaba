#Nacos 
作为注册中心、配置中心、总线。

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

#nacos集群配置
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

# Sentinel
面向分布式服务架构的高可用流量控制组件

https://sentinelguard.io/zh-cn/docs
## 流控
QPS 、 线程数
QPS是处理请求之前就直接返回default
线程数 是处理请求，但是处理请求的线程数量是规定的，可能会慢

###流控模式
- 直接
> A服务的QPS高了，那就限制A服务
- 关联
> A服务的QPS高了，限制B服务，比如支付服务QPS太高了，可以限制下订单服务。

> 当前服务：B
> 关联资源：A
>
> 这样设置才是例子的效果
- 链路
> 规定一个服务作为入口，只有从个入口进入的请求才会被统计

###流控效果
- 快速失败 （默认）
> 直接返回失败
- 预热
> 不会直接从低水位变到高水位，会通过冷加载因子codeFactor（默认3），从阈值 / codeFactor，经过预热时长，才到达QPS阈值
>
> 提供服务的能力会越来越强，直至到达阈值
- 排队等待
> 匀速排队，请求匀速通过。
>
> 设置超时时间
>
> 主要用于间隔性突发的流量

## 服务降级
> 可以用jmeter测试
###降级策略
- RT
> 条件1：1s内有n个请求进来;
>
> 条件2：n个请求的平均响应时间(秒级，1s内) > 阈值
> 
> 触发熔断，待时间窗口结束，关闭服务降级，恢复服务
- 异常比例
> 条件1：资源每秒的请求量>=n;
> 
> 条件2：每秒异常总数占QPS的比例超过阈值.
> 
> 进入降级状态。待时间窗口结束，恢复服务。
- 异常数 (分钟级)
> 近一分钟的异常数超过阈值，就进行熔断。
> 
> 时间窗口timeWindow一定要 > 60s , 否则可能熔断结束之后还会触发熔断。

##热点规则（限流）
> 如果接口异常还是会返回Error，所以@SentinelResource的blockHandler只处理block，不处理runtimeException
>
>要想处理exception可以使用@SentinelResource注解的follBack属性

细粒度到一个请求的参数，如果这个参数的QPS到达阈值，就回返回Error页面，

如果该接口使用了@SentinelResource注解并配置了blockHandler属性
（该属性为String，对应兜底方法名），

就会调用该handler接口。

####参数例外项
如果希望当请求的一个参数为某个特殊值的时候，他的限流和平时不一样，

比如
> 第一个参数的值为13的时候，限流阈值不是10，而是200。

## 系统规则
是应用整体维度的，仅对入口流量生效。

对系统流量的整体控制。
> 入口流量指的是进入应用的流量

阈值类型：
- load
- RT
- 线程数
- 入口QPS
- CPU使用率

## 持久化规则
可以持久化到nacos、数据库
####添加依赖
```
    <dependency>
        <groupId>com.alibaba.scp</groupId>
        <artifactId>sentinel-datasource-nacos</artifactId>
    </dependency>
```
yml配置
```
spring:
  cloud:
    sentinel:
      datasource:
        ds1:
          nacos:
            server-addr: localhost:8848
            dataId: ${spring.application.name}
            groupId: DEFAULT_GROUP
            data-type: json
            rule-type: flow
```

## 如果使用feign
依赖
```
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </denpendency>
```
yml配置
```
feign:
  sentinel:
    enable: true
```
启动类加注解
```
@EnableFeignClients
```
service接口加注解
```
@FeignClient(value="服务名", fallback=xxx.class)
```
service接口方法加注解
```
@GetMapping(value="/payment/get/{id}")
```