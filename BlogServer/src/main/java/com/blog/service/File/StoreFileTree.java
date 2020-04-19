package com.blog.service.File;

import com.blog.proto.BlogStore;
import com.blog.utils.EncryptUtils;
import com.blog.utils.PathUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.Files;

public class StoreFileTree extends StoreFile {

    @Override
    public File getHashFile(String hash) {
        return new File(PathUtils.joinPath(StoreFileTree.getStorePath(), "tree", StoreFileTree.getHashDir(hash)), hash);
    }

    public String writeFile(BlogStore.StoreFile.StoreTree tree) {
        try {
            byte[] bytes = tree.toByteArray();
            String hash = EncryptUtils.sha1(bytes);
            File hashFile = this.getHashFile(hash);
            if (!hashFile.getParentFile().exists()) {
                hashFile.getParentFile().mkdirs();
            }
            if (!hashFile.exists()) {

                Files.write(hashFile.toPath(), bytes);

            }
            return hash;
        } catch (IOException e) {
            return "";
        }
    }

    public BlogStore.StoreFile.StoreTree readFile(String hash) {
        if (StringUtils.isBlank(hash)) {
            return null;
        }
        File hashFile = this.getHashFile(hash);
        try (InputStream in = new FileInputStream(hashFile)) {
            return BlogStore.StoreFile.StoreTree.parseFrom(IOUtils.toByteArray(in));
        } catch (Exception e) {
            return null;
        }
    }
}
