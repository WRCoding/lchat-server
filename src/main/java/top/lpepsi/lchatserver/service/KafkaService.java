package top.lpepsi.lchatserver.service;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import top.lpepsi.lchatserver.dao.mapper.MessageMapper;
import top.lpepsi.lchatserver.entity.Message;
import top.lpepsi.lchatserver.entity.Response;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 林北
 * @description 处理kafka消息
 * @date 2021-11-15 23:07
 */
@Slf4j
@Service
public class KafkaService {

    @Resource
    private MessageMapper messageMapper;

    @KafkaListener(topics = {"lchat"})
    public void receive(ConsumerRecord<String, String> record){
        log.info("receive: topic:{}, partition:{}, value:{},offset:{}",record.topic(),record.partition(),record.value(),
                record.offset());
        Message message = JSONUtil.toBean(record.value(), Message.class);
        if (message != null){
            log.info("消费Kafka: {}",message);
            messageMapper.insert(message);
        }
    }

    //事务
    public Response<List<Message>> getOffLine(String id) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receive_id",id);
        final List<Message> messageList = messageMapper.getOffLineMessage(id);
        if (messageList.size() > 0){
            messageMapper.delete(queryWrapper);
        }
        return Response.success(messageList);
    }
}
