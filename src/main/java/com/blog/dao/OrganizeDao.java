package com.blog.dao;

import com.blog.db.Organize;
import com.blog.db.User;
import com.blog.proto.BlogStore;

public class OrganizeDao {

    public static Organize getOrganize(int type, int id) {
        switch (type) {
            case BlogStore.OwnerType.User_VALUE:
                return User.findById(id);
            default:
                return null;
        }
    }
}
