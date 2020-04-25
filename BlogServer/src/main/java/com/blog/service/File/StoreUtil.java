package com.blog.service.File;

import com.blog.proto.BlogStore;
import com.blog.utils.PathUtils;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class StoreUtil {

    public static List<BlogStore.StoreFile.StoreTree> getChildList(BlogStore.StoreFile.StoreTree tree) {
        List<BlogStore.StoreFile.StoreTree> list = new ArrayList<>();
        if (tree.getStoreType() == BlogStore.StoreFile.StoreTypeEnum.Tree) {
            for (String hash : tree.getChildItemList()) {
                list.add(StoreFileTree.readFile(hash));
            }
        }
        return list;
    }

    public static Set<String> getChildNameList(FileUrl url) {
        BlogStore.StoreFile.StoreTree tree = url.getStoreTree();
        if (null == tree) {
            return Collections.EMPTY_SET;
        }
        return StoreUtil.getChildList(tree).stream().map(BlogStore.StoreFile.StoreTree::getFileName).collect(Collectors.toSet());
    }

    private static void toZip(Collection<BlogStore.StoreFile.StoreTree> treeList, ZipOutputStream zos, String parentPath) throws IOException {
        for (BlogStore.StoreFile.StoreTree itemTree : treeList) {
            String filePath = PathUtils.joinPath(parentPath, itemTree.getFileName());
            filePath = filePath.indexOf("/") == 0 ? filePath.substring(1) : filePath;
            switch (itemTree.getStoreType()) {
                case Tree:
                    StoreUtil.toZip(StoreUtil.getChildList(itemTree), zos, filePath);
                    break;
                case Blob:
                    zos.putNextEntry(new ZipEntry(filePath));
                    StoreFileBlob.readFile(itemTree.getChildItemList(), zos);
                    break;
                default:
            }
        }
    }

    public static void toZip(Collection<BlogStore.StoreFile.StoreTree> treeList, OutputStream out) {
        try (ZipOutputStream zos = new ZipOutputStream(out)) {
            StoreUtil.toZip(treeList, zos, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
