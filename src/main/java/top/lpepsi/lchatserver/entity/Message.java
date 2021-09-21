package top.lpepsi.lchatserver.entity;

import lombok.Data;

/**
 * @author 林北
 * @description
 * @date 2021-09-20 21:25
 */
@Data
public class Message {

    /** 消息序列号：时间戳 */
    private long msgSeq;

    /** 发送者 */
    private String from;

    /** 接收者 */
    private String to;

    /** 消息内容 */
    private String message;

    /** 消息类型，如文字，文件，图片等类型 */
    private String msgType;
}
