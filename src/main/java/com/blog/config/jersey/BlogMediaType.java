package com.blog.config.jersey;

import javax.ws.rs.core.MediaType;

public class BlogMediaType extends MediaType {

    public static final String PROTOBUF_TYPE = "application";
    public static final String PROTOBUF_SUBTYPE = "x-protobuf";
    public static final String APPLICATION_PROTOBUF = PROTOBUF_TYPE + "/" + PROTOBUF_SUBTYPE;
}
