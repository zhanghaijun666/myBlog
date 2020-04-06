package com.blog.service.login;

import com.blog.db.User;
import com.blog.proto.BlogStore;
import org.apache.commons.lang3.StringUtils;
import org.javalite.activejdbc.Base;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoginAuthority {

    private final List<Authority> list = new ArrayList<>();
    private final DataSource dataSource;

    public LoginAuthority(DataSource dataSource) {
        this.dataSource = dataSource;
        list.add(new SystemAuthority());
        list.add(new PlainTextAuthority());
    }

    public User login(String user) {
        User dbUser = null;
        try {
            Base.open(dataSource);
            Base.openTransaction();
            for (Authority authority : list) {
                dbUser = authority.checkUserInfo(user);
                if (!Objects.isNull(dbUser)) {
                    break;
                }
            }
            Base.commitTransaction();
        } catch (Exception e) {
            Base.rollbackTransaction();
        } finally {
            Base.close();
        }
        return dbUser;
    }
}

interface Authority {
    public User checkUserInfo(String user);
}

class SystemAuthority implements Authority {
    @Override
    public User checkUserInfo(String user) {
        return User.findFirst("username = ? ", user);
    }
}

class PlainTextAuthority implements Authority {
    @Override
    public User checkUserInfo(String user) {
        File file = new File("user.txt");
        if (!file.exists() || !file.canRead()) {
            return null;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                if (tempString.startsWith("#")) {
                    continue;
                }
                String[] fields = tempString.split(":");
                if (fields == null || fields.length < 2) {
                    continue;
                }
                String platUser = fields[0].trim();
                String platPass = fields[1].trim();
                if (StringUtils.isBlank(platUser) || StringUtils.isBlank(platPass)) {
                    continue;
                }
                if (StringUtils.equals(user, platUser)) {
                    User dbUser = User.findFirst("username = ? ", platUser);
                    if (Objects.isNull(dbUser)) {
                        dbUser = User.create("username", platUser);
                        dbUser.setString("nickname", platUser);
                        dbUser.setString("password", platUser);
                        dbUser.setLong("status", BlogStore.Status.StatusActive_VALUE);
                        dbUser.saveIt();
                    }
                    return dbUser;
                }
            }
        } catch (Exception e) {
        }
        return null;
    }
}
