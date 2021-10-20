package top.lpepsi.lchatserver.dao;/**
 * @author 林北
 * @description
 * @date 2021-08-08 10:26
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import top.lpepsi.lchatserver.entity.Friend;

import java.util.List;

/**
 *@author 林北
 *@description
 *@date 2021-08-08 10:26
 */
public interface FriendRepository extends JpaRepository<Friend,String> {

    @Query(value = "select userId\n" +
            "from (select friend_id as userId from lchat_friend where user_id = ?1 and " +
            "is_agree = 1) t1\n" +
            "union \n" +
            "(select user_id as userId from lchat_friend where friend_id = ?1 and " +
            "is_agree = 1)",nativeQuery = true)
    List<String> getFriendIdById(@Param("id")String id);
}
