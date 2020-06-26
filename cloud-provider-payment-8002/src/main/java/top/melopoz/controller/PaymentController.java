package top.melopoz.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import top.melopoz.entities.Payment;
import top.melopoz.service.PaymentService;
import top.melopoz.vo.R;

/**
 * @Author: zhangce
 * @Descripition:
 * @Date: Created in 2020/6/26
 */
@RestController
@RequestMapping("/payment")
@Slf4j
public class PaymentController {
    @Autowired
    PaymentService paymentService;

    @Value("${server.port}")
    private String port;

    /*@RequestBody注解保证consumer使用restTemplate传过来的请求的body可以被解析，否则拿不到请求的参数*/
    @PostMapping("/create")
    public R create(@RequestBody Payment payment) {
        log.info("请求参数:{}", payment.toString());
        boolean save = paymentService.save(payment);
        log.info("create payment:{},result:{}", payment.toString(), save);
        return save ? new R(200, "创建成功" + port) : new R(204, "创建失败" + port);
    }

    @GetMapping("/get/{id}")
    public R get(@PathVariable Integer id) {
        Payment payment = paymentService.getById(id);
        log.info("get...id:{},result:{}", id, payment.toString());
        if (payment != null) {
            return new R(200, "查询成功" + port, payment);
        }
        return new R(204, "查询不到对应数据，id:" + id + "。" + port);
    }
}
