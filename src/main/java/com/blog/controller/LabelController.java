package com.blog.controller;


import com.blog.mybatis.entity.Label;
import com.blog.mybatis.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author haijun.zhang
 * @since 2020-06-03
 */
@RestController
@RequestMapping("/label")
public class LabelController {
    @Autowired
    private LabelService labelService;

    @GetMapping
    public List<Label> getAllLabel() {
        return labelService.selectAll();
    }
}
