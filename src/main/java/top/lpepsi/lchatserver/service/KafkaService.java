package top.lpepsi.lchatserver.service;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import top.lpepsi.lchatserver.dao.MessageRepository;
import top.lpepsi.lchatserver.entity.Message;
import top.lpepsi.lchatserver.entity.Response;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author 林北
 * @description 处理kafka消息
 * @date 2021-11-15 23:07
 */
@Slf4j
@Service
public class KafkaService {

    @Autowired
    private MessageRepository messageRepository;

    @KafkaListener(topics = {"lchat"})
    public void receive(ConsumerRecord<String, String> record){
        log.info("receive: topic:{}, partition:{}, value:{},offset:{}",record.topic(),record.partition(),record.value(),
                record.offset());
        Message message = JSONUtil.toBean(record.value(), Message.class);
        if (message != null){
            log.info("消费Kafka: {}",message);
            messageRepository.save(message);
        }
    }

    @Transactional
    public Response<List<Message>> getOffLine(String id) {
        final List<Message> messages = messageRepository.findAllByTo(id);
        if (messages.size() > 0){
            messageRepository.deleteByTo(id);
        }
        return Response.success(messages);
    }
}
