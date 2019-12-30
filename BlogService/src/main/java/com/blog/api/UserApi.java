package com.blog.api;

import javax.ws.rs.*;

@Path("/demo")
//@Produces({"application/x-protobuf", "application/json"})
//@Consumes({"application/x-protobuf", "application/json"})
public class UserApi {

    @GET
    @Path("/{str}")
    public String reverse(@PathParam("str") String input) {
        return new StringBuilder(input).reverse().toString();
    }
}
