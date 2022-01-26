package top.lpepsi.lchatserver.service;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import top.lpepsi.lchatserver.dao.mapper.MessageMapper;
import top.lpepsi.lchatserver.dao.mapper.UserGroupMessageMapper;
import top.lpepsi.lchatserver.entity.Message;
import top.lpepsi.lchatserver.entity.Response;
import top.lpepsi.lchatserver.entity.group.UserGroupMessage;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    @Resource
    private UserGroupMessageMapper userGroupMessageMapper;

    @KafkaListener(topics = {"lchat"})
    public void receive(ConsumerRecord<String, String> record) {
        log.info("receive: topic:{}, partition:{}, value:{},offset:{}", record.topic(), record.partition(), record.value(),
                record.offset());
        Message message = JSONUtil.toBean(record.value(), Message.class);
        if (message != null) {
            log.info("消费Kafka: {}", message);
            messageMapper.insert(message);
        }
    }

    @KafkaListener(topics = {"lchat-group"})
    public void receiveGroup(ConsumerRecord<String, String> record) {
        log.info("receive: topic:{}, partition:{}, value:{},offset:{}", record.topic(), record.partition(), record.value(),
                record.offset());
        UserGroupMessage userGroupMessage = JSONUtil.toBean(record.value(), UserGroupMessage.class);
        if (userGroupMessage != null) {
            log.info("消费Kafka: {}", userGroupMessage);
            userGroupMessageMapper.insert(userGroupMessage);
        }
    }

    //事务
    public Response<List<Message>> getOffLine(String id) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receive_id", id);
        final List<Message> messageList = messageMapper.getOffLineMessage(id);
        List<Message> groupMessage = getOffLineGroupMessage(id);
        if (messageList.size() > 0) {
            messageList.addAll(groupMessage);
            messageMapper.delete(queryWrapper);
        }
        return messageList.size() > 0 ? Response.success(messageList) : Response.success(groupMessage);
    }

    public List<Message> getOffLineGroupMessage(String lcid) {
        QueryWrapper<UserGroupMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("lcid", lcid);
        List<UserGroupMessage> userGroupMessageList = userGroupMessageMapper.selectList(queryWrapper);
        if (userGroupMessageList.size() > 0) {
            userGroupMessageMapper.delete(queryWrapper);
        }
        List<String> groupIds = userGroupMessageList.stream().map(UserGroupMessage::getGroupId).collect(Collectors.toList());
        List<Long> msgSeqs = userGroupMessageList.stream().map(UserGroupMessage::getMsgSeq).collect(Collectors.toList());
        if (groupIds.size() > 0) {
            List<Message> groupMsgByGroupId = messageMapper.getGroupMsgByGroupId(groupIds);
            List<Message> groupMessage = groupMsgByGroupId.stream().filter(msg -> msgSeqs.contains(msg.getMsgSeq())).collect(Collectors.toList());
            return groupMessage;
        }
        return Collections.emptyList();
    }
}
