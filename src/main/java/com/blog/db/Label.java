package com.blog.db;

import com.blog.proto.BlogStore;
import com.blog.utils.BasicConvertUtils;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import java.util.ArrayList;
import java.util.List;

@Table("label")
public class Label extends Model implements CommonModel {

    public static List<BlogStore.Label> getAllActive() {
        List<Label> labels = Label.find("status = ? ", BlogStore.Status.StatusActive_VALUE);
        List<BlogStore.Label> list = new ArrayList<>();
        for (Label label : labels) {
            list.add(label.builderMessage().build());
        }
        return list;
    }

    public static Label saveMessage(BlogStore.Label message) {
        Label label = Label.findFirst("title = ? ", message.getTitle());
        if (null == label) {
            label = Label.create("title = ? ", message.getTitle());
        }
        label.setInteger("parent_id", message.getParenId());
        label.setString("title", message.getTitle());
        label.setString("description", message.getDescription());
        label.setString("color", message.getColor());
        label.setInteger("status", message.getStatusValue());
        return label;
    }

    public BlogStore.Label.Builder builderMessage() {
        return BlogStore.Label.newBuilder()
                .setLabelId(BasicConvertUtils.toInteger(getInteger("id"), 0))
                .setParenId(BasicConvertUtils.toInteger(getInteger("parent_id"), 0))
                .setTitle(BasicConvertUtils.toString(getString("title"), ""))
                .setDescription(BasicConvertUtils.toString(getString("description"), ""))
                .setColor(BasicConvertUtils.toString(getString("color"), ""))
                .setStatusValue(BasicConvertUtils.toInteger(getInteger("status"), 0));
    }

}
