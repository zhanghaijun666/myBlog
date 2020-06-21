package com.blog.config.mvc;

import com.blog.config.jersey.BlogMediaType;
import com.google.protobuf.Message;
import lombok.SneakyThrows;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

public class MyMessageConverter extends AbstractHttpMessageConverter<Message> {

    public MyMessageConverter() {
        super(new MediaType(BlogMediaType.PROTOBUF_TYPE, BlogMediaType.PROTOBUF_SUBTYPE));
        this.setDefaultCharset(StandardCharsets.UTF_8);
    }

    @Override
    protected boolean supports(Class<?> type) {
        return Message.class.isAssignableFrom(type);
    }

    @SneakyThrows
    @Override
    protected Message readInternal(Class<? extends Message> type, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        Method newBuilder = type.getMethod("newBuilder");
        Message.Builder builder = (Message.Builder) newBuilder.invoke(type);
        return builder.mergeFrom(httpInputMessage.getBody()).build();
    }

    @Override
    protected void writeInternal(Message message, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        message.writeTo(httpOutputMessage.getBody());
    }

}
