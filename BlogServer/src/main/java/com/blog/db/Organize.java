package com.blog.db;

public interface Organize {
    public User setFileHash(String hash);

    public String getFileHash();

    boolean saveIt();

}
