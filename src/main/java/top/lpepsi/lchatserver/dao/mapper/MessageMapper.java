package top.lpepsi.lchatserver.dao.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.lpepsi.lchatserver.entity.Message;

import java.util.List;

/**
 *@author 林北
 *@description
 *@date 2021-12-18 10:08
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

    List<Message> getOffLineMessage(String receiveId);

    void insertGroupMessage(Message message);

    List<Message> getGroupMsgByGroupId(List<String> list);
}
