package top.lpepsi.lchatserver.controller;

import org.springframework.web.bind.annotation.*;
import top.lpepsi.lchatserver.entity.Response;
import top.lpepsi.lchatserver.entity.UserInfo;
import top.lpepsi.lchatserver.entity.dto.GroupDTO;
import top.lpepsi.lchatserver.entity.dto.GroupInfoDTO;
import top.lpepsi.lchatserver.entity.group.GroupMemberInfo;
import top.lpepsi.lchatserver.service.group.GroupService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 林北
 * @description 群聊接口
 * @date 2021-12-04 09:38
 */
@RestController
@RequestMapping("/group")
public class GroupController {

    @Resource
    private GroupService groupService;

    @PostMapping("/save")
    public Response<GroupInfoDTO> createGroup(@RequestBody GroupDTO groupDTO){
        return groupService.createGroup(groupDTO);
    }

    @GetMapping("/members/{groupId}")
    public Response<List<GroupMemberInfo>> members(@PathVariable("groupId") String groupId){
        return groupService.members(groupId);
    }

    @GetMapping("/groupInfo/{lcid}")
    public Response<List<GroupInfoDTO>> groupInfoByLcid(@PathVariable("lcid") String lcid){
        return groupService.groupInfoByLcid(lcid);
    }
}
