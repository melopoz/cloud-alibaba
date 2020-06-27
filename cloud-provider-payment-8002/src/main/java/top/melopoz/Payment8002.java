package top.melopoz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.concurrent.TimeUnit;

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

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Payment8002.class, args);
        while(true) {
            //When configurations are refreshed dynamically, they will be updated in the Enviroment, therefore here we retrieve configurations from Environment every other second.
            String coder = applicationContext.getEnvironment().getProperty("info.coder");
            System.err.println("info.coder:" + coder);
            TimeUnit.SECONDS.sleep(1);
        }
    }

}
