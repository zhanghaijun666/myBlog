package com.blog.controller;

import com.blog.db.Organize;
import com.blog.db.User;
import com.blog.proto.BlogStore;
import com.blog.service.File.FileUrl;
import com.blog.service.File.StoreFileBlob;
import com.blog.service.File.StoreFileTree;
import com.blog.utils.PathUtils;
import com.blog.utils.RequestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.Request;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/file")
public class FileContorller {

    @PostMapping("/upload/**")
    @ResponseBody
    public String fileChunkUpload(@RequestParam("file") MultipartFile[] fileArray, HttpServletRequest request) {
        String originPath = request.getRequestURI().replace("/file/upload", "");
        if (fileArray.length == 0) {
            return "文件为空";
        }
        User user = RequestUtils.getUser(request);
        if (null == user) {
            return "user is null";
        }
        FileUrl fileUrl = new FileUrl(originPath, user.getUserId());
        for (MultipartFile file : fileArray) {
            BlogStore.FileStore.StoreTree.Builder storeTree = BlogStore.FileStore.StoreTree.newBuilder()
                    .setOwnerType(fileUrl.getOwnerType())
                    .setOwnerId(fileUrl.getOwnerId())
                    .setFileName(file.getOriginalFilename())
                    .setContentType(file.getContentType())
                    .setFileSize(file.getSize())
                    .setCreateTime(System.currentTimeMillis())
                    .setUpdateTime(System.currentTimeMillis())
                    .setCommitterId(fileUrl.getUserId());
            try (InputStream in = file.getInputStream()) {
                storeTree.setBlobHash(new StoreFileBlob().writeFile(IOUtils.toByteArray(in)));
                String treeHash = new StoreFileTree().writeFile(storeTree.build());
                User dbUser = (User) fileUrl.getOrganize();
                dbUser.setFileHash(treeHash);
                dbUser.saveIt();
            } catch (IOException e) {
                return "";
            }
        }
        return "ok";
    }
}
