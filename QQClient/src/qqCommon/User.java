package qqCommon;

import java.io.Serializable;

/**
 * @author Carson
 * @Version
 */
public class User implements Serializable {
    private String userId;
    private String passwd;

    private static final long serialVersionUID = 1L;   

    public User(){  // The idea of providing a parameterless constructor and then using Setter's method to regulate attributes in other classes
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
