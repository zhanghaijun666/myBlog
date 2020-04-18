package com.blog.service.File;

import com.blog.proto.BlogStore;
import com.blog.utils.EncryptUtils;
import com.blog.utils.PathUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;

public class StoreFileBlob implements StoreFile {
    private static final int MAX_BLOB_SIZE = 1 * 1024 * 1024;

    @Override
    public File getHashFile(String hash) {
        return new File(PathUtils.joinPath(this.getStorePath(), "blob", this.getHashDir(hash)), hash);
    }

    public BlogStore.FileStore.HashList writeFile(byte[] bytes) throws IOException {

        BlogStore.FileStore.HashList.Builder builder = BlogStore.FileStore.HashList.newBuilder();
        int totalSize = bytes.length;
        int indexSize = 0;
        while (indexSize < totalSize) {
            byte[] blobBytes = Arrays.copyOfRange(bytes, indexSize, Math.min(indexSize + StoreFileBlob.MAX_BLOB_SIZE, totalSize));
            String hash = EncryptUtils.sha1(blobBytes);
            builder.addHsah(hash);
            File hashFile = this.getHashFile(hash);
            if (!hashFile.getParentFile().exists()) {
                hashFile.getParentFile().mkdirs();
            }
            if (hashFile.exists()) {
                continue;
            }
            Files.write(hashFile.toPath(), blobBytes);
            indexSize += StoreFileBlob.MAX_BLOB_SIZE;
        }
        return builder.build();
    }

    public void readFile(BlogStore.FileStore.HashList list, OutputStream out) {
        for (String hash : list.getHsahList()) {
            File hashFile = this.getHashFile(hash);
            try (InputStream in = new FileInputStream(hashFile)) {
                int len = -1;
                byte[] bytes = new byte[1 * 1024 * 1024];
                while ((len = in.read(bytes)) != -1) {
                    out.write(bytes, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
