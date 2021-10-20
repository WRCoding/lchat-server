package top.lpepsi.lchatserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.lpepsi.lchatserver.entity.Friend;
import top.lpepsi.lchatserver.entity.Response;
import top.lpepsi.lchatserver.entity.UserInfo;
import top.lpepsi.lchatserver.service.FriendService;

import java.util.List;

/**
 * @author 林北
 * @description
 * @date 2021-09-25 15:14
 */
@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @PostMapping("/addfriend")
    public Response<String> addFriend(@RequestBody Friend friend){
        return friendService.saveFriend(friend);
    }

    @GetMapping("/getfriends")
    public Response<List<UserInfo>> getFriends(String id){
        return friendService.getFriends(id);
    }
}
