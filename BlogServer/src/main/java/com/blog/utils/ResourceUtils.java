package com.blog.utils;

import java.io.*;

public class ResourceUtils {

    private static final byte[] NEWLINE = "\r\n".getBytes();

    public static void writeResource(File file, final OutputStream out) throws IOException {
        if (file.getName().startsWith("packed-")) {
            try (BufferedReader in = new BufferedReader(new FileReader(file))) {
                String filePath;
                while ((filePath = in.readLine()) != null) {
                    filePath = filePath.trim();
                    if (filePath.length() > 0 && !filePath.startsWith("#")) {
                        File innerFile = new File(file.getParent(), filePath);
                        writeResource(innerFile, out);
                        out.write(ResourceUtils.NEWLINE);
                    }
                }
            }
        } else if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            for (File childFile : childFiles) {
                writeResource(childFile, out);
                out.write(ResourceUtils.NEWLINE);
            }
        } else {
            byte[] buffer = new byte[2048];
            try (BufferedInputStream innerStream = new BufferedInputStream(new FileInputStream(file))) {
                int bytesSize;
                while ((bytesSize = innerStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesSize);
                }
                out.flush();
            }
        }
    }
}
