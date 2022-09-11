package qqCommon;

import java.io.Serializable;


public class User implements Serializable {
    private String userId;
    private String passwd;

    private static final long serialVersionUID = 1L;    

    public User(){  
    }
    public User(String userId, String passwd) {
        this.userId = userId;
        this.passwd = passwd;
    }

    public String getUserId() {
        return userId;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
