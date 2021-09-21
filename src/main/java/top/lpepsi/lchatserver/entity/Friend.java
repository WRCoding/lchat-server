package top.lpepsi.lchatserver.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author 林北
 * @description
 * @date 2021-09-11 16:04
 */
@Data
@Entity
@Table(name = "lchat_friend")
public class Friend extends BaseEntity{

    @Column(name = "userId")
    private String userId;

    @Column(name = "addUserId")
    private String addUserId;

}
