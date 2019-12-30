package com.blog.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/demo")
public class UserApi {

    @GET
    @Path("/{str}")
    public String reverse(@PathParam("str") String input) {
        return new StringBuilder(input).reverse().toString();
    }
}
