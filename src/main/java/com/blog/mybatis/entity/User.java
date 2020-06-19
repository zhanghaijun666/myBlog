package com.blog.mybatis.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.blog.proto.BlogStore;
import com.blog.utils.BasicConvertUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * Create user for db
 * </p>
 *
 * @author haijun.zhang
 * @since 2020-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user")
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String username;

    private String password;

    private String nickname;

    private String email;

    private String phone;

    private Long birthday;

    private String fileHash;

    private Integer status;

    private Date createdAt;

    private Integer createdBy;

    private Date updatedAt;

    private Integer updatedBy;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public BlogStore.UserItem bulidUserItem() {
        return BlogStore.UserItem.newBuilder()
                .setUserId(this.getId())
                .setUsername(this.getUsername())
                .setNickname(BasicConvertUtils.toString(this.getNickname(), this.getUsername()))
                .setEmail(BasicConvertUtils.toString(this.getEmail(), ""))
                .setPhone(BasicConvertUtils.toString(this.getPhone(), ""))
                .setBirthday(BasicConvertUtils.toLong(this.getBirthday(), 0))
                .setStatus(BlogStore.Status.forNumber(this.getStatus()))
                .build();
    }

}
