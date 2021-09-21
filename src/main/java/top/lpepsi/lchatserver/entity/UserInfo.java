package top.lpepsi.lchatserver.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;


/**
 * @author 林北
 * @description 用户信息实体
 * @date 2021-08-07 16:24
 */

@Data
@Entity
@Table(name = "lchat_user_info")
public class UserInfo extends BaseEntity {



    @Column(name = "username")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "description")
    private String description;

    @Column(name = "background")
    private String background;

    @Transient
    private Integer days;
}
