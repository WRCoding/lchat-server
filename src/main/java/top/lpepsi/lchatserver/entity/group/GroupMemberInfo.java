package top.lpepsi.lchatserver.entity.group;

import lombok.Data;


/**
 * @author 林北
 * @description
 * @date 2021-12-14 15:48
 */
@Data
public class GroupMemberInfo {

    /** 群组ID */
    private String groupId;

    private String lcid;

    private String userName;

    private String avatar;
}
