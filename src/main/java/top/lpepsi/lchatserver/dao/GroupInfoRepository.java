package top.lpepsi.lchatserver.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import top.lpepsi.lchatserver.entity.Message;
import top.lpepsi.lchatserver.entity.group.GroupInfo;

/**
 * @author 林北
 * @description
 * @date 2021-12-11 11:33
 */
public interface GroupInfoRepository extends JpaRepository<GroupInfo,String> {
}
