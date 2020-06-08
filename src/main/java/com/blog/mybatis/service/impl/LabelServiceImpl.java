package com.blog.mybatis.service.impl;

import com.blog.mybatis.entity.Label;
import com.blog.mybatis.mapper.LabelMapper;
import com.blog.mybatis.service.LabelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * Create label for db 服务实现类
 * </p>
 *
 * @author haijun.zhang
 * @since 2020-06-03
 */
@Service
public class LabelServiceImpl extends ServiceImpl<LabelMapper, Label> implements LabelService {
    @Autowired
    LabelMapper labelMapper;


    @Override
    public List<Label> selectAll() {
        return labelMapper.selectAll();
    }
}
