package com.blog.db;

import java.util.Date;

import com.blog.proto.BlogStore;
import org.javalite.activejdbc.Model;

public interface CommonModel {
    public static final String STATUS = "status";
    public static final String CREATED_BY = "created_by";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_BY = "updated_by";
    public static final String UPDATED_AT = "updated_at";

    //model methods
    default Object getId() {
        return getLong("id");
    }

    default BlogStore.Status getStatus() {
        Integer status = getInteger(STATUS);
        return null == status ? BlogStore.Status.StatusDefault : BlogStore.Status.forNumber(status);
    }

    default void setStatus(BlogStore.Status status) {
        set(STATUS, status.getNumber());
    }

    default int getCreatedBy() {
        return getInteger(CREATED_BY);
    }

    default void setCreatedBy(Integer createdBy) {
        set(CREATED_BY, createdBy);
    }

    default int getUpdatedBy() {
        return getInteger(UPDATED_BY);
    }

    default void setUpdatedBy(Integer updatedBy) {
        set(UPDATED_BY, updatedBy);
    }

    default Long getCreatedAt() {
        return getLong(CREATED_AT);
    }

    default Long getUpdatedAt() {
        return getLong(UPDATED_AT);
    }

    default void setCreatedAt(Date date) {
        set(CREATED_AT, date);
    }

    default void setUpdatedAt(Date date) {
        set(UPDATED_AT, new java.sql.Timestamp(date.getTime()));
    }

    Long getLong(String attributeName);

    Integer getInteger(String attributeName);

    <T extends Model> T set(String attributeName, Object value);

    boolean saveIt();

//    default Date getDate(String attributeName) {
//        return new Date(getTimestamp(attributeName).getTime());
//    }
//    public Timestamp getTimestamp(String attributeName);
//    default public <T extends Model> T setDate(String attributeName, Date value) {
//        return set(attributeName, value);
//    }
}
