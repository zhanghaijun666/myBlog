package com.blog.service.File;

import com.blog.utils.BasicConvertUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Setter
@Getter
public class FileUrl {
    public static Pattern standUrlPattern = Pattern.compile("\\/?(?<rootHash>[^/]+)?\\/(?<gtype>[\\d]+)\\/(?<gpid>-?[\\d]+)\\/(?<bucket>[^/]+)(?<path>\\/?.*)");
    public static final String DEFAULT_ROOT_HASH = "default";
    public static final String DEFAULT_BUCKET = "directory";
    public static final int DEFAULT_GPTYPE = 0;
    public static final int DEFAULT_GPID = 0;

    private String rootHash = FileUrl.DEFAULT_ROOT_HASH;
    private int gpType = FileUrl.DEFAULT_GPTYPE;
    private int gpId = FileUrl.DEFAULT_GPID;
    private String bucket = FileUrl.DEFAULT_BUCKET;
    private String path = "";
    private int userId = FileUrl.DEFAULT_GPID;

    private String storeHash = null;    //文件库的真是hash

    public FileUrl(String originPath, int userId) {
        this.userId = userId;
        this.decode(originPath);
    }

    private void decode(String standUrlPath) {
        if (StringUtils.isNotEmpty(standUrlPath)) {
            String originFullPath = standUrlPath.replaceAll("\\/+", "/");
            Matcher matcher = standUrlPattern.matcher(originFullPath);
            if (matcher.matches()) {
                String originHash = BasicConvertUtils.toString(matcher.group("rootHash"), FileUrl.DEFAULT_ROOT_HASH);
                int originType = BasicConvertUtils.toInteger(matcher.group("gtype"), FileUrl.DEFAULT_GPTYPE);
                int originId = BasicConvertUtils.toInteger(matcher.group("gpid"), FileUrl.DEFAULT_GPID);
                String originBucket = BasicConvertUtils.toString(matcher.group("bucket"), FileUrl.DEFAULT_BUCKET);
                String originPath = BasicConvertUtils.toString(matcher.group("path"), "");
                if (originPath.length() > 0 && originPath.lastIndexOf("/") == originPath.length() - 1) {
                    originPath = originPath.substring(0, originPath.length() - 1);
                }
                this.rootHash = originHash;
                this.gpType = originType;
                this.gpId = originId;
                this.bucket = originBucket;
                this.path = originPath;
            }
        }
    }

}
