package top.lpepsi.lchatserver.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;



/**
 * @author 林北
 * @description 实体基类
 * @date 2021-08-08 10:36
 */

@Data
public class BaseEntity {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @TableField(fill = FieldFill.INSERT)
    private Long created;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updated;
}
