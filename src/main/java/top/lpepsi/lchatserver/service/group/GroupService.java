package top.lpepsi.lchatserver.service.group;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import top.lpepsi.lchatserver.controller.util.LcIdGenerate;
import top.lpepsi.lchatserver.dao.GroupInfoRepository;
import top.lpepsi.lchatserver.dao.GroupMemberRepository;
import top.lpepsi.lchatserver.dao.UserRepository;
import top.lpepsi.lchatserver.entity.ResponseCode;
import top.lpepsi.lchatserver.entity.UserInfo;
import top.lpepsi.lchatserver.entity.group.GroupInfo;
import top.lpepsi.lchatserver.entity.Response;
import top.lpepsi.lchatserver.entity.dto.GroupInfoDTO;
import top.lpepsi.lchatserver.entity.group.GroupMember;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 林北
 * @description
 * @date 2021-12-04 10:53
 */
@Service
public class GroupService {

    @Resource
    private LcIdGenerate lcIdGenerate;

    @Resource
    private GroupInfoRepository groupInfoRepository;

    @Resource
    private GroupMemberRepository groupMemberRepository;

    @Resource
    private UserRepository userRepository;

    public Response<GroupInfoDTO> createGroup(GroupInfoDTO groupInfoDTO) {
        if (groupInfoDTO == null){
            return Response.error(ResponseCode.PARAM_FAIL);
        }
        GroupInfo groupInfo = createGroupInfo(groupInfoDTO);
        final String groupId = groupInfoRepository.save(groupInfo).getGroupId();
        saveGroupMember(groupId,groupInfoDTO.getGroupMembers());
        groupInfoDTO.setGroupId(groupId);
        return Response.success(groupInfoDTO);
    }

    /**
     * Description: 保存群组id和群成员的映射关系
     * @param groupId
     * @param groupMembers
     * return: void
     * Author: 7link
     * Date: 2021-12-11
    */
    private void saveGroupMember(String groupId, List<String> groupMembers) {
        for (String groupMember : groupMembers) {
            groupMemberRepository.save(new GroupMember(groupId,groupMember));
        }
    }

    /**
     * Description: 生成群组信息
     * @param groupInfoDTO
     * return: top.lpepsi.lchatserver.entity.group.GroupInfo
     * Author: 7link
     * Date: 2021-12-11
    */
    private GroupInfo createGroupInfo(GroupInfoDTO groupInfoDTO) {
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setGroupId(lcIdGenerate.generateGroupId());
        groupInfo.setGroupName(groupInfoDTO.getGroupName());
        groupInfo.setGroupOwner(groupInfoDTO.getGroupCreator());
        groupInfo.setGroupCreator(groupInfoDTO.getGroupCreator());
        return groupInfo;
    }

    public Response<List<UserInfo>> members(String groupId) {
        List<GroupMember> groupMembers = groupMemberRepository.findGroupMemberByGroupId(groupId);
        List<UserInfo> list = new ArrayList<>();
        for (GroupMember groupMember : groupMembers) {
            list.add(userRepository.findUserInfoByLcid(groupMember.getGroupMemberId()));
        }
        return Response.success(list);
    }
}
