package top.lpepsi.lchatserver.entity.group;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * desc: 群聊信息用户映射
 * author:ink
 * date:2022-01-16 10:22
 */
@Data
@TableName("lchat_user_group_message")
public class UserGroupMessage {

    /** 用户ID */
    private String lcid;
    /** 群聊ID */
    private String groupId;
    /** 消息ID */
    private Long msgSeq;

    public static UserGroupMessage instance(String lcid, String groupId, Long msgSeq){
        return new UserGroupMessage(lcid, groupId, msgSeq);
    }

    private UserGroupMessage(String lcid, String groupId, Long msgSeq) {
        this.lcid = lcid;
        this.groupId = groupId;
        this.msgSeq = msgSeq;
    }
}
