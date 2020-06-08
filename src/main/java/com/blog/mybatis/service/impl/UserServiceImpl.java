package com.blog.mybatis.service.impl;

import com.blog.mybatis.entity.User;
import com.blog.mybatis.mapper.UserMapper;
import com.blog.mybatis.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * Create user for db 服务实现类
 * </p>
 *
 * @author haijun.zhang
 * @since 2020-06-03
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
