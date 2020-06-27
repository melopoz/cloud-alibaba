package top.melopoz.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import top.melopoz.entities.Payment;
import top.melopoz.handler.CustomHandler;
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
@RefreshScope//cloud原生注解，配置的动态刷新，自动更新
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

    @Value("${info.coder}")
    private String configInfo;

    @GetMapping("/config/info")
    public R configInfo(){
        return new R(200, "OK", configInfo);
    }

    /*测试sentinel 热点 key*/
    @GetMapping("/testHotKey")
    /*在sentinel控制台新增热点规则时，资源名要使用这个value*/
    @SentinelResource(value = "testHotKey", blockHandler = "deal_testHotKey")
    public String testHotKey(@RequestParam(required = false, value = "p1")String p1,
                             @RequestParam( required = false, value = "p2")String p2){
        return "...testHotKey";
    }
    /*熔断之后的兜底方法，如果@SentinelResource中不指定handler，会返回一个Error页面*/
    public String deal_testHotKey(String p1, String p2, BlockException e){
        return "...deal_testHotKey,xxxxxxxxxx";
    }

    /*自定义统一限流处理处理*/
    @GetMapping("/testHandler")
    @SentinelResource(value = "testHandler", blockHandlerClass = CustomHandler.class, blockHandler = "handlerException")
    public String testHandler(){
        return "...testHandler 测试统一限流handler";
    }
}
