package com.blog.controller;

import com.blog.proto.BlogStore;
import com.blog.service.File.FileUrl;
import com.blog.service.File.StoreFactory;
import com.blog.service.File.StoreFileBlob;
import com.blog.service.File.StoreUtil;
import com.blog.utils.RequestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

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
        FileUrl fileUrl = new FileUrl(originPath, RequestUtils.getUserId(request));
        if (!fileUrl.isOwner()) {
            return "权限不足";
        }
        Set<String> chilName = StoreUtil.getChildNameList(fileUrl);
        for (MultipartFile file : fileArray) {
            if (chilName.contains(file.getOriginalFilename())) {
                continue;
            }
            BlogStore.StoreFile.StoreTree.Builder storeTree = BlogStore.StoreFile.StoreTree.newBuilder()
                    .setStoreType(BlogStore.StoreFile.StoreTypeEnum.Blob)
                    .setOwnerType(fileUrl.getOwnerType())
                    .setOwnerId(fileUrl.getOwnerId())
                    .setFileName(file.getOriginalFilename())
                    .setContentType(file.getContentType())
                    .setFileSize(file.getSize())
                    .setCreateTime(System.currentTimeMillis())
                    .setUpdateTime(System.currentTimeMillis())
                    .setCommitterId(fileUrl.getUserId());
            try (InputStream in = file.getInputStream()) {
                storeTree.addAllChildItem(StoreFileBlob.writeFile(IOUtils.toByteArray(in)));
                StoreFactory.addStore(new FileUrl(originPath, RequestUtils.getUserId(request)), storeTree.build());
            } catch (IOException e) {
                return "";
            }
        }
        return "ok";
    }
}
