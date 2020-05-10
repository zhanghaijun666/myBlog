package com.blog.service.File;

import com.blog.utils.PathUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;

import java.io.*;
import java.util.*;

public class MimeUtils {
    private volatile static MimeUtils singleton;
    private List<FileMime> list = new ArrayList<>();

    public static MimeUtils getInstance() {
        if (singleton == null) {
            synchronized (MimeUtils.class) {
                if (singleton == null) {
                    singleton = new MimeUtils();
                }
            }
        }
        return singleton;
    }

    public String getContentType(String fileName) {
        FileMime fileMime = this.getFileMime(fileName);
        return null == fileMime ? MediaType.APPLICATION_OCTET_STREAM_VALUE : fileMime.getContentType();
    }

    public String getIcon(String fileName) {
        FileMime fileMime = this.getFileMime(fileName);
        return null == fileMime ? "other" : fileMime.getIcon();
    }

    private FileMime getFileMime(String fileName) {
        if (StringUtils.isBlank(fileName) || fileName.lastIndexOf(".") == -1) {
            return null;
        }
        String ext = fileName.substring(fileName.lastIndexOf("."));
        Optional<FileMime> result = list.stream().filter((fileMime) -> StringUtils.equals(fileMime.getExt(), ext)).findFirst();
        return result.isPresent() ? result.get() : null;
    }


    private MimeUtils() {
        File file = new File(PathUtils.joinPath(PathUtils.getBlogServerPath(), "conf/mimes.conf"));
        if (!file.exists()) {
            return;
        }
        try (BufferedReader read = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = read.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#") || line.isEmpty()) {
                    continue;
                }
                String[] entry = line.split("\\s+");
                int count = entry.length;
                if (count < 4) {
                    continue;
                }
                list.add(new FileMime(entry[0], entry[1], entry[2], entry[3]));
            }
        } catch (Exception e) {
        }
    }

    private class FileMime {
        private String mimeType;
        private String icon;
        private String ext;
        private String contentType;

        public FileMime(String mimeType, String icon, String ext, String contentType) {
            this.mimeType = mimeType;
            this.icon = icon;
            this.ext = ext;
            this.contentType = contentType;
        }

        public String getMimeType() {
            return mimeType;
        }

        public String getIcon() {
            return icon;
        }

        public String getExt() {
            return ext;
        }

        public String getContentType() {
            return contentType;
        }

    }
}
