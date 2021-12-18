package top.lpepsi.lchatserver.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * @author 林北
 * @description 用户信息实体
 * @date 2021-08-07 16:24
 */

@Data
@TableName("lchat_user_info")
public class UserInfo extends BaseEntity {

    private String lcid;

    private String userName;

    private String password;

    private String avatar;

    private String description;

    private String background;

    @TableField(exist = false)
    private Integer days;
}
