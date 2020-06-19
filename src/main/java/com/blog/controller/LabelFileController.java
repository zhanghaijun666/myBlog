package com.blog.controller;


import com.blog.mybatis.entity.LabelFile;
import com.blog.mybatis.service.LabelFileService;
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
@RequestMapping("/label-file")
public class LabelFileController {


    @Autowired
    private LabelFileService service;

    @GetMapping
    public List<LabelFile> getAllLabel() {
        return service.list();
    }
}
