package com.blog.db;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("user")
public class user extends Model implements CommonModel {
    public static final int DEFAULT_USER_ID = 0;

}
