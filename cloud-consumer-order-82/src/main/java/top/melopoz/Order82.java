package top.melopoz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author: zhangce
 * @Descripition:
 * @Date: Created in 2020/6/26
 */
@SpringBootApplication
@EnableDiscoveryClient
public class Order82 {

    public static void main(String[] args) {
        SpringApplication.run(Order82.class, args);
    }

}
