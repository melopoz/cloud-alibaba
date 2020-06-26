package top.melopoz.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import top.melopoz.entities.Payment;
import top.melopoz.vo.R;

/**
 * @Author: zhangce
 * @Descripition:
 * @Date: Created in 2020/6/26
 */
@RestController
@RequestMapping("/consumer/payment")
@Slf4j
public class OrderController {
    private static final String PAYMENT_URL = "http://localhost:8001/payment";

    @Autowired
    RestTemplate restTemplate;

    @PostMapping("/create")
    public R<Payment> create(Payment payment) {
        log.info("请求参数:{}", payment.toString());
        R r = restTemplate.postForObject(PAYMENT_URL + "/create", payment, R.class);
        log.info("服务调用结果:{}", r.toString());
        return r;
    }

    @GetMapping("/get/{id}")
    public R<Payment> get(@PathVariable Long id) {
        log.info("请求参数:{}", id);
        R r = restTemplate.getForObject(PAYMENT_URL + "/get/" + id, R.class);
        log.info("服务调用结果:{}", r.toString());
        return r;
    }
}
