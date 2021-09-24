package top.lpepsi.lchatserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.lpepsi.lchatserver.entity.Response;
import top.lpepsi.lchatserver.service.ChatServer;

/**
 * @author 林北
 * @description
 * @date 2021-09-24 23:35
 */
@RestController
public class ServerController {

    @Autowired
    private ChatServer chatServer;

    @GetMapping("/getClient")
    public Response getClient(){
        return chatServer.getClient();
    }
}
