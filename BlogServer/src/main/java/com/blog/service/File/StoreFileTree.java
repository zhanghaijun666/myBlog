package com.blog.service.File;

import com.blog.proto.BlogStore;
import com.blog.utils.EncryptUtils;
import com.blog.utils.PathUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class StoreFileTree implements StoreFile {

    @Override
    public File getHashFile(String hash) {
        return new File(PathUtils.joinPath(this.getStorePath(), "tree", this.getHashDir(hash)), hash);
    }

    public String writeFile(BlogStore.FileStore.StoreTree tree) throws IOException {
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
    }

    public BlogStore.FileStore.StoreTree readFile(String hash) throws IOException {
        File hashFile = this.getHashFile(hash);
        try (InputStream in = new FileInputStream(hashFile)) {
            return BlogStore.FileStore.StoreTree.parseFrom(IOUtils.toByteArray(in));
        }
    }
}
