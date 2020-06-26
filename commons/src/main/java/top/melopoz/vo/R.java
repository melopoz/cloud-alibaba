package top.melopoz.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Author: zhangce
 * @Descripition:
 * @Date: Created in 2020/6/26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class R<T> {
    private Integer code;
    private String message;
    private T data;

    public R(Integer code, String message) {
        this(code, message, null);
    }
}
