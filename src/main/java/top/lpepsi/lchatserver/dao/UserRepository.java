package top.lpepsi.lchatserver.dao;/**
 * @author 林北
 * @description
 * @date 2021-08-08 10:26
 */

import org.springframework.data.jpa.repository.JpaRepository;
import top.lpepsi.lchatserver.entity.UserInfo;

import java.util.List;

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

    /**
     * 根据用户名查找
     * @author 林北
     * @date 2021-09-22 20:33
     **/
    List<UserInfo> findUserInfoByUserNameContaining(String key);

    /**
     * Description: 通过lcid查找用户
     * @param lcid
     * @return top.lpepsi.lchatserver.entity.UserInfo
     * Author: 7link
     * Date: 2021-12-05
    */
    UserInfo findUserInfoByLcid(String lcid);

}
