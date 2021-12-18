package top.lpepsi.lchatserver.entity.group;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.lpepsi.lchatserver.entity.BaseEntity;



/**
 * @author 林北
 * @description 群组和群成员映射表
 * @date 2021-12-11 11:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("lchat_group_member")
public class GroupMember extends BaseEntity {

    /** 群组ID */
    private String groupId;

    /** 群成员id */
    private String groupMemberId;
}
