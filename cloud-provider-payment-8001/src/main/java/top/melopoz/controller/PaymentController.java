package top.melopoz.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.melopoz.commons.R;
import top.melopoz.entities.Payment;
import top.melopoz.service.PaymentService;

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

    @PostMapping("/create")
    public R create(Payment payment) {
        boolean save = paymentService.save(payment);
        log.info("create payment:{},result:{}", payment.toString(), save);
        return save ? new R(200, "创建成功") : new R(204, "创建失败");
    }

    @GetMapping("/get/{id}")
    public R get(@PathVariable Integer id) {
        Payment payment = paymentService.getById(id);
        log.info("get...id:{},result:{}", id, payment.toString());
        if (payment != null) {
            return new R(200, "查询成功", payment);
        }
        return new R(204, "查询不到对应数据，id:{}", id);
    }
}
