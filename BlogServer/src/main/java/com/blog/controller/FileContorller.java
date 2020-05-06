package com.blog.controller;

import com.blog.proto.BlogStore;
import com.blog.service.File.FileUrl;
import com.blog.service.File.StoreFactory;
import com.blog.service.File.StoreFileBlob;
import com.blog.service.File.StoreUtil;
import com.blog.utils.RequestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

@RestController
@RequestMapping("/file")
public class FileContorller {

    @PostMapping("/upload/**")
    public String fileChunkUpload(@RequestParam("file") MultipartFile[] fileArray, HttpServletRequest request) throws IOException {
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
            File tmpFile = new File(file.getOriginalFilename());
            FileUtils.copyInputStreamToFile(file.getInputStream(), tmpFile);
            BlogStore.StoreFile.StoreTree.Builder storeTree = BlogStore.StoreFile.StoreTree.newBuilder()
                    .setStoreType(BlogStore.StoreFile.StoreTypeEnum.Blob)
                    .setOwnerType(fileUrl.getOwnerType())
                    .setOwnerId(fileUrl.getOwnerId())
                    .setFileName(file.getOriginalFilename())
//                    .setContentType(file.getContentType())
                    .setContentType(StoreUtil.getContentType(tmpFile))
                    .setFileSize(file.getSize())
                    .setCreateTime(System.currentTimeMillis())
                    .setUpdateTime(System.currentTimeMillis())
                    .setCommitterId(fileUrl.getUserId());
            tmpFile.delete();
            try (InputStream in = file.getInputStream()) {
                storeTree.addAllChildItem(StoreFileBlob.writeFile(IOUtils.toByteArray(in)));
                StoreFactory.addStore(new FileUrl(originPath, RequestUtils.getUserId(request)), storeTree.build());
            } catch (IOException e) {
                return "";
            }
        }
        return "ok";
    }

    @GetMapping(value = "/preview/**")
    public ResponseEntity<byte[]> filePreview(HttpServletRequest request) {
        String originPath = request.getRequestURI().replace("/file/preview", "");
        FileUrl fileUrl = new FileUrl(originPath, RequestUtils.getUserId(request));
        if (!fileUrl.isExist() || fileUrl.isFolder()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            HttpHeaders httpHeaders = new HttpHeaders();
            // 不是用缓存
            httpHeaders.setCacheControl(CacheControl.noCache());
            httpHeaders.setPragma("no-cache");
            httpHeaders.setExpires(0L);
            httpHeaders.setContentType(MediaType.valueOf(fileUrl.getStoreTree().getContentType()));
            return new ResponseEntity<>(
                    StoreFileBlob.readFile(fileUrl.getStoreTree().getChildItemList()), httpHeaders, HttpStatus.OK);
        }
    }
}
