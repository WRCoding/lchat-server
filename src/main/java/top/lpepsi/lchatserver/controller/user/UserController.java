package top.lpepsi.lchatserver.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.lpepsi.lchatserver.entity.Friend;
import top.lpepsi.lchatserver.entity.Response;
import top.lpepsi.lchatserver.entity.UserInfo;
import top.lpepsi.lchatserver.service.user.UserService;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * @author 林北
 * @description
 * @date 2021-08-07 17:16
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;


    @PostMapping("/save")
    public Object save(@RequestBody UserInfo userInfo){
        log.info(userInfo.toString());
        return userService.save(userInfo);
    }

    @PostMapping("/login")
    public Response<UserInfo> login(@RequestBody UserInfo userInfo){
        System.out.println(userInfo);
        return userService.login(userInfo);
    }

    @PostMapping("/uploadFile")
    public Response<UserInfo> uploadFile(MultipartFile file,String userId,boolean flag){
        return userService.uploadFile(file,userId,flag);
    }

    @PostMapping("/addfriend")
    public Response<String> addFriend(@RequestBody Friend friend){
        return userService.addFriend(friend);
    }

    @GetMapping("/search")
    public Response<List<UserInfo>> searchUser(String key){
        return userService.searchUser(key);
    }
}
