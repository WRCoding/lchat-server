package top.lpepsi.lchatserver.core;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author 林北
 * @description
 * @date 2021-12-25 15:53
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("created",System.currentTimeMillis(),metaObject);
        this.setFieldValByName("updated",System.currentTimeMillis(),metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updated",System.currentTimeMillis(),metaObject);
    }
}
