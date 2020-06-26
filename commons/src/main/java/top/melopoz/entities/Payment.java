package top.melopoz.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author: zhangce
 * @Descripition:
 * @Date: Created in 2020/6/26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Payment implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String serial;
}
