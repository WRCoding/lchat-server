package top.lpepsi.lchatserver.dao;/**
 * @author 林北
 * @description
 * @date 2021-08-08 10:26
 */

import org.springframework.data.jpa.repository.JpaRepository;
import top.lpepsi.lchatserver.entity.Friend;
import top.lpepsi.lchatserver.entity.UserInfo;

/**
 *@author 林北
 *@description
 *@date 2021-08-08 10:26
 */
public interface FriendRepository extends JpaRepository<Friend,String> {
}
