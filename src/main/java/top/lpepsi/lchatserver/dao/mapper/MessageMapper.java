package top.lpepsi.lchatserver.dao.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.lpepsi.lchatserver.entity.Message;

/**
 *@author 林北
 *@description
 *@date 2021-12-18 10:08
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {
}
