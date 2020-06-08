package com.blog.mybatis.mapper;

import com.blog.mybatis.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Create user for db Mapper 接口
 * </p>
 *
 * @author haijun.zhang
 * @since 2020-06-03
 */
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT * FROM user")
    List<User> selectAll();
}
