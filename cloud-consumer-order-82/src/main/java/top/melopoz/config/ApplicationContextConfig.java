package top.melopoz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: zhangce
 * @Descripition:
 * @Date: Created in 2020/6/26
 */
@Component
public class ApplicationContextConfig {
    @Bean
    /*可以使用LoadBalanced实现负载均衡，也可以在nacosController中使用loadBalancerClient这个Bean实现负载均衡*/
    //@LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
