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

    List<String> findGroupIdByLcid(String lcid);

}
