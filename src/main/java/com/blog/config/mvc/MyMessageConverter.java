package com.blog.config.mvc;

import com.blog.config.jersey.BlogMediaType;
import com.google.protobuf.Message;
import lombok.SneakyThrows;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class MyMessageConverter implements HttpMessageConverter<Message> {
    @Override
    public boolean canRead(Class<?> type, MediaType mediaType) {
        return Message.class.isAssignableFrom(type);
    }

    @Override
    public boolean canWrite(Class<?> type, MediaType mediaType) {
        return Message.class.isAssignableFrom(type);
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Collections.singletonList(new MediaType(BlogMediaType.PROTOBUF_TYPE, BlogMediaType.PROTOBUF_SUBTYPE));

    }

    @SneakyThrows
    @Override
    public Message read(Class<? extends Message> type, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        Method newBuilder = type.getMethod("newBuilder");
        Message.Builder builder = (Message.Builder) newBuilder.invoke(type);
        return builder.mergeFrom(httpInputMessage.getBody()).build();
    }

    @Override
    public void write(Message message, MediaType mediaType, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        message.writeTo(httpOutputMessage.getBody());
    }
}
