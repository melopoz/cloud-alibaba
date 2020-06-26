package top.melopoz.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import top.melopoz.entities.Payment;
import top.melopoz.vo.R;

/**
 * @Author: zhangce
 * @Descripition:
 * @Date: Created in 2020/6/27
 */
@RestController
@Slf4j
@RequestMapping("/consumer/payment/nacos")
public class OrderNacosController {
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${service-url.nacos-user-service")
    private String serviceUrl;

    @GetMapping("/get/{id}")
    public R<Payment> get(@PathVariable Long id) {
        //使用loadBalancerClient这个Bean实现负载均衡或者使用@LoadBalanced修饰的restTemplate进行负载均衡
        ServiceInstance serviceInstance = loadBalancerClient.choose("nacos-payment-provider");
        String hostPort = String.format("http://%s:%s", "localhost", serviceInstance.getPort());
        log.info("请求参数:{},调用{}的服务", id, hostPort);
        R r = restTemplate.getForObject(hostPort + "/payment/get/" + id, R.class);
        log.info("服务调用结果:{}", r.toString());
        return r;
    }
}
