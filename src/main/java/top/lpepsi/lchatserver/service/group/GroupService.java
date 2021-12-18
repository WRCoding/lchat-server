package top.lpepsi.lchatserver.service.group;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import top.lpepsi.lchatserver.core.util.LcIdGenerate;
import top.lpepsi.lchatserver.dao.mapper.GroupInfoMapper;
import top.lpepsi.lchatserver.dao.mapper.GroupMemberInfoMapper;
import top.lpepsi.lchatserver.dao.mapper.GroupMemberMapper;
import top.lpepsi.lchatserver.entity.ResponseCode;
import top.lpepsi.lchatserver.entity.dto.GroupDTO;
import top.lpepsi.lchatserver.entity.group.GroupMemberInfo;
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
    private GroupMemberInfoMapper groupMemberInfoMapper;

    @Resource
    private GroupInfoMapper groupInfoMapper;

    @Resource
    private GroupMemberMapper groupMemberMapper;

    public Response<GroupInfoDTO> createGroup(GroupDTO groupDTO) {
        if (groupDTO == null) {
            return Response.error(ResponseCode.PARAM_FAIL);
        }
        GroupInfo groupInfo = createGroupInfo(groupDTO);
        groupInfoMapper.insert(groupInfo);
        saveGroupMember(groupInfo.getGroupId(), groupDTO.getGroupMembers());
        //todo 直接返回群聊信息和群成员详细信息
        return Response.success(createGroupInfoDTO(groupInfo.getGroupId()));
    }

    private GroupInfoDTO createGroupInfoDTO(String groupId) {
        List<GroupMemberInfo> groupMemberInfos = groupMemberInfoMapper.findMembersByGroupId(groupId);
        QueryWrapper<GroupInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_id", groupId);
        GroupInfo groupInfo = groupInfoMapper.selectOne(queryWrapper);
        return new GroupInfoDTO(groupInfo.getGroupId(), groupInfo.getGroupName(),
                groupInfo.getGroupCreator(),groupInfo.getGroupOwner(),groupMemberInfos);
    }

    /**
     * Description: 保存群组id和群成员的映射关系
     *
     * @param groupId
     * @param groupMembers return: void
     *                     Author: 7link
     *                     Date: 2021-12-11
     */
    private void saveGroupMember(String groupId, List<String> groupMembers) {
        for (String groupMember : groupMembers) {
            groupMemberMapper.insert(new GroupMember(groupId, groupMember));
        }
    }

    /**
     * Description: 生成群组信息
     *
     * @param groupDTO return: top.lpepsi.lchatserver.entity.group.GroupInfo
     *                 Author: 7link
     *                 Date: 2021-12-11
     */
    private GroupInfo createGroupInfo(GroupDTO groupDTO) {
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setGroupId(lcIdGenerate.generateGroupId());
        groupInfo.setGroupName(groupDTO.getGroupName());
        groupInfo.setGroupOwner(groupDTO.getGroupMembers().get(0));
        groupInfo.setGroupCreator(groupDTO.getGroupMembers().get(0));
        return groupInfo;
    }

    /**
     * Description: 根据groupID返回群成员信息
     * @param groupId
     * return: top.lpepsi.lchatserver.entity.Response<java.util.List<top.lpepsi.lchatserver.entity.UserInfo>>
     * Author: 7link
     * Date: 2021-12-18
    */
    public Response<List<GroupMemberInfo>> members(String groupId) {
        List<GroupMemberInfo> groupMemberInfos = groupMemberInfoMapper.findMembersByGroupId(groupId);
        return Response.success(groupMemberInfos);
    }

    /**
     * Description: 返回该lcid的所有群聊信息
     * @param lcid
     * return: top.lpepsi.lchatserver.entity.Response<java.util.List<top.lpepsi.lchatserver.entity.dto.GroupInfoDTO>>
     * Author: 7link
     * Date: 2021-12-18
    */
    public Response<List<GroupInfoDTO>> groupInfoByLcid(String lcid) {
        List<String> groupIds = groupMemberMapper.findGroupIdByLcid(lcid);
        List<GroupInfoDTO> list = new ArrayList<>();
        for (String groupId : groupIds) {
            list.add(createGroupInfoDTO(groupId));
        }
        return Response.success(list);
    }
}
