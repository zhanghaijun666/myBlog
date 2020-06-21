package com.blog.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.blog.proto.BlogStore;
import com.blog.utils.BasicConvertUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Size;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

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

    @NotBlank(message = "姓名 不允许为空")
    @Size(min = 2, max = 10, message = "姓名 长度必须在 {min} - {max} 之间")
    private String username;

    private String password;

    private String nickname;

    @Email
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

    public void setPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
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

    public static User toBulid(BlogStore.UserItem item) {
        User dbUser = new User();
        dbUser.setId(item.getUserId());
        dbUser.setUsername(item.getUsername());
        dbUser.setNickname(item.getNickname());
        dbUser.setPhone(item.getPhone());
        dbUser.setEmail(item.getEmail());
        dbUser.setBirthday(item.getBirthday());
        dbUser.setStatus(item.getStatusValue());
        return dbUser;
    }

}
