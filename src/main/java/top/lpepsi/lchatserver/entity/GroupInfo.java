package top.lpepsi.lchatserver.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author 林北
 * @description 群聊实体
 * @date 2021-12-04 11:02
 */
@Data
@Entity
@Table(name = "lchat_group_info")
public class GroupInfo extends BaseEntity {

    @Column(name = "groupId")
    private String groupId;

    @Column(name = "groupName")
    private String groupName;

    @Column(name = "groupMember")
    private String groupMember;

    /** 群主 */
    @Column(name = "groupOwner")
    private String groupOwner;

    /** 群聊创建人 */
    @Column(name = "groupCreator")
    private String groupCreator;

}
