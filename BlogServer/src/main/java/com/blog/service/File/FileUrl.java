package com.blog.service.File;

import com.blog.dao.OrganizeDao;
import com.blog.db.Organize;
import com.blog.db.User;
import com.blog.proto.BlogStore;
import com.blog.utils.BasicConvertUtils;
import com.blog.utils.PathUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Setter
@Getter
public class FileUrl {
    public static Pattern standUrlPattern = Pattern.compile("\\/?(?<type>[\\d]+)\\/(?<id>-?[\\d]+)\\/(?<bucket>[^/]+)(?<path>\\/?.*)");
    public static final String DEFAULT_BUCKET = "default";
    public static final int DEFAULT_OWNER_TYPE = 0;
    public static final int DEFAULT_OWNER_ID = 0;

    private int OwnerType = FileUrl.DEFAULT_OWNER_TYPE;
    private int OwnerId = FileUrl.DEFAULT_OWNER_ID;
    private String bucket = FileUrl.DEFAULT_BUCKET;
    private String path = "";
    private int userId = FileUrl.DEFAULT_OWNER_ID;

    private String storeHash = null;    //文件库的真是hash

    public FileUrl(String originPath, int userId) {
        this.userId = userId;
        this.decode(originPath);
    }

    private void decode(String standUrlPath) {
        if (StringUtils.isNotEmpty(standUrlPath)) {
            String originFullPath = standUrlPath.replaceAll("\\/+", "/").replace("\\/$", "");
            Matcher matcher = standUrlPattern.matcher(originFullPath);
            if (matcher.matches()) {
                int originType = BasicConvertUtils.toInteger(matcher.group("type"), FileUrl.DEFAULT_OWNER_TYPE);
                int originId = BasicConvertUtils.toInteger(matcher.group("id"), FileUrl.DEFAULT_OWNER_ID);
                String originBucket = BasicConvertUtils.toString(matcher.group("bucket"), FileUrl.DEFAULT_BUCKET);
                String originPath = BasicConvertUtils.toString(matcher.group("path"), "");
                this.OwnerType = originType;
                this.OwnerId = originId;
                this.bucket = originBucket;
                this.path = originPath;
            }
        }
    }

    public String getStoreHash() {
        if (null == this.storeHash) {
            Organize organize = this.getOrganize();
            this.storeHash = organize == null ? null : organize.getFileHash();
        }
        return storeHash;
    }

    public FileUrl getParentUrl() {
        String parentPath = this.getPath().substring(0, this.getPath().lastIndexOf("/"));
        return new FileUrl(PathUtils.joinPath(this.getOwnerType(), this.getOwnerId(), this.getBucket(), parentPath), this.getUserId());
    }

    public String getFileName() {
        return PathUtils.getFileName(this.getPath());
    }

    public String getFullPath() {
        return PathUtils.joinPath(this.getOwnerType(), this.getOwnerId(), this.getBucket(), this.getPath());
    }

    public Organize getOrganize() {
        return OrganizeDao.getOrganize(this.getOwnerType(), this.getOwnerId());
    }

    public BlogStore.StoreFile.StoreTree getStoreTree() {
        List<BlogStore.StoreFile.StoreTree> list = StoreFactory.getStoreTreeList(this.getStoreHash(), this.getPath());
        return list.isEmpty() ? null : list.get(list.size() - 1);
    }

    public String getTreeHash() {
        List<BlogStore.StoreFile.StoreTree> list = StoreFactory.getStoreTreeList(this.getStoreHash(), this.getPath());
        if (list.size() > 1) {
            StoreFileTree fileTree = new StoreFileTree();
            return list.get(list.size() - 2).getChildItemList().stream().filter((hash) -> {
                return StringUtils.equals(fileTree.readFile(hash).getFileName(), this.getFileName());
            }).findFirst().get();
        }
        return "";
    }

    public boolean isOwner() {
        return this.getOwnerType() == BlogStore.OwnerType.User_VALUE && this.getUserId() > 0 && this.getUserId() == this.getOwnerId();
    }
}
