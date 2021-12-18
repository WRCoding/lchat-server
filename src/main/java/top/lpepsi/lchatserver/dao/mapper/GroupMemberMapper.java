package top.lpepsi.lchatserver.dao.mapper;/**
 * @author 林北
 * @description
 * @date 2021-12-14 20:08
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.lpepsi.lchatserver.entity.group.GroupMember;

import java.util.List;

/**
 *@author 林北
 *@description
 *@date 2021-12-14 20:08
 */
@Mapper
public interface GroupMemberMapper extends BaseMapper<GroupMember> {

    /**
     * Description: 根据lcid查找出该lcid有多少群聊
     * @param lcid
     * return: java.util.List<java.lang.String>
     * Author: 7link
     * Date: 2021-12-18
    */
    List<String> findGroupIdByLcid(String lcid);

}
