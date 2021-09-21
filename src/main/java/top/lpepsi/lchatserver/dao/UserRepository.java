package top.lpepsi.lchatserver.dao;/**
 * @author 林北
 * @description
 * @date 2021-08-08 10:26
 */

import org.springframework.data.jpa.repository.JpaRepository;
import top.lpepsi.lchatserver.entity.UserInfo;

/**
 *@author 林北
 *@description
 *@date 2021-08-08 10:26
 */
public interface UserRepository extends JpaRepository<UserInfo,String> {
    /**
     * 通过用户名查找用户信息
     * @param  username
     * @return top.lpepsi.chatterer.entity.UserInfo
     **/
    UserInfo findUserInfoByUserName(String username);
}
