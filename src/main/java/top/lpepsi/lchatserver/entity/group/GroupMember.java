package top.lpepsi.lchatserver.entity.group;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.lpepsi.lchatserver.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author 林北
 * @description 群组和群成员映射表
 * @date 2021-12-11 11:28
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lchat_group_member")
public class GroupMember extends BaseEntity {

    /** 群组ID */
    @Column(name = "groupId")
    private String groupId;

    /** 群成员id */
    @Column(name = "groupMember")
    private String groupMemberId;
}
