package com.blog.service.File;

import com.blog.proto.BlogStore;
import com.blog.utils.EncryptUtils;
import com.blog.utils.PathUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StoreFileBlob extends StoreFile {
    private static final int MAX_BLOB_SIZE = 1 * 1024 * 1024;

    @Override
    public File getHashFile(String hash) {
        return new File(PathUtils.joinPath(StoreFileBlob.getStorePath(), "blob", StoreFileBlob.getHashDir(hash)), hash);
    }

    public List<String> writeFile(byte[] bytes) throws IOException {
        List<String> hashList = new ArrayList<>();
        int totalSize = bytes.length;
        int indexSize = 0;
        while (indexSize < totalSize) {
            byte[] blobBytes = Arrays.copyOfRange(bytes, indexSize, Math.min(indexSize + StoreFileBlob.MAX_BLOB_SIZE, totalSize));
            indexSize += StoreFileBlob.MAX_BLOB_SIZE;
            String hash = EncryptUtils.sha1(blobBytes);
            hashList.add(hash);
            File hashFile = this.getHashFile(hash);
            if (!hashFile.getParentFile().exists()) {
                hashFile.getParentFile().mkdirs();
            }
            if (hashFile.exists()) {
                continue;
            }
            Files.write(hashFile.toPath(), blobBytes);
        }
        return hashList;
    }

    public void readFile(List<String> list, OutputStream out) {
        for (String hash : list) {
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
