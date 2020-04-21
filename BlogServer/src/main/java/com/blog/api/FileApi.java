package com.blog.api;

import com.blog.jersey.BlogMediaType;
import com.blog.proto.BlogStore;
import com.blog.service.File.FileUrl;
import com.blog.service.File.StoreFactory;
import com.blog.service.File.StoreFileTree;
import com.blog.service.File.StoreUtil;
import com.blog.utils.RequestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Path("/file")
@Produces({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
@Consumes({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
public class FileApi {

    @GET
    @Path("/{path:.*}")
    public BlogStore.StoreFile.StoreList getFile(@PathParam("path") String fullPath, @Context HttpServletRequest request) {
        FileUrl url = new FileUrl(fullPath, RequestUtils.getUserId(request));
        if (!url.isOwner()) {
            return BlogStore.StoreFile.StoreList.getDefaultInstance();
        }
        BlogStore.StoreFile.StoreTree tree = url.getStoreTree();
        if (null == tree) {
            return BlogStore.StoreFile.StoreList.getDefaultInstance();
        }
        List<BlogStore.StoreFile.StoreTree> list = new ArrayList<>();
        StoreFileTree fileTree = new StoreFileTree();
        for (String hash : tree.getChildItemList()) {
            list.add(fileTree.readFile(hash).toBuilder().clearChildItem().build());
        }
        return BlogStore.StoreFile.StoreList.newBuilder()
                .setParentItem(tree.toBuilder().clearChildItem().build())
                .addAllItems(list)
                .build();
    }

    @PUT
    @Path("/addfolder/{name}/{path:.*}")
    public BlogStore.Result addFolder(@PathParam("name") String folderName, @PathParam("path") String fullPath, @Context HttpServletRequest request) {
        FileUrl fileUrl = new FileUrl(fullPath, RequestUtils.getUserId(request));
        if (!fileUrl.isOwner() || StringUtils.isBlank(folderName)) {
            return BlogStore.Result.newBuilder().setCode(BlogStore.ReturnCode.RETURN_ERROR).build();
        }
        if (StoreUtil.getChildNameList(fileUrl).contains(folderName)) {
            return BlogStore.Result.newBuilder().setCode(BlogStore.ReturnCode.RETURN_FILE_EXIST).build();
        }
        BlogStore.StoreFile.StoreTree.Builder storeTree = BlogStore.StoreFile.StoreTree.newBuilder()
                .setStoreType(BlogStore.StoreFile.StoreTypeEnum.Tree)
                .setOwnerType(fileUrl.getOwnerType())
                .setOwnerId(fileUrl.getOwnerId())
                .setFileName(folderName)
                .setContentType("")
                .setFileSize(0)
                .setCreateTime(System.currentTimeMillis())
                .setUpdateTime(System.currentTimeMillis())
                .setCommitterId(fileUrl.getUserId());
        StoreFactory.addStore(fileUrl, storeTree.build());
        return BlogStore.Result.newBuilder().setCode(BlogStore.ReturnCode.RETURN_OK).build();
    }

    @DELETE
    @Path("/{path:.*}")
    public BlogStore.Result deleteFile(@PathParam("path") String fullPath, @Context HttpServletRequest request) {
        FileUrl fileUrl = new FileUrl(fullPath, RequestUtils.getUserId(request));
        if (!fileUrl.isOwner()) {
            return BlogStore.Result.newBuilder().setCode(BlogStore.ReturnCode.RETURN_ERROR).build();
        }
        StoreFactory.deleteStore(fileUrl.getParentUrl(), fileUrl.getHashTree());
        return BlogStore.Result.newBuilder().setCode(BlogStore.ReturnCode.RETURN_OK).build();
    }

    @POST
    @Path("/rename/{name}/{path:.*}")
    public BlogStore.Result rename(@PathParam("name") String newName, @PathParam("path") String fullPath, @Context HttpServletRequest request) {
        FileUrl fileUrl = new FileUrl(fullPath, RequestUtils.getUserId(request));
        if (!fileUrl.isOwner() || StringUtils.isBlank(newName)) {
            return BlogStore.Result.newBuilder().setCode(BlogStore.ReturnCode.RETURN_ERROR).build();
        }
        if (StringUtils.equals(newName, fileUrl.getFileName()) || StoreUtil.getChildNameList(fileUrl).contains(newName)) {
            return BlogStore.Result.newBuilder().setCode(BlogStore.ReturnCode.RETURN_FILE_EXIST).build();
        }
        StoreFactory.rename(fileUrl, newName);
        return BlogStore.Result.newBuilder().setCode(BlogStore.ReturnCode.RETURN_OK).build();
    }


    @GET
    @Path("/download/{filename}")
    @Produces(BlogMediaType.WILDCARD)
    @Consumes(BlogMediaType.WILDCARD)
    public void down(@PathParam("filename") String filename, @Context HttpServletResponse response) throws Exception {
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
        response.setHeader("Cache-Control", "no-cache");
        File file = new File("D:\\", filename);
        FileUtils.copyFile(file, response.getOutputStream());
    }


    /**
     * curl -X POST -F "file=@111.txt" http://localhost:8080/api/file/upload
     */
    @POST
    @Path("/upload")
    @Produces(BlogMediaType.TEXT_PLAIN)
    @Consumes(BlogMediaType.MULTIPART_FORM_DATA)
    public String upload(@FormDataParam("file") InputStream fileInputStream, @FormDataParam("file") FormDataContentDisposition disposition) throws Exception {
        File upload = new File("D:/files/", UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(disposition.getFileName()));
        if (!upload.getParentFile().exists()) {
            upload.getParentFile().mkdirs();
        }
        try {
            FileUtils.copyInputStreamToFile(fileInputStream, upload);
        } catch (IOException e) {
            return "上传文件失败";
        }
        return "upload success!";
    }
}
