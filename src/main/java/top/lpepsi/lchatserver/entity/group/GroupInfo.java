package top.lpepsi.lchatserver.entity.group;

import lombok.Data;
import top.lpepsi.lchatserver.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author 林北
 * @description 群组信息
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

    /** 群主 */
    @Column(name = "groupOwner")
    private String groupOwner;

    /** 群聊创建人,跟群主概念不同,群主可变,创建人不可变,最开始群主是创建人 */
    @Column(name = "groupCreator")
    private String groupCreator;

}
