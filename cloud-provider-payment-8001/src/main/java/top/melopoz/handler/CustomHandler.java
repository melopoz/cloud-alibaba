package top.melopoz.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;

/**
 * @Author: zhangce
 * @Descripition:
 * @Date: Created in 2020/6/27
 */
public class CustomHandler {
    /*注意方法返回值要和接口的方法返回值一致，这里为了测试使用了String，本来应该使用 vo.R*/
    public static String handlerException(BlockException blockException){
        return "global 限流 handler";
    }
    public static String handlerException2(BlockException blockException){
        return "global 限流 handler2";
    }
}
