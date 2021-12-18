package top.lpepsi.lchatserver.dao.mapper;/**
 * @author 林北
 * @description
 * @date 2021-12-18 09:16
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.lpepsi.lchatserver.entity.UserInfo;

/**
 *@author 林北
 *@description
 *@date 2021-12-18 09:16
 */
@Mapper
public interface UserMapper extends BaseMapper<UserInfo> {
}
