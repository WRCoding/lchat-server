package top.lpepsi.lchatserver.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import top.lpepsi.lchatserver.entity.Message;

import java.util.List;

/**
 * @author 林北
 * @description
 * @date 2021-11-16 20:04
 */
public interface MessageRepository extends JpaRepository<Message,String> {
    /**
     * @description 获取ID的离线记录
     * @author 林北
     * @date 2021-11-16 20:14
     **/
//    @Query(value = "select * from lchat_message where receive_id = ?",nativeQuery = true)
    List<Message> findAllByTo(String id);

    void deleteByTo(String id);
}
