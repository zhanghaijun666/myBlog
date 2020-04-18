package com.blog.service.File;

import com.blog.utils.PathUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

public interface StoreFile {

    File getHashFile(String hash);

    default String getHashDir(String hash) {
        if (StringUtils.isBlank(hash)) {
            return "";
        }
        if (hash.length() < 6) {
            return hash;
        }
        return PathUtils.joinPath(hash.substring(0, 5), hash.substring(5, 7));
    }

    default String getStorePath() {
        return PathUtils.joinPath(PathUtils.getBlogServerPath(), "store");
    }


}
