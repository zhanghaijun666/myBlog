package com.blog.config;

import com.blog.jersey.ProtobufReader;
import com.blog.jersey.ProtobufWriter;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

@Component
@ApplicationPath("api")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(ProtobufReader.class);
        register(ProtobufWriter.class);
        register(MultiPartFeature.class);
        packages("com.blog.api");
    }
}
