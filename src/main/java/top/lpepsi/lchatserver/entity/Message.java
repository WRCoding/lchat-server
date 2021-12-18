package top.lpepsi.lchatserver.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;



/**
 * @author 林北
 * @description
 * @date 2021-09-20 21:25
 */
@Data
@TableName("lchat_message")
public class Message extends BaseEntity{

    /** 消息序列号：时间戳 */
    private long msgSeq;


    /** 发送者 */
    @TableField(value = "send_id")
    private String from;

    /** 接收者 */
    @TableField(value = "receive_id")
    private String to;

    /** 消息内容 */
    private String message;

    /** 消息内容类型 文字，文件，图片等类型 */
    private String msgType;

    /** 消息类型 群聊,单聊,默认为单聊*/
    private String type = Type.SINGLE.getType();
}
