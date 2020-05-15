package com.blog.service.File;

import com.blog.proto.BlogStore.StoreFile;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class StoreFactory {

    public static void addStore(FileUrl parentUrl, StoreFile.StoreTree newTree) {
        String treeHash = StoreFileTree.writeFile(newTree);
        StoreFactory.performStoreTreeUpdate(parentUrl, new Action() {
            @Override
            public StoreFile.StoreTree perform(StoreFile.StoreTree oldStoreItem) {
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
            public StoreFile.StoreTree perform(StoreFile.StoreTree oldStoreItem) {
                return oldStoreItem.toBuilder()
                        .setFileName(newName)
                        .build();
            }
        });
    }

    public static void deleteStore(FileUrl parentUrl, Map.Entry<String, StoreFile.StoreTree> entry) {
        if (null == entry) {
            return;
        }
        StoreFactory.performStoreTreeUpdate(parentUrl, new Action() {
            @Override
            public StoreFile.StoreTree perform(StoreFile.StoreTree oldStoreItem) {
                List<String> list = new ArrayList<>(oldStoreItem.getChildItemList());
                list.remove(entry.getKey());
                return oldStoreItem.toBuilder()
                        .clearChildItem().addAllChildItem(list)
                        .setFileSize(oldStoreItem.getFileSize() - entry.getValue().getFileSize())
                        .build();
            }
        });
    }

    interface Action {
        public StoreFile.StoreTree perform(StoreFile.StoreTree oldStoreItem);
    }

    public static String performStoreTreeUpdate(FileUrl fileUrl, Action action) {
        LinkedHashMap<String, StoreFile.StoreTree> map = StoreFactory.getStoreTreeList(fileUrl.getStoreHash(), fileUrl.getPath());
        if (null == map || map.isEmpty()) {
            StoreFile.StoreTree rootTree = action.perform(StoreFile.StoreTree.newBuilder()
                    .setStoreType(StoreFile.StoreTypeEnum.Tree)
                    .setOwnerType(fileUrl.getOwnerType())
                    .setOwnerId(fileUrl.getOwnerId())
                    .setFileName("").setContentType("").setFileSize(0)
                    .setCreateTime(System.currentTimeMillis())
                    .setUpdateTime(System.currentTimeMillis())
                    .setCommitterId(fileUrl.getUserId())
                    .build());
            String rootHash = StoreFileTree.writeFile(rootTree);
            fileUrl.getOrganize().setFileHash(rootHash).saveIt();
            return rootHash;
        }
        //逆向便利map
        ListIterator<Map.Entry<String, StoreFile.StoreTree>> iter = new ArrayList<>(map.entrySet()).listIterator(map.size());
        Map.Entry<String, StoreFile.StoreTree> entry = null;
        StoreFile.StoreTree newTree = null;
        String newHash = "";
        while (iter.hasPrevious()) {
            if (null == entry) {
                entry = iter.previous();
                newTree = action.perform(entry.getValue());
            } else if (null != entry) {
                String oldHash = entry.getKey();
                long oldSize = entry.getValue().getFileSize();
                entry = iter.previous();
                List<String> childHash = new ArrayList<>(entry.getValue().getChildItemList());
                childHash.remove(oldHash);
                childHash.add(newHash);
                newTree = entry.getValue().toBuilder()
                        .clearChildItem().addAllChildItem(childHash)
                        .setFileSize(entry.getValue().getFileSize() - oldSize + newTree.getFileSize())
                        .build();
            }
            newHash = StoreFileTree.writeFile(newTree);
        }
        fileUrl.getOrganize().setFileHash(newHash).saveIt();
        return newHash;
    }

    public static LinkedHashMap<String, StoreFile.StoreTree> getStoreTreeList(String storeHash, String path) {
        LinkedHashMap<String, StoreFile.StoreTree> map = new LinkedHashMap<>();
        StoreFile.StoreTree tree = StoreFileTree.readFile(storeHash);
        if (null == tree) {
            return map;
        }
        map.put(storeHash, tree);
        for (String fileName : path.split("/")) {
            if (StringUtils.isBlank(fileName)) {
                continue;
            }
            boolean existChild = false;
            for (String hash : tree.getChildItemList()) {
                StoreFile.StoreTree childTree = StoreFileTree.readFile(hash);
                if (StringUtils.equals(fileName, childTree.getFileName())) {
                    map.put(hash, childTree);
                    tree = childTree;
                    existChild = true;
                    break;
                }
            }
            if (!existChild) {
                return new LinkedHashMap<>();
            }
        }
        return map;
    }
}
