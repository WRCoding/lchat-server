package top.lpepsi.lchatserver.dao.mapper;/**
 * @author 林北
 * @description
 * @date 2021-12-14 19:54
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.lpepsi.lchatserver.entity.dto.GroupMemberInfoDTO;

import java.util.List;

/**
 *@author 林北
 *@description
 *@date 2021-12-14 19:54
 */
@Mapper
public interface GroupMemberInfoMapper extends BaseMapper<GroupMemberInfoDTO> {

    /**
     * Description: 根据groupId获取群成员信息
     * @param groupId
     * return: java.util.List<top.lpepsi.lchatserver.entity.dto.GroupMemberInfoDTO>
     * Author: 7link
     * Date: 2021-12-14
    */
    List<GroupMemberInfoDTO> findMembersByGroupId(String groupId);
}
