package top.lpepsi.lchatserver.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import top.lpepsi.lchatserver.entity.group.GroupInfo;
import top.lpepsi.lchatserver.entity.group.GroupMember;

import java.util.List;

/**
 * @author 林北
 * @description
 * @date 2021-12-11 11:33
 */
public interface GroupMemberRepository extends JpaRepository<GroupMember,String> {

    /**
     * Description: 根据groupId返回成员的lcid
     * @param groupId
     * @return java.util.List<top.lpepsi.lchatserver.entity.group.GroupMember>
     * Author: 7link
     * Date: 2021-12-12
    */
    List<GroupMember> findGroupMemberByGroupId(String groupId);

    /**
     * Description: 根据lcid返回成员所有的群组
     * @param lcid
     * return: java.util.List<top.lpepsi.lchatserver.entity.group.GroupMember>
     * Author: 7link
     * Date: 2021-12-14
    */
    List<GroupMember> findGroupMemberByGroupMemberId(String lcid);
}
