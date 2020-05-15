package com.blog.api;

import com.blog.jersey.BlogMediaType;
import com.blog.proto.BlogStore;
import com.blog.service.File.*;
import com.blog.utils.RequestUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        for (String hash : tree.getChildItemList()) {
            list.add(StoreFileTree.readFile(hash).toBuilder().clearChildItem().build());
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
        if (StringUtils.equals(newName, fileUrl.getFileName()) || StoreUtil.getChildNameList(fileUrl.getParentUrl()).contains(newName)) {
            return BlogStore.Result.newBuilder().setCode(BlogStore.ReturnCode.RETURN_FILE_EXIST).build();
        }
        StoreFactory.rename(fileUrl, newName);
        return BlogStore.Result.newBuilder().setCode(BlogStore.ReturnCode.RETURN_OK).build();
    }


    @GET
    @Path("/download/{path:.*}")
    @Produces(BlogMediaType.WILDCARD)
    @Consumes(BlogMediaType.WILDCARD)
    public void down(@PathParam("path") String fullPath, @Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception {
        FileUrl fileUrl = new FileUrl(fullPath, RequestUtils.getUserId(request));
        if (!fileUrl.isOwner() || null == fileUrl.getStoreTree()) {
            response.setContentType("text/html;charset=utf-8");
            response.sendError(Response.Status.UNAUTHORIZED.getStatusCode(), "UNAUTHORIZED");
            return;
        }
        BlogStore.StoreFile.StoreTree tree = fileUrl.getStoreTree();
        response.setContentType("application/force-download");
        response.setCharacterEncoding("UTF-8");
        String userAgent = request.getHeader("User-Agent");
        String formFileName;
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            // 针对IE或者以IE为内核的浏览器：
            formFileName = URLEncoder.encode(tree.getFileName(), "UTF-8");
        } else {
            // 非IE浏览器的处理：
            formFileName = new String(tree.getFileName().getBytes("UTF-8"), "ISO-8859-1");
        }

        try (OutputStream out = response.getOutputStream()) {
            if (fileUrl.isFolder()) {
                response.addHeader("Content-Disposition", "attachment;fileName=" + formFileName + ".zip");
                StoreUtil.toZip(Collections.singletonList(tree), out);
            } else {
                response.addHeader("Content-Disposition", "attachment;fileName=" + formFileName);
                StoreFileBlob.readFile(tree.getChildItemList(), out);
            }
        }
    }
//    /**
//     * curl -X POST -F "file=@111.txt" http://localhost:8080/api/file/upload
//     */
//    @POST
//    @Path("/upload")
//    @Produces(BlogMediaType.TEXT_PLAIN)
//    @Consumes(BlogMediaType.MULTIPART_FORM_DATA)
//    public String upload(@FormDataParam("file") InputStream fileInputStream, @FormDataParam("file") FormDataContentDisposition disposition) throws Exception {
//        File upload = new File("D:/files/", UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(disposition.getFileName()));
//        if (!upload.getParentFile().exists()) {
//            upload.getParentFile().mkdirs();
//        }
//        try {
//            FileUtils.copyInputStreamToFile(fileInputStream, upload);
//        } catch (IOException e) {
//            return "上传文件失败";
//        }
//        return "upload success!";
//    }
}
