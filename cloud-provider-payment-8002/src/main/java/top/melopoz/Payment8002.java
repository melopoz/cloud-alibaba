package top.melopoz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author: zhangce
 * @Descripition:
 * @Date: Created in 2020/6/26
 */
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("top.melopoz.mapper")
@EnableDiscoveryClient
public class Payment8002 {

    public static void main(String[] args) {
        SpringApplication.run(Payment8002.class, args);
    }

}
