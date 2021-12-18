package top.lpepsi.lchatserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;



/**
 * @author 林北
 * @description
 * @date 2021-09-11 16:04
 */
@Data
@TableName("lchat_friend")
public class Friend extends BaseEntity{

    private String userId;

    private String friendId;

    private Integer isAgree = 1;
}
