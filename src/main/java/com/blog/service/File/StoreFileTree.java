package com.blog.service.File;

import com.blog.proto.BlogStore;
import com.blog.utils.EncryptUtils;
import com.blog.utils.PathUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StoreFileTree extends StoreFile {

    private static File getHashFile(String hash) {
        return new File(PathUtils.joinPath(StoreFileTree.getStorePath(), "tree", StoreFileTree.getHashDir(hash)), hash);
    }

    public static String writeFile(BlogStore.StoreFile.StoreTree tree) {
        try {
            byte[] bytes = tree.toByteArray();
            String hash = EncryptUtils.sha1(bytes);
            File hashFile = StoreFileTree.getHashFile(hash);
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

    public static List<BlogStore.StoreFile.StoreTree> readFile(Collection<String> hashList) {
        List<BlogStore.StoreFile.StoreTree> list = new ArrayList<>();
        for (String hash : hashList) {
            list.add(readFile(hash));
        }
        return list;
    }

    public static BlogStore.StoreFile.StoreTree readFile(String hash) {
        if (StringUtils.isBlank(hash)) {
            return null;
        }
        File hashFile = StoreFileTree.getHashFile(hash);
        try (InputStream in = new FileInputStream(hashFile)) {
            return BlogStore.StoreFile.StoreTree.parseFrom(IOUtils.toByteArray(in));
        } catch (Exception e) {
            return null;
        }
    }
}
