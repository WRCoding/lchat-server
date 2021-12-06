package top.lpepsi.lchatserver.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.lpepsi.lchatserver.dao.FriendRepository;
import top.lpepsi.lchatserver.entity.Friend;
import top.lpepsi.lchatserver.entity.Response;
import top.lpepsi.lchatserver.entity.UserInfo;
import top.lpepsi.lchatserver.service.user.UserService;

import java.util.List;

/**
 * @author 林北
 * @description
 * @date 2021-09-25 15:19
 */
@Service
@Slf4j
public class FriendService {

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserService userService;

    public Response<String> saveFriend(Friend friend){
        friendRepository.save(friend);
        return Response.success();
    }

    public Response<List<UserInfo>> getFriends(String id) {
        final List<String> lcids = friendRepository.getFriendIdById(id);
        return Response.success(userService.getFriendInfo(lcids));
    }
}
