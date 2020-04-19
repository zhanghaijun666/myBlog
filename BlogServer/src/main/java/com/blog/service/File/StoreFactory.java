package com.blog.service.File;

import com.blog.proto.BlogStore;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StoreFactory {

    public static void addStore(FileUrl parentUrl, BlogStore.StoreFile.StoreTree newTree) {
        String treeHash = new StoreFileTree().writeFile(newTree);
        StoreFactory.performStoreTreeUpdate(parentUrl, new Action() {
            @Override
            public BlogStore.StoreFile.StoreTree perform(BlogStore.StoreFile.StoreTree oldStoreItem) {
                return oldStoreItem.toBuilder()
                        .addChildItem(treeHash)
                        .setFileSize(oldStoreItem.getFileSize() + newTree.getFileSize())
                        .build();
            }
        });
    }

    public static void rename(FileUrl fileUrl, String newName) {
        StoreFactory.performStoreTreeUpdate(fileUrl, new Action() {
            @Override
            public BlogStore.StoreFile.StoreTree perform(BlogStore.StoreFile.StoreTree oldStoreItem) {
                return oldStoreItem.toBuilder()
                        .setFileName(newName)
                        .build();
            }
        });
    }

    public static void deleteStore(FileUrl parentUrl, String deleteHash) {
        StoreFactory.performStoreTreeUpdate(parentUrl, new Action() {
            @Override
            public BlogStore.StoreFile.StoreTree perform(BlogStore.StoreFile.StoreTree oldStoreItem) {
                List<String> list = oldStoreItem.getChildItemList();
                list.remove(deleteHash);
                return oldStoreItem.toBuilder()
                        .clearChildItem()
                        .addAllChildItem(list)
                        .build();
            }
        });
    }

    interface Action {
        public BlogStore.StoreFile.StoreTree perform(BlogStore.StoreFile.StoreTree oldStoreItem);
    }

    public static String performStoreTreeUpdate(FileUrl fileUrl, Action action) {
        List<BlogStore.StoreFile.StoreTree> list = StoreFactory.getStoreTreeList(fileUrl.getStoreHash(), fileUrl.getPath());
        if (null == list || list.isEmpty()) {
            list.add(BlogStore.StoreFile.StoreTree.newBuilder()
                    .setStoreType(BlogStore.StoreFile.StoreTypeEnum.Tree)
                    .setOwnerType(fileUrl.getOwnerType())
                    .setOwnerId(fileUrl.getOwnerId())
                    .setFileName("")
                    .setContentType("")
                    .setFileSize(0)
                    .setCreateTime(System.currentTimeMillis())
                    .setUpdateTime(System.currentTimeMillis())
                    .setCommitterId(fileUrl.getUserId())
                    .build());
        }
        StoreFileTree fileTree = new StoreFileTree();
        BlogStore.StoreFile.StoreTree oldTree = null;
        BlogStore.StoreFile.StoreTree newTree = null;
        int index = list.size() - 1;
        String treeHash = "";
        while (index >= 0) {
            oldTree = list.get(index);
            if (index == list.size() - 1) {
                newTree = action.perform(oldTree);
            } else if (StringUtils.isNotBlank(treeHash)) {
                newTree = oldTree.toBuilder().setFileSize(0).build();
            }
            index--;
            treeHash = fileTree.writeFile(newTree);
        }
        fileUrl.getOrganize().setFileHash(treeHash).saveIt();
        return treeHash;
    }

    public static List<BlogStore.StoreFile.StoreTree> getStoreTreeList(String storeHash, String path) {
        List<BlogStore.StoreFile.StoreTree> list = new ArrayList<>();
        StoreFileTree fileTree = new StoreFileTree();
        BlogStore.StoreFile.StoreTree tree = fileTree.readFile(storeHash);
        if (null == tree) {
            return list;
        }
        list.add(tree);
        for (String fileName : path.split("/")) {
            if (StringUtils.isBlank(fileName)) {
                continue;
            }
            for (String hash : tree.getChildItemList()) {
                BlogStore.StoreFile.StoreTree childTree = fileTree.readFile(hash);
                if (StringUtils.equals(fileName, childTree.getFileName())) {
                    list.add(childTree);
                }
            }
            return Collections.EMPTY_LIST;
        }
        return list;
    }
}
