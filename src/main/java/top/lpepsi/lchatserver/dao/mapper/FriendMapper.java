package top.lpepsi.lchatserver.dao.mapper;/**
 * @author 林北
 * @description
 * @date 2021-12-18 09:29
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.lpepsi.lchatserver.entity.Friend;

import java.util.List;

/**
 *@author 林北
 *@description
 *@date 2021-12-18 09:29
 */
@Mapper
public interface FriendMapper extends BaseMapper<Friend> {

    List<String> findFriendsByLcid(String lcid);
}
