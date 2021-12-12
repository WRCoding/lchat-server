package top.lpepsi.lchatserver.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * @author 林北
 * @description 群组DTO
 * @date 2021-12-11 11:09
 */
@Data
public class GroupInfoDTO {

    /** 群组创建后生成的groupId */
    private String groupId;
    /** 群组名称 */
    private String groupName;
    /** 群组创建人 */
    private String groupCreator;
    /** 群组成员*/
    private List<String> groupMembers;
}
