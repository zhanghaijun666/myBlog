package com.blog.api;

import com.blog.db.User;
import com.blog.jersey.BlogMediaType;
import com.blog.proto.BlogStore;
import com.blog.service.File.FileUrl;
import com.blog.service.File.StoreFileTree;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Path("/file")
public class FileApi {

    @GET
    @Path("/get/{path:.*}")
    @Produces({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    @Consumes({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    public BlogStore.FileStore.StoreTree getFile(@PathParam("path") String fullPath) throws IOException {
        FileUrl url = new FileUrl(fullPath, 0);
        User dbUser = (User) url.getOrganize();
        if (StringUtils.isNotBlank(dbUser.getFileHash())) {
            return new StoreFileTree().readFile(dbUser.getFileHash());
        }
        return BlogStore.FileStore.StoreTree.getDefaultInstance();
    }


    @GET
    @Path("/download/{filename}")
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
