package com.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/file")
public class FileContorller {

    @PostMapping("/upload")
    @ResponseBody
    public Map fileChunkUpload(@RequestParam("file") MultipartFile[] fileArray, HttpServletResponse response, HttpServletRequest request) {
        HashMap<String, String> map = new HashMap();
        if (fileArray.length == 0) {
            map.put("res", "文件为空");
            return map;
        }
        for (MultipartFile file : fileArray) {
            File targetFile = new File("D:/Download/file/" + file.getOriginalFilename());
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            try {
                file.transferTo(targetFile);
                map.put(file.getOriginalFilename(), "{" +
                        "ContentType:" + file.getContentType() + "," +
                        "Name:" + file.getOriginalFilename() + "," +
                        "Size:" + file.getSize() +
                        "}");
            } catch (IOException e) {
                e.printStackTrace();
                map.put(file.getOriginalFilename(), "上传失败");
            }
        }
        return map;
    }
}
