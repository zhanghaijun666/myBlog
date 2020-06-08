package com.blog.mybatis.service;

import com.blog.mybatis.entity.Label;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * Create label for db 服务类
 * </p>
 *
 * @author haijun.zhang
 * @since 2020-06-03
 */
public interface LabelService extends IService<Label> {
    public List<Label> selectAll();
}
