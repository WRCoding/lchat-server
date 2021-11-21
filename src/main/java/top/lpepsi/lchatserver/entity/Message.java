package top.lpepsi.lchatserver.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author 林北
 * @description
 * @date 2021-09-20 21:25
 */
@Data
@Entity
@Table(name = "lchat_message")
public class Message extends BaseEntity{

    /** 消息序列号：时间戳 */
    @Column(name = "msgSeq")
    private long msgSeq;

    /** 发送者 */
    @Column(name = "sendId")
    private String from;

    /** 接收者 */
    @Column(name = "receiveId")
    private String to;

    /** 消息内容 */
    @Column(name = "message")
    private String message;

    /** 消息类型，如文字，文件，图片等类型 */
    @Column(name = "msgType")
    private String msgType;
}
