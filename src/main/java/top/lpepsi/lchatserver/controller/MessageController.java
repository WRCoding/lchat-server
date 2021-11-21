package top.lpepsi.lchatserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.lpepsi.lchatserver.entity.Message;
import top.lpepsi.lchatserver.entity.Response;
import top.lpepsi.lchatserver.service.KafkaService;

import java.util.List;

/**
 * @author 林北
 * @description
 * @date 2021-11-16 20:07
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private KafkaService kafkaService;

    @GetMapping("/offline/message/{id}")
    public Response<List<Message>> offLineMessage(@PathVariable("id") String id){
        return kafkaService.getOffLine(id);
    }

}
