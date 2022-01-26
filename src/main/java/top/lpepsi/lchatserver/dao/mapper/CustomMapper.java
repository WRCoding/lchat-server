package top.lpepsi.lchatserver.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * desc: 自定义Mapper
 * author:ink
 * date:2022-01-16 19:59
 */
public interface CustomMapper<T> extends BaseMapper<T> {

    default void insertBatch(List<T> list){
        for (T t : list) {
            insert(t);
        }
    }
}
