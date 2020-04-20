package com.blog.service.File;

import com.blog.proto.BlogStore;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class StoreUtil {

    public static Set<String> getChildNameList(FileUrl url) {
        BlogStore.StoreFile.StoreTree tree = url.getStoreTree();
        if (null == tree) {
            return Collections.EMPTY_SET;
        }
        Set<String> nameList = new HashSet<>();
        StoreFileTree fileTree = new StoreFileTree();
        for (String hash : tree.getChildItemList()) {
            nameList.add(fileTree.readFile(hash).getFileName());
        }
        return nameList;
    }
}
