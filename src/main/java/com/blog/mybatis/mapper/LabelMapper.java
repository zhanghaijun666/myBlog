package com.blog.mybatis.mapper;

import com.blog.mybatis.entity.Label;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.mybatis.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Create label for db Mapper 接口
 * </p>
 *
 * @author haijun.zhang
 * @since 2020-06-03
 */
public interface LabelMapper extends BaseMapper<Label> {
    @Select("SELECT * FROM label")
    List<Label> selectAll();
}
