package top.lpepsi.lchatserver.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.lpepsi.lchatserver.entity.group.GroupMemberInfo;

import java.util.List;

/**
 * @author 林北
 * @description 群聊详细信息DTO
 * @date 2021-12-11 11:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupInfoDTO {

    /** 群组创建后生成的groupId */
    private String groupId;
    /** 群组名称 */
    private String groupName;
    /** 群组创建人 */
    private String groupCreator;
    /** 群主 */
    private String groupOwner;
    /** 群组成员*/
    private List<GroupMemberInfo> groupMembers;
}
